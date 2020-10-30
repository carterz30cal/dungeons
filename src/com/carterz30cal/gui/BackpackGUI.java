package com.carterz30cal.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.carterz30cal.player.BackpackItem;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

public class BackpackGUI extends GUI
{

	public BackpackGUI(Player p) {
		super(p);
		
		inventory = Bukkit.createInventory(null, 54, "Backpack - Page 1");
		page = 1;
		
		createPage(DungeonsPlayerManager.i.get(p));
		render(p);
	}
	
	private void createPage(DungeonsPlayer d)
	{
		ItemStack[] contents = new ItemStack[52];
		for (int i = 0; i < 52; i++)
		{
			if (i / 9 == 5) 
			{
				if (i % 9 == 2) contents[i] = GUICreator.item(Material.ARROW, "Previous Page", null, 1); 
				else if (i % 9 == 6) contents[i] = GUICreator.item(Material.ARROW, "Next Page", null, 1); 
				else contents[i] = GUICreator.pane();
			}
			else
			{
				BackpackItem item = d.backpackb.get(page-1)[i];
				if (item != null) contents[i] = item.create();
			}
		}
		
		inventory.setContents(contents);
	}
}
