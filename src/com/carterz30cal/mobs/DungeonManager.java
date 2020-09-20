package com.carterz30cal.mobs;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.carterz30cal.dungeons.Dungeons;

public class DungeonManager
{
	public static DungeonManager i;
	// we will do something similar to the crafting hashes
	// the hash will be your z coord divided by 1000 and minus 20k
	public HashMap<Integer,Dungeon> dungeons;
	public HashMap<String,Dungeon> warps;
	public Dungeon hub;
	
	public DungeonManager()
	{
		i = this;
		
		dungeons = new HashMap<Integer,Dungeon>();
		warps = new HashMap<String,Dungeon>();
		hub = new Dungeon();
		hub.name = "The Hub";
		hub.spawn = new Location(Bukkit.getWorld("hub"),-10000,24,10004);
		
		File dataFile = new File(Dungeons.instance.getDataFolder(), "dungeons.yml");
		if (!dataFile.exists())
		{
			dataFile.getParentFile().mkdirs();
			Dungeons.instance.saveResource("dungeons.yml",false);
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
		
		for (String d : data.getKeys(false))
		{
			Dungeon dungeon = new Dungeon();
			dungeon.name = data.getString(d + ".name");
			String[] sp = data.getString(d + ".spawn").split(",");
			int hash = hash(Integer.parseInt(sp[2]));
			dungeon.spawn = new Location(Bukkit.getWorld("hub"),Integer.parseInt(sp[0]),Integer.parseInt(sp[1]),Integer.parseInt(sp[2]));
			
			for (String spawn : data.getConfigurationSection(d + ".spawns").getKeys(false))
			{
				String[] spawns = spawn.split(",");
				SpawnPosition spl = new SpawnPosition(new Location(Bukkit.getWorld("hub"),Integer.parseInt(spawns[0]),Integer.parseInt(spawns[1]),Integer.parseInt(spawns[2])));
				dungeon.spawns.put(spl, data.getString(d + ".spawns." + spawn));
			}
			dungeon.oreReplacement = Material.valueOf(data.getString(d + ".oreblock", "BEDROCK"));
			dungeon.orexp = data.getInt(d + ".orexp", 10);
			for (String ore : data.getConfigurationSection(d + ".ores").getKeys(false))
			{
				dungeon.ores.put(Material.valueOf(ore),data.getString(d + ".ores." + ore, "bedrock"));
			}
			warps.put(d, dungeon);
			dungeons.put(hash, dungeon);
		}
	}
	
	public int hash(int z)
	{
		return Math.round((z-20000)/1000f);
	}
}
