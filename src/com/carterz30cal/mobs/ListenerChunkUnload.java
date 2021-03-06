package com.carterz30cal.mobs;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
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
			if (mob != null) mob.destroy(null);
			else if (entity.getType() != EntityType.PLAYER) entity.remove();
		}
	}
}
