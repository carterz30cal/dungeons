package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilitySoulstealer extends AbsAbility
{
	public static ArrayList<DMob> struck;
	
	public AbilitySoulstealer()
	{
		if (struck == null) struck = new ArrayList<DMob> ();
	}
	
	
	
	@Override
	public ArrayList<String> description() {
		ArrayList<String> desc = new ArrayList<String>();
		desc.add(prefix + "Drain");
		desc.add("Struck mobs lose 25 health per second");
		desc.add(" for 10 seconds");
		desc.add(ChatColor.RED + "Ability cannot kill mobs");
		return desc;
	}

	
	@Override
	public int onAttack (DungeonsPlayer d, DMob mob,int damage)
	{
		if (struck.contains(mob)) return damage;
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Dungeons.instance,
				(Runnable) new BukkitRunnable ()
				{
					int procd = 0;
					@Override
					public void run() {
						if (procd == 10 && !isCancelled()) 
						{
							cancel();
							struck.remove(mob);
							return;
						}
						if (mob.health > 25) mob.damage(25, d.player, true);
						procd++;
					}
		
				}, 0,20);
		struck.add(mob);
		return damage;
	}
}
