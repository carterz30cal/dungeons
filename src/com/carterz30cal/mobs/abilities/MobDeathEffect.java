package com.carterz30cal.mobs.abilities;

import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.utility.ParticleFunctions;

public class MobDeathEffect extends DMobAbility
{
	public MobDeathEffect(FileConfiguration data, String path) {
		super(data, path);

	}

	public void killed(DMob mob)
	{
		if (!Dungeons.instance.isEnabled()) return;

		ParticleFunctions.moving(((LivingEntity)mob.entities.get(0)).getEyeLocation(), Particle.SOUL, 30, 0.1);
	}
}
