package com.carterz30cal.tasks;

import java.util.ArrayList;

import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

public class TaskTickAbilities extends BukkitRunnable {
	@Override
	public void run()
	{
		for (DungeonsPlayer d : DungeonsPlayerManager.i.players.values()) 
		{
			ArrayList<Class<? extends AbsAbility>> uniques = new ArrayList<>();
			for (AbsAbility a : d.stats.abilities) 
			{
				if (a.isUnique() && uniques.contains(a.getClass())) continue;
				a.onTick(d);
				
				uniques.add(a.getClass());
			}
			d.regainMana(0.00175);
		}
	}

}
