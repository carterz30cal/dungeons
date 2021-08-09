package com.carterz30cal.items.abilities.infested;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.DamageType;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.RandomFunctions;
import com.carterz30cal.utility.StdUtils;

public class AbilitySpiritHarvester extends AbsAbility
{
	public static Map<DungeonsPlayer,List<Spirit>> spirits = new HashMap<>();
	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Raging Spirits");
		d.add("When you kill an enemy, has a 50% chance to summon an");
		d.add("angry spirit that attacks nearby enemies for 8 seconds.");
		d.add("Each summon consumes 4 mana per attack. ");
		d.add("Spirits will heal you for 12❤ before they disappear.");
		return d;
	}
	@Override
	public void onKill2(DungeonsPlayer d,DMob dMob)
	{
		spirits.putIfAbsent(d, new ArrayList<>());
		if (RandomFunctions.random(1, 2) == 1) new Spirit(d,dMob.entities.get(0).getLocation());
	}
	public void onEnd(DungeonsPlayer d) 
	{
		for (Spirit s : spirits.getOrDefault(d,new ArrayList<>())) s.cancel();
	};
	public void onLogOut(DungeonsPlayer d) 
	{
		for (Spirit s : spirits.getOrDefault(d,new ArrayList<>())) s.cancel();
	}; 
}
class Spirit extends BukkitRunnable
{
	public Blaze spirit;
	public DungeonsPlayer owner;
	
	public int cooldown;
	
	public Spirit(DungeonsPlayer d,Location l)
	{
		owner = d;
		spirit = (Blaze)Dungeons.w.spawnEntity(l, EntityType.BLAZE);
		spirit.setAI(false);
		spirit.setInvulnerable(true);
		spirit.setSilent(true);
		
		cooldown = 0;
		
		AbilitySpiritHarvester.spirits.get(d).add(this);
		runTaskTimer(Dungeons.instance,20,20);
	}
	@Override
	public void cancel()
	{
		spirit.setHealth(0);
		owner.heal(12);
		super.cancel();
	}
	@Override
	public void run() {
		cooldown += 20;
		if (cooldown == 160 || owner.player.getLocation().distance(spirit.getLocation()) > 25) 
		{
			AbilitySpiritHarvester.spirits.get(owner).remove(this);
			cancel();
		}
		else if (cooldown % 20 == 0)
		{
			int damage = (int)((60d * (owner.stats.mana / 275d)) * owner.stats.damagemod);
			int cost = 4;
			if (owner.player.getInventory().getItemInMainHand() != null && ItemBuilder.getItem(owner.player.getInventory().getItemInMainHand()) != null && ItemBuilder.getItem(owner.player.getInventory().getItemInMainHand()).equals("mythical_spiritsword"))
			{
				cost += 2;
				owner.heal(3);
			}
			
			Set<DMob> nearby = new HashSet<>();
			for (Entity e : Dungeons.w.getNearbyEntities(spirit.getLocation(), 8, 3, 8)) if (DMobManager.get(e) != null) nearby.add(DMobManager.get(e));
			for (DMob mob : nearby) 
			{
				StdUtils.drawLaser(spirit.getEyeLocation(), mob.entities.get(0).getLocation(), 40, 0.9f, Color.ORANGE);
				mob.damage(damage, owner, DamageType.MAGIC, false);
				if (!owner.useMana(cost)) 
				{
					cancel();
					return;
				}
			}
		}
	}
	
	
}
