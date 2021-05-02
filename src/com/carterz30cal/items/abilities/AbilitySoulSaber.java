package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DamageType;
import com.carterz30cal.player.DungeonsPlayer;

import net.md_5.bungee.api.ChatColor;

public class AbilitySoulSaber extends AbsAbility
{

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Soul Strike");
		d.add("After hitting an enemy, strike");
		d.add("again after a second dealing");
		d.add("12% of your damage as true damage");
		d.add(ChatColor.RED + "Deals 15% less regular damage");
		return d;
	}
	
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		new BukkitRunnable()
		{

			@Override
			public void run() 
			{
				dMob.damage((int) (damage * 0.12),d, DamageType.TRUE,false);
			}
			
		}.runTaskLater(Dungeons.instance, 20);
		return (int) (damage * 0.85);
	}

}
