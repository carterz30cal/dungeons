package com.carterz30cal.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import com.carterz30cal.enchants.AbsEnchant;
import com.carterz30cal.enchants.EnchantManager;
import com.carterz30cal.items.ItemBuilder;

public class EnchantGuideGUI extends GUI
{
	private Player owner;
	public EnchantGuideGUI(Player p)
	{
		super(p);
		
		page = 1;
		
		inventory = Bukkit.createInventory(null,54,"Enchants - Page " + page);
		int e = (page-1)*28;
		ItemStack[] contents = new ItemStack[54];
		List<AbsEnchant> enchants = new ArrayList<>();
		List<String> id = new ArrayList<>();
		for (String ench : EnchantManager.enchantments.keySet()) 
		{
			AbsEnchant enchant = EnchantManager.get(ench);
			if (enchant.hide()) continue;
			enchants.add(enchant);
			id.add(ench);
		}
		for (int i = 0; i < 54; i++)
		{
			if (i % 9 == 0 || i % 9 == 8 || i / 9 == 0 || i / 9 == 5) contents[i] = GUICreator.pane();
			else
			{
				if (e >= enchants.size()) contents[i] = GUICreator.pane(Material.BLACK_STAINED_GLASS_PANE);
				else
				{
					AbsEnchant enchant = enchants.get(e);
					ItemStack book = ItemBuilder.i.build("book",id.get(e) + "," + Math.max(1, enchant.max()),1);
					contents[i] = book;
					e++;
				}
			}
		}
		contents[52] = GUICreator.item(Material.ARROW, "Page " + (page+1), null);
		if (page > 1) contents[46] = GUICreator.item(Material.ARROW, "Page " + (page-1), null);
		inventory.setContents(contents);
		
		owner = p;
		render(p);
	}
	
	public void draw()
	{
		inventory = Bukkit.createInventory(null,54,"Enchants - Page " + page);
		int e = (page-1)*28;
		ItemStack[] contents = new ItemStack[54];
		List<AbsEnchant> enchants = new ArrayList<>();
		List<String> id = new ArrayList<>();
		for (String ench : EnchantManager.enchantments.keySet()) 
		{
			AbsEnchant enchant = EnchantManager.get(ench);
			if (enchant.hide()) continue;
			enchants.add(enchant);
			id.add(ench);
		}
		for (int i = 0; i < 54; i++)
		{
			if (i % 9 == 0 || i % 9 == 8 || i / 9 == 0 || i / 9 == 5) contents[i] = GUICreator.pane();
			else
			{
				if (e >= enchants.size()) contents[i] = GUICreator.pane(Material.BLACK_STAINED_GLASS_PANE);
				else
				{
					AbsEnchant enchant = enchants.get(e);
					ItemStack book = ItemBuilder.i.build("book",id.get(e) + "," + Math.max(1, enchant.max()),1);
					contents[i] = book;
					e++;
				}
			}
		}
		contents[52] = GUICreator.item(Material.ARROW, "Page " + (page+1), null);
		if (page > 1) contents[46] = GUICreator.item(Material.ARROW, "Page " + (page-1), null);
		inventory.setContents(contents);
		render(owner);
	}
	
	public boolean handleClick(InventoryClickEvent e,int position,Player p)
	{
		if (position == 52)
		{
			page++;
			draw();
		}
		else if (position == 46 && page > 1)
		{
			page--;
			draw();
		}
		return true;
	}

	public boolean handleDrag(InventoryDragEvent e,Player p)
	{
		return true;
	}
}
