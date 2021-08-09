package com.carterz30cal.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.ChatColor;

public class CommandVote implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) 
	{
		if (arg0 instanceof Player) 
		{
			arg0.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Voting Links");
			arg0.sendMessage(ChatColor.GOLD + "Each vote grants 24 hours of 35% extra xp");
			//arg0.sendMessage(ChatColor.WHITE + "- minecraftservers.org/server/612995");
			arg0.sendMessage(ChatColor.WHITE + "- minecraft-mp.com/server-s284523");
			//arg0.sendMessage(ChatColor.WHITE + "- minebrowse.com/server/2821");
			//arg0.sendMessage(ChatColor.WHITE + "- topg.org/minecraft-servers/server-628661");
		}
		return true;
	}

}
