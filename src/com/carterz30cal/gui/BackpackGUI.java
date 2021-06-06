package com.carterz30cal.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.player.BackpackItem;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.utility.BackpackSort;
import com.carterz30cal.utility.InventoryHandler;
import com.carterz30cal.utility.StringManipulator;

import net.md_5.bungee.api.ChatColor;

public class BackpackGUI extends GUI
{
	public int sorting = 1; // sorting method index
	
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
				if (item != null)
				{
					if (!ItemBuilder.i.items.containsKey(item.itemType) && !item.itemType.equals("book"))
					{
						int compensation = 5*item.amount;
						
						d.player.sendMessage(ChatColor.GREEN + "An item has been deleted so here is " + compensation + " coins as compensation.");
						d.coins += compensation;
						
						if (!item.enchants.equals(""))
						{
							d.player.sendMessage(ChatColor.GREEN + "- Returned enchants");
							InventoryHandler.addItem(d, ItemBuilder.i.build("book", item.enchants, 1), false);
						}
						if (!item.sharp.equals(""))
						{
							d.player.sendMessage(ChatColor.GREEN + "- Returned sharpeners");
							for (String s : item.sharp.split(",")) InventoryHandler.addItem(d,ItemBuilder.i.build(ItemBuilder.i.sharps.get(s).id,1),false);
						}
					}
					else contents[i] = item.create();
				}
			}
		}
		
		contents[49] = GUICreator.item(Material.FURNACE, ChatColor.GOLD + "Sort", ChatColor.RED + "Method: " + StringManipulator.neat_sorting(BackpackSort.values()[sorting]));
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
		else
		{
			if (page == d.backpackb.size()) d.backpackb.remove(page-1);
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
		if (position == 49)
		{
			if (e.isRightClick())
			{
				sorting++;
				if (sorting == BackpackSort.values().length) sorting = 0;
				save(d);
				createPage(d);
				render(p);
			}
			else if (e.isLeftClick()) 
			{
				save(d);
				try
				{
					InventoryHandler.sortBackpack(d, BackpackSort.values()[sorting]);
				}
				catch (IllegalArgumentException exc)
				{
					d.player.sendMessage(ChatColor.RED + "Your sorting failed.. weird!");
					System.out.println(ChatColor.RED + "[SORTING ERROR] " + d.player.getName() + "'s sort failed.");
				}
				
				createPage(d);
				render(p);
			}
		}
		if (position >= 45 && position < 54) return true;
		else return false;
	}
	@Override
	public boolean handleDrag(InventoryDragEvent e,Player p)
	{
		return false;
	}
}
