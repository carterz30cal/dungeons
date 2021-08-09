package com.carterz30cal.player;

import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.items.magic.ItemWand;

public class BackpackItem
{
	public String itemType;
	public String enchants;
	public String sharp;
	public String rune;
	public String extras;
	public String spell;
	public String modifier;
	public int fuel;
	public int slot;
	public int amount;
	
	public static HashMap<String,String> standardNames;
	
	public void save(String path)
	{
		save(Dungeons.instance.getPlayerConfig(),path);
	}
	public void save(FileConfiguration players, String path)
	{
		if (itemType.equals("uielement")) return;
		String slotPath = path + "." + slot;
		if (!players.contains(slotPath))
		{
			players.createSection(slotPath);
		}
		
		players.set(slotPath + ".itemType", itemType);
		//if (!customName.equals("")) players.set(slotPath + ".name", customName);
		//else players.set(slotPath + ".name", null);
		if (!enchants.equals("")) players.set(slotPath + ".enchants", enchants);
		else players.set(slotPath + ".enchants", null);
		if (!sharp.equals("")) players.set(slotPath + ".sharp", sharp);
		else players.set(slotPath + ".sharp", null);
		if (!extras.equals("")) players.set(slotPath + ".extras", extras);
		else players.set(slotPath + ".extras",null);
		if (amount > 1) players.set(slotPath + ".amount", amount);
		else players.set(slotPath + ".amount", null);
		if (!rune.equals("")) players.set(slotPath + ".rune", rune);
		if (!spell.equals("")) players.set(slotPath + ".spell", spell);
		else players.set(slotPath + ".spell", null);
		if (!modifier.equals("")) players.set(slotPath + ".modifier", modifier);
		else players.set(slotPath + ".modifier", null);
		if (fuel > 0) players.set(slotPath + ".fuel", fuel);
		else players.set(slotPath + ".fuel", null);
	}
	
	public ItemStack create()
	{
		if (itemType.equals("uielement")) return null;
		ItemStack base = ItemBuilder.i.build(itemType, null,enchants,sharp);
		base = ItemBuilder.addExtras(base,extras);
		if (!rune.equals("")) base = ItemBuilder.i.addRune(base, rune);
		base.setAmount(amount);
		
		ItemMeta meta = base.getItemMeta();
		if (!spell.equals("")) meta.getPersistentDataContainer().set(ItemWand.kSpell, PersistentDataType.STRING,spell);
		if (!modifier.equals("")) meta.getPersistentDataContainer().set(ItemWand.kModifier, PersistentDataType.STRING,modifier);
		ItemBuilder.setFuel(meta, fuel);
		
		meta = ItemBuilder.i.updateMeta(meta, null);
		base.setItemMeta(meta);
		
		return base;
	}
	
	
	public BackpackItem (String path,int Slot)
	{
		if (standardNames == null) standardNames = new HashMap<String,String>();
		
		// path should be data.<playeruuid>.backpack.<slot>
		FileConfiguration players = Dungeons.instance.getPlayerConfig();
		
		slot = Slot;
		
		itemType = players.getString(path + ".itemType");
		
		//customName = players.getString(path + ".name","");
		enchants = players.getString(path + ".enchants","");
		sharp = players.getString(path + ".sharp","");
		amount = players.getInt(path + ".amount",1);
		extras = players.getString(path + ".extras","");
		rune = players.getString(path + ".rune","");
		spell = players.getString(path + ".spell", "");
		modifier = players.getString(path + ".modifier","");
		fuel = players.getInt(path + ".fuel", 0);
	}
	
	public BackpackItem (ItemStack item,int Slot)
	{
		if (standardNames == null) standardNames = new HashMap<String,String>();
		
		slot = Slot;
		amount = item.getAmount();
		enchants = "";
		if (!item.hasItemMeta()) itemType = item.getType().toString();
		PersistentDataContainer p = item.getItemMeta().getPersistentDataContainer();
		//customName = ChatColor.stripColor(item.getItemMeta().getDisplayName());
		enchants = p.getOrDefault(ItemBuilder.kEnchants, PersistentDataType.STRING,"");
		sharp = p.getOrDefault(ItemBuilder.kSharps, PersistentDataType.STRING,"");
		itemType = p.getOrDefault(ItemBuilder.kItem, PersistentDataType.STRING, item.getType().toString());
		extras = p.getOrDefault(ItemBuilder.kExtras, PersistentDataType.STRING, "");
		rune = p.getOrDefault(ItemBuilder.kRunic, PersistentDataType.STRING, "");
		
		spell = p.getOrDefault(ItemWand.kSpell, PersistentDataType.STRING, "");
		modifier = p.getOrDefault(ItemWand.kModifier, PersistentDataType.STRING, "");
		fuel = ItemBuilder.getFuel(item.getItemMeta());
	}
}
