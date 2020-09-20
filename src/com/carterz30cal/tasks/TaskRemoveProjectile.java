package com.carterz30cal.tasks;

import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskRemoveProjectile extends BukkitRunnable
{
	public Entity projectile;
	
	public TaskRemoveProjectile(Entity p)
	{
		projectile = p;
	}
	@Override
	public void run()
	{
		projectile.remove();
	}
	
}
