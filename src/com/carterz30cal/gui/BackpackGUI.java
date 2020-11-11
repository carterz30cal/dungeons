package com.carterz30cal.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import com.carterz30cal.player.BackpackItem;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

public class BackpackGUI extends GUI
{

	public BackpackGUI(Player p) {
		super(p);
		
		page = 1;
		
		createPage(DungeonsPlayerManager.i.get(p));
		render(p);
	}
	
	private void createPage(DungeonsPlayer d)
	{
		inventory = Bukkit.createInventory(null, 54, "Backpack - Page " + page);
		ItemStack[] contents = new ItemStack[54];
		for (int i = 0; i < 54; i++)
		{
			if (i / 9 == 5) 
			{
				if (i % 9 == 2 && page > 1) contents[i] = GUICreator.item(Material.ARROW, "Previous Page", null, 1); 
				else if (i % 9 == 6) contents[i] = GUICreator.item(Material.ARROW, "Next Page", null, 1); 
				else contents[i] = GUICreator.pane();
			}
			else if (page <= d.backpackb.size())
			{
				BackpackItem[] p = d.backpackb.get(page-1);
				BackpackItem item = p[i];
				if (item != null) contents[i] = item.create();
			}
		}
		
		inventory.setContents(contents);
	}
	public void save(DungeonsPlayer d)
	{
		BackpackItem[] npage = new BackpackItem[45];
		boolean keep = false;
		for (int i = 0; i < 45; i++)
		{
			ItemStack item = inventory.getItem(i);
			if (item == null) continue;
			keep = true;
			npage[i] = new BackpackItem(item,i);
		}
		if (keep)
		{
			if (page-1 >= d.backpackb.size()) d.backpackb.add(npage);
			else d.backpackb.set(page-1, npage);
		}
	}
	@Override
	public boolean handleClick (InventoryClickEvent e, int position, Player p)
	{
		DungeonsPlayer d = DungeonsPlayerManager.i.get(p);
		if (position == 47 && page > 1)
		{
			save(d);
			page--;
			createPage(d);
			render(p);
		}
		else if (position == 51)
		{
			save(d);
			page++;
			createPage(d);
			render(p);
		}
		if (position >= 45 && position < 54) return true;
		else return false;
	}
	@Override
	public boolean handleDrag(InventoryDragEvent e,Player p)
	{
		/*
		boolean stop = false;
		for (int s : e.getInventorySlots()) if (s >= 45 && s < 54) stop = true;
		return stop;
		*/
		return false;
	}
}
