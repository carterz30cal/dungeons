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
			"waterway/titan_mobs","waterway/mobs_fishing","waterway/mobs_sands",
			"necropolis/skeletons","necropolis/ghouls","necropolis/slimes","necropolis/crypts_mobs1","necropolis/crypts_mobs2",
			"necropolis/crypts_miniboss","necropolis/diggingmobs","necropolis/digging_mobs3","necropolis/crypts_mobsancient",
			"necropolis/mushroom_mobs","necropolis/mobs_boss","necropolis/mobs_ancient","necropolis/mobs_ancient_boss",
			"unique/unique_mobs","necropolis/mobs_crypts",
			"infested/mobs_spiders","infested/mobs_hunter"
	};
	
	public static DMob get(Entity e)
	{
		if (e == null) return null;
		String s = e.getPersistentDataContainer().getOrDefault(DMob.identifier, PersistentDataType.STRING, null);
		if (s == null) return null;
		if (s.split("_").length > 1) return null;
		return mobs.get(UUID.fromString(s));
	}
	public static String getId(Entity e)
	{
		if (e == null) return null;
		String s = e.getPersistentDataContainer().getOrDefault(DMob.identifier, PersistentDataType.STRING, null);
		if (s == null) return null;
		return s;
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
		if (mob == null || pos == null || pos.position == null) return null;
		return new DMob(types.get(mob),pos,pos.position,true);
	}
	public static DMob spawn(String mob,SpawnPosition pos,boolean modifiers)
	{
		if (mob == null) return null;
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
			
			for (String mob : data.getKeys(false)) 
			{
				if (data.getString(mob + ".type").equals("SKINNED")) types.put(mob, new SkinnedType(data,mob));
				else if (data.getBoolean(mob + ".loadcustom",false)) types.put(mob, new CustomType(data,mob));
				else types.put(mob,new DMobType(data,mob));
			}
		}
	}
}
