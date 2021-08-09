package com.carterz30cal.items.abilities.infested;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStats;
import com.carterz30cal.utility.RandomFunctions;

import net.md_5.bungee.api.ChatColor;

public class AbilitySAORef extends AbsAbility {

	public static Map<DMob,Integer> combo = new HashMap<>();
	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Repetition");
		d.add("6% chance to combo strike.");
		d.add("Every hit on the same enemy deals");
		d.add("1% more damage than the last.");
		d.add("This sword deals no sweeping damage.");
		return d;
	}
	
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		if (RandomFunctions.random(1, 100) <= 6) 
		{
			dMob.spawnIndicator(d, ChatColor.GOLD + "CO" + ChatColor.RED + "MB" + ChatColor.DARK_RED + "O!");
			d.player.playSound(dMob.entities.get(0).getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.6f, 1);
			
			new BukkitRunnable()
			{
				int procs = 0;
				@Override
				public void run() {
					if (procs == 4) cancel();
					// TODO Auto-generated method stub
					Bukkit.getServer().getPluginManager().callEvent(new EntityDamageByEntityEvent(d.player,dMob.entities.get(0),DamageCause.ENTITY_ATTACK,1));
					procs++;
				}
				
			}.runTaskTimer(Dungeons.instance, 4,4);
		}
		combo.put(dMob, combo.getOrDefault(dMob, -1) + 1);
		
		int nd = (int) (damage * (1 + (0.01 * combo.get(dMob))));
		return nd;
	} 
	
	public void finalStats(DungeonsPlayerStats s) 
	{
		s.damageSweep = 0;
	}

}
