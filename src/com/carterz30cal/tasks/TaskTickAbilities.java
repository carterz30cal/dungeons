package com.carterz30cal.tasks;

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
			for (AbsAbility a : d.stats.abilities) a.onTick(d);
			d.regainMana(0.00125);
		}
	}

}
