package com.carterz30cal.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.carterz30cal.dungeons.Dungeons;

public class ShopManager 
{
	public static ShopManager i;
	public static HashMap<String,Shop> shops;
	
	
	public ShopManager ()
	{
		i = this;
		
		shops = new HashMap<String,Shop>();
		
		File file = null;
		try
		{
			file = File.createTempFile("shopfile.shops", null);
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}

		ItemBuilder.copyToFile(Dungeons.instance.getResource("shops.yml"),file);
		FileConfiguration shop = new YamlConfiguration();
		try 
		{
			shop.load(file);
		} 
		catch (IOException | InvalidConfigurationException e) 
		{
		    e.printStackTrace();
		}
		
		for (String s : shop.getKeys(false))
		{
			Shop sh = new Shop();
			sh.name = shop.getString(s + ".name", "Ye Olde Shoppe");
			sh.items = new ArrayList<ShopItem>();
			for (String i : shop.getConfigurationSection(s + ".items").getKeys(false))
			{
				ShopItem item = new ShopItem();
				item.item = i.split(";")[0];
				item.price = shop.getInt(s + ".items." + i + ".price", 10);
				item.enchants = shop.getString(s + ".items." + i + ".enchants", "");
				item.sharps = shop.getString(s + ".items." + i + ".sharps", "");
				item.sale = shop.getInt(s + ".items." + i + ".saleprice",-1);
				sh.items.add(item);
			}
			shops.put(s,sh);
		}
	}
	
}
