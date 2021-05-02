package com.carterz30cal.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ItemBuilder;

import net.md_5.bungee.api.ChatColor;

public class MarketGUI extends GUI
{
	public static Map<Integer,MarketOrder> orders = new HashMap<>();
	
	public static void init()
	{
		File market = new File(Dungeons.instance.getDataFolder(), "market.yml");
		if (!market.exists())
		{
			market.getParentFile().mkdirs();
			Dungeons.instance.saveResource("market.yml",false);
		}
		FileConfiguration data = new YamlConfiguration();
		try 
		{
            data.load(market);
        } 
		catch (IOException | InvalidConfigurationException e) 
		{
            e.printStackTrace();
        }
		
		for (String o : data.getKeys(false))
		{
			MarketOrder order = new MarketOrder();
			
			order.id = data.getInt(o + ".id");
			order.item = data.getString(o + ".item");
			order.amount = data.getInt(o + ".amount");
			order.price = data.getInt(o + ".price");
			order.owner = UUID.fromString(data.getString(o + ".owner"));
		}
		
		// test orders
		// 68d8b205-1a82-42a4-be93-cf8df405d879 PlsGiveVIP12 UUID
		MarketOrder order = new MarketOrder();
		order.owner = UUID.fromString("68d8b205-1a82-42a4-be93-cf8df405d879");
		order.id = 0;
		order.item = "bone";
		order.amount = 256;
		order.price = 1;
		orders.put(0, order);
		order = new MarketOrder();
		order.owner = UUID.fromString("68d8b205-1a82-42a4-be93-cf8df405d879");
		order.id = 1;
		order.item = "water_fragment";
		order.amount = 1024;
		order.price = 1;
		orders.put(1, order);
		
	}
	
	/*
	 * 1+ = main
	 * 0 = buying
	 * -1 = creating order
	 * 
	 */
	
	private int getId(int slot)
	{
		int id = (page-1)*54;
		for (int i = 0; i < slot; i++)
		{
			if (i % 9 == 0 || i % 9 == 8 || i / 9 == 0 || i / 9 == 5) continue;
			id++;
		}
		return id;
	}
	public MarketGUI(Player p,int page) 
	{
		super(p);
		
		ItemStack[] contents = new ItemStack[54];
		inventory = Bukkit.createInventory(null, 54,"Material Marketplace");
		
		int id = (page-1)*54;
		for (int i = 0; i < 54; i++)
		{
			if (i % 9 == 0 || i % 9 == 8 || i / 9 == 0 || i / 9 == 5) 
			{
				contents[i] = GUICreator.pane();
				continue;
			}
			
			MarketOrder order = orders.get(id);
			if (order == null) continue;
			
			ItemStack item = ItemBuilder.i.build(order.item, 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.GRAY + "" + order.amount + "x " + meta.getDisplayName());
			List<String> lore = new ArrayList<>();
			
			lore.add(ChatColor.GRAY + "Price: " + ChatColor.GOLD + order.price);
			lore.add(ChatColor.GRAY + "Seller: " + ChatColor.RED + Bukkit.getOfflinePlayer(order.owner).getName());
			
			meta.setLore(lore);
			item.setItemMeta(meta);
			contents[i] = item;
			
			id++;
		}
		inventory.setContents(contents);
		render(p);
	}

	@Override
	public boolean handleClick(InventoryClickEvent e,int position,Player p)
	{
		
		return true;
	}
}

class MarketOrder
{
	public UUID owner;
	public int id;
	
	public String item;
	
	public int amount;
	public int price;
}