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
	
	public EnchantGuideGUI(Player p)
	{
		super(p);
		
		page = 1;
		
		inventory = Bukkit.createInventory(null,54,"Enchants");
		int e = 0;
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
					ItemStack book = ItemBuilder.i.build("book",id.get(e) + "," + enchant.max(),1);
					contents[i] = book;
					e++;
				}
			}
		}
		
		inventory.setContents(contents);
		render(p);
	}
	
	public boolean handleClick(InventoryClickEvent e,int position,Player p)
	{
		return true;
	}

	public boolean handleDrag(InventoryDragEvent e,Player p)
	{
		return true;
	}
}
