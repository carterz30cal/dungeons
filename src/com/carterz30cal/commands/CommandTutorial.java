package com.carterz30cal.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.carterz30cal.areas.WaterwayTutorial;

public class CommandTutorial implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg0 instanceof Player)
		{
			((Player)arg0).teleport(WaterwayTutorial.spawn);
			return true;
		}
		return false;
	}

}
