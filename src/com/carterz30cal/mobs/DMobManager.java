package com.carterz30cal.mobs;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ItemBuilder;

public class DMobManager 
{
	public static DMobManager i;
	public static final NamespacedKey arrowDamage = new NamespacedKey(Dungeons.instance,"arrowDamage");
	public static HashMap<UUID,DMob> mobs;
	public static HashMap<String,DMobType> types;
	public static HashMap<String,DMobModifier> modifiers;
	
	
	private static final String[] files = {
			"waterway/drenched"   ,"waterway/bossmobs","waterway/uniquemobs","waterway/soaked","waterway/fishes",
			"necropolis/skeletons","necropolis/ghouls","necropolis/slimes"
	};
	
	public static DMob get(Entity e)
	{
		if (e == null) return null;
		String s = e.getPersistentDataContainer().getOrDefault(DMob.identifier, PersistentDataType.STRING, null);
		if (s == null) return null;
		return mobs.get(UUID.fromString(s));
	}
	public static void end()
	{
		for (DMob mob : mobs.values()) if (mob != null) mob.remove();
	}
	public static ArmorStand hit(Entity e,int damage, ChatColor colour)
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
	public static DMob spawn(String mob,SpawnPosition pos)
	{
		return new DMob(types.get(mob),pos,pos.position,true);
	}
	public static DMob spawn(String mob,SpawnPosition pos,boolean modifiers)
	{
		return new DMob(types.get(mob),pos,pos.position,modifiers);
	}
	public DMobManager()
	{
		i = this;
		
		mobs = new HashMap<UUID,DMob>();
		types = new HashMap<String,DMobType>();
		modifiers = new HashMap<String,DMobModifier>();
		DMobModifier.base = new DMobModifier();
		for (String f : files)
		{
			File file = null;
			try
			{
				file = File.createTempFile("mobfile." + f, null);
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
			if (file == null) continue;
			ItemBuilder.copyToFile(Dungeons.instance.getResource(f + ".yml"),file);
			FileConfiguration data = new YamlConfiguration();
			try 
			{
				data.load(file);
			}
			catch (IOException | InvalidConfigurationException e) 
			{ 
		        e.printStackTrace();
		    }
			
			for (String mob : data.getKeys(false)) types.put(mob,new DMobType(data,mob));
		}
	}
}
