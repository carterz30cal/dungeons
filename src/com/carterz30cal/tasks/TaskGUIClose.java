package com.carterz30cal.tasks;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.gui.GUI;

public class TaskGUIClose extends BukkitRunnable
{
	private GUI gui;
	private Player player;
	private boolean drop;
	public TaskGUIClose(GUI g,Player p,boolean d)
	{
		gui = g;
		player = p;
		drop = d;
	}

	@Override
	public void run()
	{
		gui.drop = drop;
		player.closeInventory();
		gui.drop = true;
	}
}
