package com.carterz30cal.mobs;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

public class ListenerDismountEvent implements Listener
{
	@EventHandler
	public void onDismount(EntityDismountEvent e)
	{
		if (e.getEntityType() != EntityType.PLAYER) e.setCancelled(true);
	}
}
