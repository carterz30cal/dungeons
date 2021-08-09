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
		runTaskTimer(Dungeons.instance,5,1);
	}
	@Override
	public void run() {
		if (entity == null || entity.display == null || !entity.display.isValid())
		{
			if (entity.health > 0) entity.remove();
			cancel();
			return;
		}
	
		entity.update();
		
		Entity top = entity.entities.get(entity.entities.size()-1);
		Location l = top.getLocation();
		l.add(0, 0.1 + top.getHeight(), 0);
		entity.display.teleport(l);
	}

}
