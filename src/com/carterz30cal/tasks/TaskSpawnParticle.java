package com.carterz30cal.tasks;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskSpawnParticle extends BukkitRunnable
{
	public Location location;
	public Particle particle;
	public int amount;
	
	public TaskSpawnParticle(Particle p,Location l,int a)
	{
		particle = p;
		location = l;
		amount = a;
	}
	@Override
	public void run()
	{
		location.getWorld().spawnParticle(particle, location, amount,0,0,0,0.01);
	}
}
