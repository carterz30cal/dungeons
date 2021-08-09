package com.carterz30cal.items.abilities.infested;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.DamageType;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.Mafs;
import com.carterz30cal.utility.ParticleFunctions;
import com.carterz30cal.utility.StdUtils;

import net.md_5.bungee.api.ChatColor;

public class AbilityHurricane extends AbsAbility
{
	public static List<DungeonsPlayer> onCooldown = new ArrayList<>();
	
	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Storm of Blades");
		d.add("Right click to release the storm, dealing 0.9x your damage");
		d.add("to all visible non-boss enemies within 25 blocks, costing 125 mana.");
		d.add("This attack repeats until you run out of mana.");
		d.add("Cooldown of 30s which starts when the attack finishes.");
		return d;
	}
	
	public boolean onInteract(DungeonsPlayer d, PlayerInteractEvent e) 
	{
		if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && !onCooldown.contains(d) && d.canUseMana(125))
		{
			Set<DMob> mobs = new HashSet<>();
			for (Entity en : Dungeons.w.getNearbyEntities(d.player.getLocation(), 25, 5, 25)) 
			{
				DMob m = DMobManager.get(en);
				if (m != null && !m.type.boss && !StdUtils.areBlocksOnVector(m.entities.get(0).getLocation(), d.player.getLocation())) mobs.add(m);
			}
			if (mobs.size() == 0) return false;
			
			double dpm = 360d / mobs.size();
			
			onCooldown.add(d);
			d.player.playSound(d.player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
			Location pivot = d.player.getLocation();
			int m = 0;
			for (DMob mob : mobs)
			{
				int r = 5;
				Location l = d.player.getLocation().add(Mafs.getCircleX(dpm*m, r),0,Mafs.getCircleZ(dpm*m, r));
				while (StdUtils.areBlocksOnVector(l, pivot) && r > 1) 
				{
					r--;
					l = d.player.getLocation().add(Mafs.getCircleX(dpm*m, r),0,Mafs.getCircleZ(dpm*m, r));
				}
				mob.entities.get(0).teleport(l);
				((LivingEntity)mob.entities.get(0)).addPotionEffect(new PotionEffect(PotionEffectType.SLOW,200,10,true));
				m++;
			}
			DMob[] mobArray = mobs.toArray(new DMob[0]);
			new BukkitRunnable()
			{
				int c = 0;
				@Override
				public void run() {
					int cm = c % mobArray.length;
					DMob target = mobArray[cm];
					
					double fr = target.entities.get(0).getLocation().distance(pivot) - 0.5;
					Location pl = pivot.clone().add(Mafs.getCircleX(dpm*cm, fr),1.5,Mafs.getCircleZ(dpm*cm, fr));
					
					mobs.removeIf((DMob m) -> m.health < 1);
					if (cm == 0 || mobs.size() == 0) 
					{
						if (!d.useMana(125)) 
						{
							cancel();
							new BukkitRunnable()
							{

								@Override
								public void run() {
									onCooldown.remove(d);
									d.player.sendMessage(ChatColor.GREEN + "You may resummon the storm.");
								}
								
							}.runTaskLater(Dungeons.instance, 600);
						}
					}
					if (target.health > 0)
					{
						target.damage((int) (d.stats.damage * 0.9), d, DamageType.PHYSICAL, true);
						Dungeons.w.strikeLightningEffect(target.entities.get(0).getLocation());
						ParticleFunctions.stationary(pl, Particle.SWEEP_ATTACK, 1);
						d.player.playSound(target.entities.get(0).getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1, 1);
					}
					
					c++;
				}
				
			}.runTaskTimer(Dungeons.instance, 1, 1);
			
			
			
			
			return true;
		}
		return false;
	}
}
