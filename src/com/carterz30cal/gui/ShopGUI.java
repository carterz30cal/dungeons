package com.carterz30cal.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.items.Shop;
import com.carterz30cal.items.ShopItem;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

public class ShopGUI extends GUI
{
	public Shop shop;
	
	public String[] items;
	public Integer[] costs;
	public String[] enchants;
	public ShopGUI(Shop s,Player player)
	{
		super(player);
		shop = s;
		
		inventory = Bukkit.createInventory(null, 54,shop.name);
		ItemStack[] contents = new ItemStack[54];
		items = new String[54];
		costs = new Integer[54];
		enchants = new String[54];
		int ootem = 0;
		for (int i = 0; i < 54; i++)
		{
			if ((i+1) % 9 < 2 || i / 9 == 0 || i / 9 == 5) contents[i] = GUICreator.pane(Material.BLACK_STAINED_GLASS_PANE);
			else
			{
				if (ootem >= shop.items.size()) 
				{
					contents[i] = GUICreator.pane(Material.WHITE_STAINED_GLASS_PANE);
				}
				else
				{
					ShopItem it = shop.items.get(ootem);
							
					ItemStack item = ItemBuilder.i.build(it.item, null,it.enchants,it.sharps);

					items[i] = it.item;
					costs[i] = it.price;
					ItemMeta u = item.getItemMeta();
					List<String> lore = u.getLore();
					lore.add("");
					if (it.price > 1) lore.add(ChatColor.GOLD + "Costs " + it.price + " coins");
					else if (it.price == 1) lore.add(ChatColor.GOLD + "Costs 1 coin");
					else lore.add(ChatColor.GOLD + "FREE!");
					u.setLore(lore);
					item.setItemMeta(u);
					
					contents[i] = item;
					
					ootem++;
				}
				
			}
		}
		inventory.setContents(contents);
		render(player);
	}
	@Override
	public boolean handleClick(InventoryClickEvent e,int position,Player p)
	{
		if (e.getClick() != ClickType.LEFT) return true;
		if (position < 54)
		{
			if (items[position] != null)
			{
				DungeonsPlayer d = DungeonsPlayerManager.i.get(p);
				if (d.coins-costs[position] >= 0 && p.getInventory().firstEmpty() > -1)
				{
					ItemStack item;
					if (enchants[position] != null) item = ItemBuilder.i.build(items[position],null,enchants[position]);
					else item = ItemBuilder.i.build(items[position], null);
					
					d.coins -= costs[position];
					p.getInventory().addItem(item);
					
					p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
				}
				else 
				{
					if (p.getInventory().firstEmpty() == -1) p.sendMessage(ChatColor.RED + "No inventory space!");
					else p.sendMessage(ChatColor.RED + "Not enough coins!");
					p.playSound(p.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1, 1);
				}
			}
		}
		return true;
	}
}
