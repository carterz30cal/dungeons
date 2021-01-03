package com.carterz30cal.tasks;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DMob;

public class TaskArmourstand extends BukkitRunnable {

	public DMob entity;
	
	public TaskArmourstand (DMob e)
	{
		entity = e;
		runTaskTimer(Dungeons.instance,1,1);
	}
	@Override
	public void run() {
		if (!entity.display.isValid()) cancel();
		
		Entity top = entity.entities.get(entity.entities.size()-1);
		Location l = top.getLocation();
		l.add(0, 0.1 + top.getHeight(), 0);
		entity.display.teleport(l);
	}

}