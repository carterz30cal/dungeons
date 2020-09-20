package com.carterz30cal.tasks;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

public class Particles
{
	/*
	 * PARTICLES
	 * 
	 * SUPPORTED SHAPES
	 * - CROSS
	 */
	private static final double m = 2;
	public static void shape(String shape,Particle particle,Location location)
	{
		World w = location.getWorld();
		switch (shape)
		{
		case "cross":
			Location ex = location.clone().subtract(0.5*m, 0, 0);
			for (int x = -5; x < 5; x++)
			{
				w.spawnParticle(particle, ex.add(0.1*m, 0, 0), 1);
			}
			ex = location.clone().subtract(0, 0, 0.5*m);
			for (int z = -5; z < 5; z++)
			{
				w.spawnParticle(particle, ex.add(0, 0, 0.1*m), 1);
			}
			break;
		case "yline":
			break;
			/*
			for (int y = -5; y < 5; y++)
			{
				w.spawnParticle(particle, location.add(0, y*0.05, 0), 1);
			}
			
			*/
		}
	}
}
