package com.carterz30cal.player;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ItemBuilder;

public class BackpackItem
{
	public String itemType;
	public String customName;
	public String enchants;
	public String sharp;
	public int slot;
	public int amount;
	
	public static HashMap<String,String> standardNames;
	
	public void save(String path)
	{
		// path should be <playeruuid>.backpack
		
		FileConfiguration players = Dungeons.instance.getPlayerConfig();
		
		String slotPath = path + "." + slot;
		if (!players.contains(slotPath))
		{
			players.createSection(slotPath);
		}
		
		players.set(slotPath + ".itemType", itemType);
		if (!customName.equals("")) players.set(slotPath + ".name", customName);
		else players.set(slotPath + ".name", null);
		if (!enchants.equals("")) players.set(slotPath + ".enchants", enchants);
		else players.set(slotPath + ".enchants", null);
		if (!sharp.equals("")) players.set(slotPath + ".sharp", sharp);
		else players.set(slotPath + ".sharp", null);
		if (amount > 1) players.set(slotPath + ".amount", amount);
		else players.set(slotPath + ".amount", null);
	}
	
	public ItemStack create(boolean highlight)
	{
		ItemStack base = ItemBuilder.i.build(itemType, null,enchants,sharp);
		base.setAmount(amount);
		return base;
	}
	
	
	public BackpackItem (String path,int Slot)
	{
		if (standardNames == null) standardNames = new HashMap<String,String>();
		
		// path should be data.<playeruuid>.backpack.<slot>
		FileConfiguration players = Dungeons.instance.getPlayerConfig();
		
		slot = Slot;
		
		itemType = players.getString(path + ".itemType");
		customName = players.getString(path + ".name","");
		enchants = players.getString(path + ".enchants","");
		sharp = players.getString(path + ".sharp","");
		amount = players.getInt(path + ".amount",1);
	}
	
	public BackpackItem (ItemStack item,int Slot)
	{
		if (standardNames == null) standardNames = new HashMap<String,String>();
		
		slot = Slot;
		amount = item.getAmount();
		enchants = "";
		if (!item.hasItemMeta()) itemType = item.getType().toString();
		PersistentDataContainer p = item.getItemMeta().getPersistentDataContainer();
		customName = ChatColor.stripColor(item.getItemMeta().getDisplayName());
		enchants = p.getOrDefault(ItemBuilder.kEnchants, PersistentDataType.STRING,"");
		sharp = p.getOrDefault(ItemBuilder.kSharps, PersistentDataType.STRING,"");
		itemType = p.getOrDefault(ItemBuilder.kItem, PersistentDataType.STRING, item.getType().toString());

	}
}
