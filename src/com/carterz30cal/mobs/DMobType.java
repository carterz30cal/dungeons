package com.carterz30cal.mobs;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.mobs.abilities.*;

public class DMobType
{
	public String name;
	
	public int level;
	
	public int health;
	public int armour;
	public int damage;
	
	public double speed;
	public double kbresist;
	public double dmgresist;
	public double coinmulti;
	public String perk;
	
	public ItemStack main;
	public ItemStack off;
	public ItemStack[] equipment;
	
	public ArrayList<EntityType> entities;
	public ArrayList<MobDrop> drops;
	public ArrayList<String> tags;
	public HashMap<Integer,String[]> entityData;
	
	public ArrayList<DMobAbility> abilities;
	
	public boolean boss;
	public boolean invisible;
	public boolean silent;
	
	public String id;
	
	public DMobType()
	{
		entities = new ArrayList<EntityType>();
		entityData = new HashMap<Integer,String[]>();
		equipment = new ItemStack[4];
		drops = new ArrayList<MobDrop>();
		tags = new ArrayList<String>();
		
		abilities = new ArrayList<DMobAbility>();
	}
	public DMobType (FileConfiguration data,String path)
	{
		id = path;
		entities = new ArrayList<EntityType>();
		for (String e : data.getString(path + ".type").split(",")) entities.add(EntityType.valueOf(e.toUpperCase()));
		
		health = data.getInt(path + ".health",1);
		armour = data.getInt(path + ".armour", 0);
		damage = data.getInt(path + ".damage", 0);
		
		level = data.getInt(path + ".level", 1);
		
		
		speed     = data.getDouble(path + ".speed",1);
		kbresist  = data.getDouble(path + ".kbresist",0);
		dmgresist = data.getDouble(path + ".dmgresist",0);
		coinmulti = data.getDouble(path + ".coinm",1);
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
	
	protected void createAbility(FileConfiguration data, String path,String a)
	{
		DMobAbility ability = null;
		
		switch (a)
		{
		case "regen":
			ability = new MobRegen(data,path);
			break;
		case "airwalker":
			ability = new MobAirWalker(data,path);
			break;
		case "swap":
			ability = new MobSwap(data,path);
			break;
		case "caster":
			ability = new MobCaster(data,path);
			break;
		case "rewarder":
			ability = new MobDamageRewarder(data,path);
			break;
		case "arrowshield":
			ability = new MobArrowShield(data,path);
			break;
		case "healcircle":
			ability = new MobHealingCircle(data,path);
			break;
		case "overkill":
			ability = new MobOverkill(data,path);
			break;
		case "summons":
			ability = new MobSummons(data,path);
			break;
		case "targeter":
			ability = new MobTargeter(data,path);
			break;
		case "boss":
			ability = new MobBoss(data,path);
			break;
		case "laser":
			ability = new MobLaser(data,path);
			break;
		case "owned":
			ability = new MobOwned(data,path);
			break;
		default: return;
		}
		abilities.add(ability);
	}
	
	
}
