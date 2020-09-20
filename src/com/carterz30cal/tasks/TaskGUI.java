package com.carterz30cal.tasks;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.gui.GUI;
import com.carterz30cal.gui.MenuType;

public class TaskGUI extends BukkitRunnable
{
	private GUI gui;
	private Player player;
	public TaskGUI(GUI g,Player p)
	{
		gui = g;
		player = p;
	}

	@Override
	public void run()
	{
		if (gui.type == MenuType.ENCHANTING)
		{
			gui.drop = false;
			gui.render(player);
			gui.drop = true;
		}
	}
}
