package com.carterz30cal.items.abilities.infested;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.DamageType;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.player.DungeonsPlayerStats;

public class AbilityRoseSword extends AbsAbility
{
	public static Map<DungeonsPlayer,Integer> healing = new HashMap<>();
	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Flower of the Ages");
		d.add("The sceptre stores all healing you receive.");
		d.add("You may right click to release this healing");
		d.add("in a burst of energy, healing all players for 2x this amount");
		d.add("and dealing it as magic damage to all enemies within 20 blocks.");
		d.add("Ability costs " + luxiumCost(5));
		d.add(ChatColor.GREEN + "+5% health.");
		return d;
	}
	
	public boolean onInteract(DungeonsPlayer d, PlayerInteractEvent e) 
	{
		int power = healing.getOrDefault(d, 0);
		if (power >= 100 && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && d.useLuxium(5))
		{
			Set<DMob> n = new HashSet<>();
			for (Entity en : Dungeons.w.getNearbyEntities(d.player.getLocation(), 19, 7, 19)) 
			{
				if (en instanceof Player && DungeonsPlayerManager.i.get((Player)en) != null) DungeonsPlayerManager.i.get((Player)en).heal(power*2);
				else if (DMobManager.get(en) != null) n.add(DMobManager.get(en));
			}
			for (DMob m : n) 
			{
				m.damage(power, d, DamageType.MAGIC, false);
				((LivingEntity)m.entities.get(0)).addPotionEffect(new PotionEffect(PotionEffectType.SLOW,60,2,true));
			}
			d.player.playSound(d.player.getLocation(), Sound.ENTITY_BLAZE_HURT, 0.8f, 0.8f);
			healing.put(d, 0);
			return true;
		}
		return false;
	}
	
	public void finalStats(DungeonsPlayerStats s) 
	{
		s.health *= 1.05;
	}
	public void onHeal(DungeonsPlayer d,int amount) 
	{
		healing.put(d, healing.getOrDefault(d, 0) + amount);
	}
}
