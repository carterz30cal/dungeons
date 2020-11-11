package com.carterz30cal.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;

public class TaskScheduleLater extends BukkitRunnable {

	private BukkitRunnable sch;
	private int later;
	public TaskScheduleLater (BukkitRunnable s, int l)
	{
		sch = s;
		later = l;
	}
	@Override
	public void run() {
		sch.runTaskLater(Dungeons.instance, later);

	}

}
