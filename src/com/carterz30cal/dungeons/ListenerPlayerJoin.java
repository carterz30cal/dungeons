package com.carterz30cal.dungeons;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.carterz30cal.bosses.AliveBossHandler;
import com.carterz30cal.bosses.BossManager;
import com.carterz30cal.npcs.NPC;
import com.carterz30cal.npcs.NPCManager;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

public class ListenerPlayerJoin implements Listener
{
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		DungeonsPlayerManager.i.create(e.getPlayer());
		
		if (e.getPlayer().getName() == "carterz30cal")
		{
			e.setJoinMessage(ChatColor.GOLD + e.getPlayer().getDisplayName() + " is here");
			Dungeons.w.strikeLightning(e.getPlayer().getLocation());
		}
		else e.setJoinMessage(ChatColor.AQUA + e.getPlayer().getDisplayName() + " has joined");
		
		for (NPC n : NPCManager.i.npcs)
		{
			n.send(e.getPlayer());
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		DungeonsPlayer d = DungeonsPlayerManager.i.get(e.getPlayer());
		for (AliveBossHandler boss : BossManager.bosses.values()) if (boss != null && boss.boss != null) boss.boss.remove(d);
		e.getPlayer().closeInventory();
		DungeonsPlayerManager.i.save(d);
		DungeonsPlayerManager.i.players.remove(e.getPlayer().getUniqueId());
		e.setQuitMessage(ChatColor.DARK_AQUA + e.getPlayer().getDisplayName() + " has left");
	}
}
