package com.carterz30cal.mobs.abilities;

import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;

import com.carterz30cal.mobs.DMob;
import com.carterz30cal.tasks.TaskMobAbilities;
import com.carterz30cal.utility.Mafs;
import com.carterz30cal.utility.ParticleFunctions;

public class MobParticleEffects extends DMobAbility 
{
	public Particle type;
	public double wobble;
	public double radius;
	public int offset;
	public double step;
	public boolean inverted;
	public MobParticleEffects(FileConfiguration data, String path) {
		super(data, path);
		
		type = Particle.valueOf(data.getString(path + ".type", "REDSTONE"));
		wobble = data.getDouble(path + ".wobble", 1);
		radius = data.getDouble(path + ".radius", 0.8);
		offset = data.getInt(path + ".offset", 0);
		step = data.getInt(path + ".step", 4);
		inverted = data.getBoolean(path + ".inverted", false);
	}

	public void tick(DMob mob)
	{
		double alt = (TaskMobAbilities.tick + offset) / step;
		double x = Mafs.getCircleX(alt, radius);
		double z = Mafs.getCircleZ(alt, radius);
		double y = Math.sin(TaskMobAbilities.tick/step) * wobble;
		if (inverted)
		{
			Vector dir = mob.entities.get(0).getLocation().getDirection().normalize();
			ParticleFunctions.stationary(mob.entities.get(0).getLocation().add(
					(y * dir.getZ())+(Mafs.getCircleZ(alt, radius) * (1-dir.getZ())),
					(mob.entities.get(0).getHeight()/2) + x,
					(y * dir.getX())+(Mafs.getCircleZ(alt, radius) * (1-dir.getX()))), type, 1);
		}
		else ParticleFunctions.stationary(mob.entities.get(0).getLocation().add(x,(mob.entities.get(0).getHeight()/2) + y,z), type, 1);
	}
}
