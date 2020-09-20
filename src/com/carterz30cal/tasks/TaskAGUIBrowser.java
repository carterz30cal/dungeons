package com.carterz30cal.tasks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.gui.GUI;
import com.carterz30cal.gui.GUICreator;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

public class TaskAGUIBrowser extends BukkitRunnable
{
	public GUI g;
	public Player p;
	
	public TaskAGUIBrowser (GUI gui, Player pl)
	{
		g = gui;
		p = pl;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int index = (g.page-1)*28;
		DungeonsPlayer d = DungeonsPlayerManager.i.get(p);
		for (int slot = 0; slot < 54; slot++)
		{
			if (slot / 9 != 0 && slot / 9 != 5 && (slot+1) % 9 > 1)
			{
				if (index >= ItemBuilder.i.items.size()) g.inventory.setItem(slot, GUICreator.pane(Material.BLACK_STAINED_GLASS_PANE));
				else g.inventory.setItem(slot, ItemBuilder.i.build(ItemBuilder.keys[index], d));
				index++;
			}
		}
		if (g.page > 1) g.inventory.setItem(47, GUICreator.item(Material.ARROW, ChatColor.GOLD + "Page " + (g.page-1), null, 1));
		else g.inventory.setItem(47,GUICreator.pane(Material.LIME_STAINED_GLASS_PANE));
		if (g.page*28 < ItemBuilder.keys.length) g.inventory.setItem(51,GUICreator.item(Material.ARROW, ChatColor.GOLD + "Page " + (g.page+1), null, 1));
		else g.inventory.setItem(51,GUICreator.pane(Material.LIME_STAINED_GLASS_PANE));
	}

}
