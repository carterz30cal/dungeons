package com.carterz30cal.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.carterz30cal.gui.MarketGUI;

import net.md_5.bungee.api.ChatColor;

public class CommandMarket implements CommandExecutor
{
	public static boolean marketDisabled = true;
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3)
	{
		if (marketDisabled)
		{
			sender.sendMessage(ChatColor.RED + "This feature is currently disabled.");
			return true;
		}
		if (!(sender instanceof Player)) return true;
		
		new MarketGUI((Player)sender,1);
		return true;
	}

}
