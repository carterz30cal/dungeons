package com.carterz30cal.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.utility.StringManipulator;

import net.md_5.bungee.api.ChatColor;

public class CommandPlaytime implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) 
	{
		if (arg0 instanceof Player) arg0.sendMessage(ChatColor.GOLD + "You have " + 
	StringManipulator.time(DungeonsPlayerManager.i.get((Player)arg0).playtime) + "of playtime!");
		return true;
	}

}
