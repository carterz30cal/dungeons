package com.carterz30cal.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.carterz30cal.utility.Stats;

import net.md_5.bungee.api.ChatColor;

public class CommandStats implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3)
	{
		if (!sender.isOp()) return true;
		
		sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Server Stats");
		sender.sendMessage(ChatColor.GRAY + "Coins spent in shops: " + Stats.coinsspent);
		sender.sendMessage(ChatColor.GRAY + "Mobs killed: " + Stats.mobskilled);
		sender.sendMessage(ChatColor.GRAY + "Crypts Opened: " + Stats.cryptsopened);
		return true;
	}

}
