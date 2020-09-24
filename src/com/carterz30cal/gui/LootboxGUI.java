package com.carterz30cal.gui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.carterz30cal.dungeons.EnchantHandler;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.items.ItemLootbox;

import net.md_5.bungee.api.ChatColor;

public class LootboxGUI extends GUI
{
	public static Integer[] order = {21,23,22,20,24,19,25};
	public LootboxGUI(ItemLootbox lootbox, Player p) {
		super(p);
		
		inventory = Bukkit.createInventory(null, 36, lootbox.name);
		ArrayList<Integer> loots = new ArrayList<Integer>();
		for (int i = 0; i < lootbox.items.size();i++)
		{
			if (loots.size() == 7) break;
			int chance = lootbox.chance.get(i);
			if (Math.floor(Math.random()*chance) == 0) loots.add(i);
		}
		
		ItemStack[] contents = new ItemStack[36];
		
		for (int s = 0; s < 36; s++)
		{
			if (s / 9 == 0 || s / 9 == 3 || s % 9 == 0 || s % 9 == 8) contents[s] = GUICreator.pane(Material.BLACK_STAINED_GLASS_PANE);
			else contents[s] = GUICreator.pane(Material.GRAY_STAINED_GLASS_PANE);
		}
		for (int l = 0; l < loots.size(); l++)
		{
			int o = order[l];
			int f = loots.get(l);
			ItemStack reward = ItemBuilder.i.build(lootbox.items.get(f), null);
			
			contents[o] = reward;
			contents[o-9] = pane(lootbox.chance.get(f));
		}
		inventory.setContents(contents);
		render(p);
	}
    @Override
	public boolean handleClick(InventoryClickEvent e,int position,Player p)
	{
    	if (!EnchantHandler.eh.isUIElement(e.getCurrentItem()))
    	{
    		p.getInventory().addItem(e.getCurrentItem());
    		inventory.setItem(position, GUICreator.item(Material.BEDROCK, 
    				e.getCurrentItem().getItemMeta().getDisplayName() + ChatColor.RED + " Already Claimed!", null, 1));
    	}
    	return true;
	}
    
	public static ItemStack pane (int rarity)
	{
		if (rarity == 1) return GUICreator.item(Material.WHITE_STAINED_GLASS_PANE, "Guaranteed", null, 1);
		else if (rarity <= 5) return GUICreator.item(Material.LIME_STAINED_GLASS_PANE, ChatColor.GREEN + "Common", null, 1);
		else if (rarity <= 15) return GUICreator.item(Material.YELLOW_STAINED_GLASS_PANE, ChatColor.GOLD + "Uncommon", null, 1);
		else if (rarity <= 50) return GUICreator.item(Material.ORANGE_STAINED_GLASS_PANE, ChatColor.RED + "Rare", null, 1);
		else return GUICreator.item(Material.RED_STAINED_GLASS_PANE, ChatColor.BOLD + "" + ChatColor.RED + "Super Rare", null, 1);
	}
}
