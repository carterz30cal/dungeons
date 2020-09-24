package com.carterz30cal.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.carterz30cal.dungeons.Dungeon;
import com.carterz30cal.dungeons.DungeonManager;

public class CommandHub implements CommandExecutor
{
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player send = (Player)sender;
			if (args.length == 0 || args[0].equals("hub")) send.teleport(DungeonManager.i.hub.spawn);
			else 
			{
				Dungeon d = DungeonManager.i.warps.getOrDefault(args[0], null);
				if (d == null) return false;
				else send.teleport(d.spawn);
			}
			
		}
		return true;
	}

}
