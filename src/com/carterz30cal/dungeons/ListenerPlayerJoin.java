package com.carterz30cal.dungeons;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.carterz30cal.npcs.NPC;
import com.carterz30cal.npcs.NPCManager;
import com.carterz30cal.player.DungeonsPlayerManager;

public class ListenerPlayerJoin implements Listener
{
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		DungeonsPlayerManager.i.create(e.getPlayer());
		
		e.setJoinMessage(ChatColor.AQUA + e.getPlayer().getDisplayName() + " has joined");
		
		for (NPC n : NPCManager.i.npcs)
		{
			n.send(e.getPlayer());
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		e.getPlayer().closeInventory();
		DungeonsPlayerManager.i.save(DungeonsPlayerManager.i.get(e.getPlayer()));
		DungeonsPlayerManager.i.players.remove(e.getPlayer().getUniqueId());
		e.setQuitMessage(ChatColor.DARK_AQUA + e.getPlayer().getDisplayName() + " has left");
	}
}
