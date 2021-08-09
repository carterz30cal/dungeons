package com.carterz30cal.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.player.CharacterSkill;

import net.md_5.bungee.api.ChatColor;

public class CommandLeaderboard implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3)
	{
		sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Top players: ");

		List<Spot> board = new ArrayList<>();
		for (String u : Dungeons.instance.getPlayerConfig().getKeys(false))
		{
			Spot s = new Spot();
			s.name = Bukkit.getOfflinePlayer(UUID.fromString(u)).getName();
			s.level = Dungeons.instance.getPlayerConfig().getInt(u + ".level", 0);
			s.exp = Dungeons.instance.getPlayerConfig().getLong(u + ".experience",0);
			board.add(s);
		}
		board.removeIf((Spot s) -> s.name == null);
		board.sort((a,b) -> a.level > b.level ? -1 : (a.level == b.level ? (a.exp > b.exp ? -1 : 1) : 1));
		for (int b = 0; b < 20 && b < board.size();b++) sender.sendMessage(ChatColor.GOLD +"" + (b+1) + ". " + CharacterSkill.prettyText(board.get(b).level) + " " + board.get(b).name);
		
		return true;
	}

}

class Spot
{
	int level;
	long exp;
	String name;
}
