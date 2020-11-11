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
			DungeonMob mob = DungeonMob.mobs.get(entity.getUniqueId());
			if (mob != null) mob.destroy(null);
			else if (entity.getType() == EntityType.SLIME) entity.remove();
		}
	}
}
