package com.carterz30cal.tasks;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobType;
import com.carterz30cal.mobs.packet.EntitySkinned;

public class TaskSetEquipment extends BukkitRunnable 
{
	public DMob mob;
	
	public TaskSetEquipment(DMob m)
	{
		mob = m;
		runTaskLater(Dungeons.instance,1);
	}
	
	@Override
	public void run() {
		DMobType type = mob.type;
		for (Entity enti : mob.entities)
		{
			LivingEntity entity = (LivingEntity)enti;
			
			if (entity instanceof Player) EntitySkinned.alive.get(entity.getEntityId()).dress(type.equipment,type.main,type.off);
			entity.getEquipment().setArmorContents(type.equipment);
			entity.getEquipment().setItemInMainHand(type.main);
			entity.getEquipment().setItemInOffHand(type.off);
		}
	}

}
