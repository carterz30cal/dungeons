package com.carterz30cal.mobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Spider;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ItemBuilder;

public class DungeonMobCreator 
{
	public static DungeonMobCreator i;
	public static final NamespacedKey arrowDamage = new NamespacedKey(Dungeons.instance,"arrowDamage");
	public static int summons; // summon cap is 30 rn
	public HashMap<String,DungeonMobType> types;
	public HashMap<String,MobAction> actions;
	public ArrayList<MobModifier> modifiers;
	
	public DungeonMobCreator()
	{
		i = this;
		
		File dataFile = new File(Dungeons.instance.getDataFolder(), "mobs.yml");
		if (!dataFile.exists())
		{
			dataFile.getParentFile().mkdirs();
			Dungeons.instance.saveResource("mobs.yml",false);
		}
		
		FileConfiguration data = new YamlConfiguration();
		try 
		{
			data.load(dataFile);
        } 
		catch (IOException | InvalidConfigurationException e) 
		{
            e.printStackTrace();
        }
		
		File actionsF = new File(Dungeons.instance.getDataFolder(), "actions.yml");
		if (!actionsF.exists())
		{
			actionsF.getParentFile().mkdirs();
			Dungeons.instance.saveResource("actions.yml",false);
		}
		
		FileConfiguration action = new YamlConfiguration();
		try 
		{
			action.load(actionsF);
        } 
		catch (IOException | InvalidConfigurationException e) 
		{
            e.printStackTrace();
        }
		
		types = new HashMap<String,DungeonMobType>();
		actions = new HashMap<String,MobAction>();
		
		for (String p : action.getKeys(false))
		{
			MobAction a = new MobAction();
			
			for (String ef : action.getConfigurationSection(p).getKeys(false)) 
			{
				a.effects.add(ef.split(";")[0] + ":" + action.getString(p + "." + ef));
			}
			
			actions.put(p, a);
		}
		for (String mob : data.getKeys(false))
		{
			DungeonMobType type = new DungeonMobType();
			
			type.type = EntityType.valueOf(data.getString(mob + ".type").toUpperCase());
			type.damage = data.getInt(mob + ".damage", 0);
			type.name = data.getString(mob + ".name");
			type.maxHealth = data.getInt(mob + ".health", 0);
			type.perk = data.getString(mob + ".perk");
			type.xp = data.getInt(mob + ".xp", 5);
			type.knockbackResist = data.getDouble(mob + ".knockbackResist", 0);
			type.baby = data.getBoolean(mob + ".baby", false);
			type.main = ItemBuilder.i.build(data.getString(mob + ".equipment.main", "null"),null);
			type.offhand = ItemBuilder.i.build(data.getString(mob + ".equipment.offhand", "null"),null);
			if (type.offhand == null) type.offhand = new ItemStack(Material.AIR);
			ItemStack[] equipment = new ItemStack[4];
			equipment[3] = ItemBuilder.i.build(data.getString(mob + ".equipment.helmet", "null"),null);
			equipment[2] = ItemBuilder.i.build(data.getString(mob + ".equipment.chestplate", "null"),null);
			equipment[1] = ItemBuilder.i.build(data.getString(mob + ".equipment.leggings", "null"),null);
			equipment[0] = ItemBuilder.i.build(data.getString(mob + ".equipment.boots", "null"),null);
			type.wearing = equipment;
			
			ArrayList<MobDrop> drops = new ArrayList<MobDrop>();
			if (data.contains(mob + ".drops"))
			{
				for (String drop : data.getConfigurationSection(mob + ".drops").getKeys(false))
				{
					String p = mob + ".drops." + drop;
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
				type.drops = drops;
			}
			else type.drops = null;
			
			String[] acts = data.getString(mob + ".actions", "none").split(",");
			if (acts[0].equals("none")) type.actions = null;
			else 
			{
				type.actions = new MobAction[acts.length];
				for (int act = 0; act < acts.length;act++)
				{
					MobAction o = actions.get(acts[act]);
					if (o != null) type.actions[act] = o;
				}
			}
			ArrayList<String> tags = new ArrayList<String>();
			for (String tag : data.getString(mob + ".tags","").split(","))
			{
				tags.add(tag);
			}
			type.tags = tags;
			
			types.put(mob, type);
		}
		
		modifiers = new ArrayList<MobModifier>();
		
		MobModifier healthy = new MobModifier();
		healthy.name = "Healthy";
		healthy.colour = ChatColor.GREEN;
		healthy.health = 1.2;
		MobModifier tanky = new MobModifier();
		tanky.name = "Tanky";
		tanky.colour = ChatColor.GRAY;
		tanky.damageReduction = 0.5;
		MobModifier hulk = new MobModifier();
		hulk.name = "Hulk";
		hulk.colour = ChatColor.DARK_RED;
		hulk.damageReduction = 0.6;
		hulk.health = 0.25;
		
		modifiers.add(healthy);
		modifiers.add(tanky);
		modifiers.add(hulk);
	}
	
	public ArmorStand createHit(Entity e, int damage,ChatColor colour)
	{
		if (colour == null) colour = ChatColor.WHITE;
		Location sp = new Location(e.getWorld(),0,0,0);
		ArmorStand h = (ArmorStand)sp.getWorld().spawnEntity(sp, EntityType.ARMOR_STAND);
		
		h.setVisible(false);
		h.setMarker(true);
		h.setInvulnerable(true);
		h.setGravity(false);
		h.setCustomName(colour + Integer.toString(damage));
		h.setCustomNameVisible(true);
		
		return h;
	}

	public DungeonMob create(String mob,SpawnPosition pos)
	{
		DungeonMobType type = types.get(mob);
		return create(type,pos,false);
	}
	public DungeonMob create(String mob,SpawnPosition pos,boolean silent)
	{
		DungeonMobType type = types.get(mob);
		return create(type,pos,silent);
	}
	public DungeonMob create (DungeonMobType type,SpawnPosition pos,boolean silent)
	{
		if (type == null) return null;

		LivingEntity entity = (LivingEntity)pos.position.getWorld().spawnEntity(pos.position, type.type);
		Silverfish silverfish = (Silverfish)pos.position.getWorld().spawnEntity(pos.position,EntityType.SILVERFISH);
		ArmorStand armourstand = (ArmorStand)pos.position.getWorld().spawnEntity(pos.position, EntityType.ARMOR_STAND);
		
		armourstand.setVisible(false);
		armourstand.setMarker(true);
		armourstand.setCustomNameVisible(true);
		
		silverfish.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10000000, 5));
		silverfish.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10000000, 255));
		silverfish.setSilent(true);
		silverfish.setRemoveWhenFarAway(false);
		silverfish.addPassenger(armourstand);
		entity.setSilent(silent);
		entity.setRemoveWhenFarAway(false);
		EntityType ty = type.type;
		if (ty == EntityType.ZOMBIE || ty == EntityType.DROWNED 
				|| ty == EntityType.HUSK || ty == EntityType.ZOMBIFIED_PIGLIN)
		{
			if (type.baby) ((Ageable)entity).setBaby();
			else ((Ageable)entity).setAdult();
		}
		
		if (type.type != EntityType.SPIDER)
		{
			entity.getEquipment().clear();
			entity.getEquipment().setArmorContents(type.wearing);
			entity.getEquipment().setItemInMainHand(type.main);
			entity.getEquipment().setItemInOffHand(type.offhand);
		}
		else ((Spider)entity).setAware(true);
		//entity.setAI(true);
		for (PotionEffect potion : entity.getActivePotionEffects()) entity.removePotionEffect(potion.getType());
		entity.addPassenger(silverfish);
		MobModifier mod = null;
		if (Math.random() <= 0.1) mod = modifiers.get((int)Math.round(Math.random()*(modifiers.size()-1)));
		DungeonMob n = new DungeonMob(type,entity,silverfish,armourstand,pos,mod);

		DungeonMob.mobs.put(entity.getUniqueId(), n);
		DungeonMob.silverfi.put(silverfish.getUniqueId(), n);
		DungeonMob.arm.put(armourstand.getUniqueId(), n);
		return n;
	}
}
