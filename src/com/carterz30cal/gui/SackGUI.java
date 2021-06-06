package com.carterz30cal.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SackGUI extends GUI
{

	public SackGUI(Player p) 
	{
		super(p);
	
		inventory = Bukkit.createInventory(null, 54, "Ingredient Sack");
		ItemStack[] contents = new ItemStack[54];
		for (int i = 0; i < 54; i++)
		{
			if (i % 9 == 0 || i % 9 == 8 || i < 9 || i / 9 == 5) contents[i] = GUICreator.pane();
		}
		
		inventory.setContents(contents);
		render(p);
	}

}
