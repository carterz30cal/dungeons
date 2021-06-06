package com.carterz30cal.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.carterz30cal.player.CharacterSkill;
import com.carterz30cal.utility.StringManipulator;

import net.md_5.bungee.api.ChatColor;

public class CommandLeaderboard implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3)
	{
		sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Top online players: ");
		for (int i = 0; i < 20; i++)
		{
			CharacterSkill s = CharacterSkill.leaderboard[i];
			if (s == null) break;
			sender.sendMessage(ChatColor.GOLD + "" + (i+1) + ". " + s.owner.player.getDisplayName() + ChatColor.AQUA + " - " + StringManipulator.truncateLess(s.experience) + " XP");
		}
		return true;
	}

}
