package com.carterz30cal.items.abilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStats;

import net.md_5.bungee.api.ChatColor;

public class AbilityDevastation extends AbsAbility
{
	public static Map<DungeonsPlayer,HitStack> stacks = new HashMap<>();
	@Override
	public ArrayList<String> description() 
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Devastation");
		d.add("Every hit within a second of the");
		d.add("last deals 10% more damage. Stacks.");
		d.add("If you reach 25 hits, the combo resets");
		d.add("and you will strike for 8x damage!");
		d.add(ChatColor.RED + "Lose 33% of your health and armour.");
		return d;
	}
	
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage)
	{
		HitStack hit = stacks.getOrDefault(d, new HitStack(d));
		if (stacks.containsKey(d)) hit.hit();
		else stacks.put(d, hit);
		
		if (hit.hits == 60) 
		{
			dMob.spawnIndicator(d, ChatColor.GOLD + "DEVA" + ChatColor.RED + "STAT" + ChatColor.DARK_RED + "ING!");
			d.player.playSound(dMob.entities.get(0).getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.4f, 0.9f);
		}
		return (int) (damage * (1 + (hit.hits-1) * 0.10));
	}
	
	public void finalStats(DungeonsPlayerStats s) 
	{
		s.health *= 0.67;
		s.armour *= 0.67;
	}

}

class HitStack extends BukkitRunnable
{
	public int hits;
	int ticksRemaining;
	public DungeonsPlayer player;
	
	public HitStack(DungeonsPlayer p)
	{
		player = p;
		hits = 1;
		ticksRemaining = 20;
		
		runTaskTimer(Dungeons.instance,1,1);
	}
	public void hit()
	{
		if (ticksRemaining != 20) hits++;
		ticksRemaining = 20;
		
		if (hits == 21) hits = 60;
		else if (hits == 61) hits = 1;
	}
	@Override
	public void run() 
	{
		ticksRemaining--;
		if (ticksRemaining == 0)
		{
			cancel();
			AbilityDevastation.stacks.remove(player);
		}
	}
}
