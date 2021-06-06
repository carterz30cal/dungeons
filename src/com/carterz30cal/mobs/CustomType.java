package com.carterz30cal.mobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.mobs.abilities.DMobAbility;
import com.carterz30cal.mobs.packet.EntityTypes;

public class CustomType extends DMobType
{
	public List<EntityTypes> customTypes = new ArrayList<>();
	
	
	public CustomType(FileConfiguration data, String path)
	{
		id = path;
		entities = new ArrayList<EntityType>();
		entities.add(EntityType.UNKNOWN);
		for (String e : data.getString(path + ".type").split(",")) customTypes.add(EntityTypes.valueOf(e.toUpperCase()));
		
		health = data.getInt(path + ".health",1);
		armour = data.getInt(path + ".armour", 1);
		damage = data.getInt(path + ".damage", 0);
		
		level = data.getInt(path + ".level", 1);
		
		
		speed     = data.getDouble(path + ".speed",1);
		kbresist  = data.getDouble(path + ".kbresist",0);
		dmgresist = data.getDouble(path + ".dmgresist",0);
		perk = data.getString(path + ".perk", "none");
		name = data.getString(path + ".name");
		
		boss = data.getBoolean(path + ".boss",false);
		invisible = data.getBoolean(path + ".invisible",false);
		silent = data.getBoolean(path + ".silent",false);
		entityData = new HashMap<Integer,String[]>();
		if (data.contains(path + ".data"))
		{
			for (int i = 0; i < entities.size();i++) entityData.put(i, data.getString(path + ".data." + i, "").split(","));
		}

		equipment = new ItemStack[4];
		if (data.contains(path + ".equipment"))
		{
			main = ItemBuilder.i.build(data.getString(path + ".equipment.main"   , "AIR"),null);
			off  = ItemBuilder.i.build(data.getString(path + ".equipment.offhand", "AIR"),null);

			equipment[3] = ItemBuilder.i.build(data.getString(path + ".equipment.helmet", "AIR"),null);
			equipment[2] = ItemBuilder.i.build(data.getString(path + ".equipment.chestplate", "AIR"),null);
			equipment[1] = ItemBuilder.i.build(data.getString(path + ".equipment.leggings", "AIR"),null);
			equipment[0] = ItemBuilder.i.build(data.getString(path + ".equipment.boots", "AIR"),null);
		}
		
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
				d.rareMessage = data.getBoolean(p + ".rare",false);
				drops.add(d);
			}
		}
		abilities = new ArrayList<DMobAbility>();
		if (data.contains(path + ".abilities")) 
		{
			for (String a : data.getConfigurationSection(path + ".abilities").getKeys(false)) createAbility(data,path + ".abilities." + a,a);
		}
		
		tags = new ArrayList<String>();
		for (String tag : data.getString(path + ".tags","").split(",")) tags.add(tag);
	}

}
