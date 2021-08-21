package com.carterz30cal.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import com.carterz30cal.enchants.AbsEnch;
import com.carterz30cal.enchants.AbsEnchTypes;
import com.carterz30cal.items.ItemBuilder;

public class EnchantGuideGUI extends GUI
{
	private Player owner;
	public EnchantGuideGUI(Player p)
	{
		super(p);
		
		page = 1;
		owner = p;
		
		draw();
	}
	
	public void draw()
	{
		inventory = Bukkit.createInventory(null,54,"Enchants - Page " + page);
		int e = (page-1)*28;
		ItemStack[] contents = new ItemStack[54];
		List<AbsEnchTypes> enchants = new ArrayList<>(Arrays.asList(AbsEnchTypes.values()));

		for (int i = 0; i < 54; i++)
		{
			if (i % 9 == 0 || i % 9 == 8 || i / 9 == 0 || i / 9 == 5) contents[i] = GUICreator.pane();
			else
			{
				if (e >= enchants.size()) contents[i] = GUICreator.pane(Material.BLACK_STAINED_GLASS_PANE);
				else
				{
					AbsEnch enchant = AbsEnchTypes.get(enchants.get(e).toString());
					ItemStack book = ItemBuilder.i.build("book",enchants.get(e).toString() + "," + enchant.max(),1);
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
