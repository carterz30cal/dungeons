package com.carterz30cal.items;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;

import com.carterz30cal.dungeons.Dungeons;

public class ShopManager 
{
	public static ShopManager i;
	public static HashMap<String,Shop> shops;
	public static HashMap<Entity,Shop> positions;
	
	public ShopManager ()
	{
		i = this;
		
		shops = new HashMap<String,Shop>();
		positions = new HashMap<Entity,Shop>();
		
		File shopFile = new File(Dungeons.instance.getDataFolder(), "shops.yml");
		if (!shopFile.exists())
		{
			shopFile.getParentFile().mkdirs();
			Dungeons.instance.saveResource("shops.yml",false);
		}
		
		FileConfiguration shop = new YamlConfiguration();
		try { shop.load(shopFile); }
		catch (IOException | InvalidConfigurationException e) { e.printStackTrace(); }
		
		for (String s : shop.getKeys(false))
		{
			Shop sh = new Shop();
			sh.name = shop.getString(s + ".name", "Ye Olde Shoppe");
			sh.items = shop.getString(s + ".items").split("-");
			shops.put(s,sh);
		}
	}
	
}
