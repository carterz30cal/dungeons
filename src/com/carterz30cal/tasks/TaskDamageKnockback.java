package com.carterz30cal.tasks;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class TaskDamageKnockback extends BukkitRunnable
{
	public Damageable e;
	public double modifier;
	
	public TaskDamageKnockback (Entity en, double m)
	{
		e = (Damageable)en;
		modifier = m;
	}
	@Override
	public void run() 
	{
		Vector vel = e.getVelocity();
		vel.setX(vel.getX()*modifier);
		vel.setZ(vel.getZ()*modifier);
		vel.setY(vel.getY()*modifier);

		e.setVelocity(vel);
	}
	
}
