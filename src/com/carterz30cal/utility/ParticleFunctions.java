package com.carterz30cal.utility;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;

import com.carterz30cal.dungeons.Dungeons;

public class ParticleFunctions
{
	public static void stationary(Location loc,Particle type,int amount)
	{
		if (type == Particle.REDSTONE) Dungeons.w.spawnParticle(type, loc, amount,0,0,0,0,new DustOptions(Color.BLUE,0.8f));
		else Dungeons.w.spawnParticle(type, loc, amount,0,0,0,0);
	}
}
