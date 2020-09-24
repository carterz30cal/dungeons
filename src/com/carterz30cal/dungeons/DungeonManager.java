package com.carterz30cal.dungeons;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.carterz30cal.mobs.SpawnPosition;

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
			// DungeonMining setup here
			dungeon.mining.xp = data.getInt(d + ".mining.xp", 0);
			for (String o : data.getConfigurationSection(d + ".mining.ores").getKeys(false))
			{
				Material block = Material.valueOf(o);
				dungeon.mining.blocks.put(block, Material.valueOf(data.getString(d + ".mining.ores." + o + ".replacement", "BEDROCK")));
				dungeon.mining.ores.put(block, data.getString(d + ".mining.ores." + o + ".ore", "bad_item"));
			}
			dungeon.mining.requirement = data.getInt(d + ".mining.boss.requirement", 500);
			dungeon.mining.boss = data.getString(d + ".mining.boss.mob", "drenched0");
			String[] loc = data.getString(d + ".mining.boss.spawn").split(",");
			Location location = new Location(Bukkit.getWorld("hub"),Integer.parseInt(loc[0]),Integer.parseInt(loc[1]),Integer.parseInt(loc[2]));
			dungeon.mining.spawn = new SpawnPosition(location);
			warps.put(d, dungeon);
			dungeons.put(hash, dungeon);
		}
	}
	
	public int hash(int z)
	{
		return Math.round((z-20000)/1000f);
	}
}
