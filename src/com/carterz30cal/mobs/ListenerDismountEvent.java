package com.carterz30cal.mobs;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.event.entity.EntityDismountEvent;

import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.tasks.TaskSetEquipment;

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
	
	@EventHandler
	public void onDropItem(EntityDropItemEvent e)
	{
		ItemStack dropped = e.getItemDrop().getItemStack();
		if (dropped == null) return;
		if (e.getEntityType() == EntityType.CHICKEN && dropped.getType() == Material.EGG)
		{
			e.setCancelled(true);
			e.getItemDrop().remove();
		}
	}
	
	@EventHandler
	public void onTarget(EntityTargetEvent e)
	{
		if (e.getTarget() == null) return;
		e.setCancelled(e.getTarget().getType() != EntityType.PLAYER);
	}
	
	@EventHandler
	public void onTransform(EntityTransformEvent e)
	{
		DMob mob = DMobManager.get(e.getEntity());
		if (mob != null) new TaskSetEquipment(mob);
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onTeleport(EntityTeleportEvent e)
	{
		e.setCancelled(true);
	}
}
