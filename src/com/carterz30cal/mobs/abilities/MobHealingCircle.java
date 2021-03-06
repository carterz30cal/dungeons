package com.carterz30cal.mobs.abilities;

import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.utility.Mafs;


public class MobHealingCircle extends DMobAbility
{
	public int healing;
	public int radius;
	public int selfheal;
	
	public int cooldown;
	private int ticker;
	public MobHealingCircle(FileConfiguration data, String path) 
	{
		super(data, path);
		
		radius = data.getInt(path + ".radius", 3);
		healing = data.getInt(path + ".healing", 0);
		selfheal = data.getInt(path + ".selfheal", 0);
		
		cooldown = data.getInt(path + ".cooldown", 20);
	}

	@Override
	public void tick(DMob mob)
	{
		ticker++;
		if (ticker < cooldown) return;
		ticker = 0;
		
		for (int d = 0; d < 360; d++)
		{
			double lx = mob.entities.get(0).getLocation().getX() + Mafs.getCircleX(d,radius);
			double lz = mob.entities.get(0).getLocation().getZ() + Mafs.getCircleZ(d,radius);
			Dungeons.w.spawnParticle(Particle.VILLAGER_HAPPY,
					lx,mob.entities.get(0).getLocation().getY() + 0.1,lz,
					1,0,0,0,0);
		}
		for (Entity e : Dungeons.w.getNearbyEntities(mob.entities.get(0).getLocation(), radius, radius, radius))
		{
			DMob mb = DMobManager.get(e);
			if (mb == null) continue;
			
			if (mb == mob) mb.heal(selfheal);
			else mb.heal(healing);
		}
	}
}
