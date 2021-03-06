package com.carterz30cal.tasks;

import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.abilities.DMobAbility;

public class TaskMobAbilities extends BukkitRunnable
{
	public DMob mob;
	public TaskMobAbilities (DMob m)
	{
		mob = m;
		
		runTaskTimer(Dungeons.instance,1,1);
	}
	@Override
	public void run()
	{
		if (mob.health < 1) cancel();
		if (!((LivingEntity)mob.entities.get(0)).hasAI()) return;
		
		for (DMobAbility a : mob.type.abilities) a.tick(mob);
	}
	
}
