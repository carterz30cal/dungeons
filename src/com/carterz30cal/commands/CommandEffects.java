package com.carterz30cal.commands;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.potions.ActivePotion;
import com.carterz30cal.utility.StringManipulator;

import net.md_5.bungee.api.ChatColor;

public class CommandEffects implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (args.length == 1 && sender.isOp())
		{
			DungeonsPlayer d = DungeonsPlayerManager.i.get(Bukkit.getPlayer(args[0]));
			if (d != null)
			{
				if (d.stats.potioneffects.size() == 0) sender.sendMessage(ChatColor.RED + "Player has no active effects!");
				else
				{
					sender.sendMessage(ChatColor.GOLD + d.player.getName() + "'s active effects:");
					for (Entry<ActivePotion,Integer> potion : d.stats.potioneffects.entrySet())
					{
						sender.sendMessage(ChatColor.RED + StringManipulator.capitalise(potion.getKey().type.name()) + " "
								+ StringManipulator.romanNumerals[potion.getKey().level-1] + " - " + StringManipulator.time(potion.getValue()));
					}
				}
			}
			return true;
		}
		else if (sender instanceof Player)
		{
			DungeonsPlayer d = DungeonsPlayerManager.i.get((Player)sender);
			if (d.stats.potioneffects.size() == 0) sender.sendMessage(ChatColor.RED + "You have no active effects!");
			else
			{
				sender.sendMessage(ChatColor.GOLD + "Your active effects:");
				for (Entry<ActivePotion,Integer> potion : d.stats.potioneffects.entrySet())
				{
					sender.sendMessage(ChatColor.RED + StringManipulator.capitalise(potion.getKey().type.name()) + " "
							+ StringManipulator.romanNumerals[potion.getKey().level-1] + " - " + StringManipulator.time(potion.getValue()));
				}
			}
			return true;
		}
		return false;
	}

}
