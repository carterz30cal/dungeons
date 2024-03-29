package com.carterz30cal.mobs;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ListenerChunkUnload implements Listener 
{
	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent e)
	{
		for (Entity entity : e.getChunk().getEntities())
		{
			DMob mob = DMobManager.get(entity);
			if (mob == null && entity.getType() != EntityType.PLAYER && entity instanceof LivingEntity && ((LivingEntity)entity).getRemoveWhenFarAway()) entity.remove();
			else if (mob != null && !mob.entities.get(0).isInvulnerable()) mob.remove();
			else if (!(entity instanceof LivingEntity)) entity.remove();
		}
	}
}
