package com.carterz30cal.mobs;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

public class ListenerDismountEvent implements Listener
{
	@EventHandler
	public void onDismount(EntityDismountEvent e)
	{
		if (e.getEntityType() != EntityType.PLAYER) e.setCancelled(true);
	}
	@EventHandler
	public void onToggleSneak (PlayerToggleSneakEvent e)
	{
		DungeonsPlayer d = DungeonsPlayerManager.i.get(e.getPlayer());
		
		for (AbsAbility a : d.stats.abilities) if (e.isSneaking()) a.onSneak(d);
	}
}
