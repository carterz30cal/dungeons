package com.carterz30cal.mobs;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.carterz30cal.items.ItemBuilder;

public class DMobType
{
	public String name;
	
	public int health;
	public int damage;
	
	public double speed;
	public double kbresist;
	public double dmgresist;
	public String perk;
	
	public ItemStack main;
	public ItemStack off;
	public ItemStack[] equipment;
	
	public ArrayList<EntityType> entities;
	public ArrayList<MobDrop> drops;
	public ArrayList<String> tags;
	public HashMap<Integer,String[]> entityData;
	
	public boolean boss;
	
	public DMobType()
	{
		entities = new ArrayList<EntityType>();
		entityData = new HashMap<Integer,String[]>();
		equipment = new ItemStack[4];
		drops = new ArrayList<MobDrop>();
		tags = new ArrayList<String>();
	}
	public DMobType (FileConfiguration data,String path)
	{
		entities = new ArrayList<EntityType>();
		for (String e : data.getString(path + ".type").split(",")) entities.add(EntityType.valueOf(e.toUpperCase()));
		
		health = data.getInt(path + ".health",1);
		damage = data.getInt(path + ".damage", 0);
		
		speed     = data.getDouble(path + ".speed",1);
		kbresist  = data.getDouble(path + ".kbresist",0);
		dmgresist = data.getDouble(path + ".dmgresist",0);
		perk = data.getString(path + ".perk", "none");
		name = data.getString(path + ".name");
		
		boss = data.getBoolean(path + ".boss",false);
		entityData = new HashMap<Integer,String[]>();
		if (data.contains(path + ".data"))
		{
			for (int i = 0; i < entities.size();i++) entityData.put(i, data.getString(path + ".data." + i, "").split(","));
		}

		main = ItemBuilder.i.build(data.getString(path + ".equipment.main", "AIR"),null);
		off = ItemBuilder.i.build(data.getString(path + ".equipment.offhand", "AIR"),null);

		equipment = new ItemStack[4];
		equipment[3] = ItemBuilder.i.build(data.getString(path + ".equipment.helmet", "AIR"),null);
		equipment[2] = ItemBuilder.i.build(data.getString(path + ".equipment.chestplate", "AIR"),null);
		equipment[1] = ItemBuilder.i.build(data.getString(path + ".equipment.leggings", "AIR"),null);
		equipment[0] = ItemBuilder.i.build(data.getString(path + ".equipment.boots", "AIR"),null);
		
		drops = new ArrayList<MobDrop>();
		if (data.contains(path + ".drops"))
		{
			for (String drop : data.getConfigurationSection(path + ".drops").getKeys(false))
			{
				String p = path + ".drops." + drop;
				MobDrop d = new MobDrop();
				
				d.chance = Double.parseDouble(data.getString(p + ".drop", "1/1").split("/")[0])
						/
						Double.parseDouble(data.getString(p + ".drop", "1/1").split("/")[1]);
				d.enchants = data.getString(p + ".enchants", null);
				d.item = drop.split(";")[0];
				d.minAmount = Integer.parseInt(data.getString(p + ".amount", "1-1").split("-")[0]);
				d.maxAmount = Integer.parseInt(data.getString(p + ".amount", "1-1").split("-")[1]);
				drops.add(d);
			}
		}
		
		tags = new ArrayList<String>();
		for (String tag : data.getString(path + ".tags","").split(",")) tags.add(tag);
	}
}