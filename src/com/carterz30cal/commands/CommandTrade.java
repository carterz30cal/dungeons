package com.carterz30cal.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.gui.TradeGUI;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

import net.md_5.bungee.api.ChatColor;

public class CommandTrade implements CommandExecutor
{
	public static final Map<DungeonsPlayer,DungeonsPlayer> requests = new HashMap<>();
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3)
	{
		if (!(sender instanceof Player)) return true;
		if (arg3.length == 0) return false;
		
		
		if (Bukkit.getPlayer(arg3[0]) == null) return false;
		DungeonsPlayer requester = DungeonsPlayerManager.i.get((Player)sender);
		DungeonsPlayer requested = DungeonsPlayerManager.i.get(Bukkit.getPlayer(arg3[0]));
		
		if (requester == requested)
		{
			sender.sendMessage(ChatColor.RED + "Are you so alone you'd trade with yourself?");
			return true;
		}
		
		if (requests.containsKey(requester))
		{
			sender.sendMessage(ChatColor.RED + "You already have an outgoing trade request!");
			return true;
		}
		
		// check if the other person has requested a trade
		if (requests.getOrDefault(requested, null) == requester)
		{
			requests.remove(requested);
			// open trade menu
			
			TradeGUI trade1 = new TradeGUI(requester.player);
			TradeGUI trade2 = new TradeGUI(requested.player);
			trade1.linked = trade2;
			trade2.linked = trade1;
			
		}
		else
		{
			requests.put(requester, requested);
			
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					requests.remove(requester);
				}
			}.runTaskLater(Dungeons.instance, 600);
			
			
			sender.sendMessage(ChatColor.GREEN + "Trade requested with " + requested.player.getPlayerListName());
			sender.sendMessage(ChatColor.GREEN + "They have 30 seconds to accept!");
			
			requested.player.sendMessage(requester.player.getPlayerListName() + ChatColor.GREEN + " has requested to trade!");
			requested.player.sendMessage("Type /trade " + requester.player.getName() + ChatColor.GREEN + " to accept!");
			requested.player.playSound(requested.player.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1, 1);
		}
		
		return true;
	}

}
