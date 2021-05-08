package com.carterz30cal.dungeons;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.SpawnPosition;

public class DungeonManager
{
	public static DungeonManager i;
	// we will do something similar to the crafting hashes
	// the hash will be your z coord divided by 1000 and minus 20k
	public HashMap<Integer,Dungeon> dungeons;
	public ArrayList<Dungeon> ordered;
	public HashMap<String,Dungeon> warps;
	public Dungeon hub;
	
	public DungeonManager()
	{
		i = this;
		
		dungeons = new HashMap<Integer,Dungeon>();
		ordered = new ArrayList<Dungeon>();
		warps = new HashMap<String,Dungeon>();
		hub = new Dungeon();
		hub.name = "The Hub";
		hub.spawn = new Location(Bukkit.getWorld("hub"),-10000,24,10004);
		hub.killsperlevel = 100000;
		warps.put("hub", hub);
		
		File dataFile = null;
		try
		{
			dataFile = File.createTempFile("dungeons", null);
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
		if (dataFile == null) return;
		ItemBuilder.copyToFile(Dungeons.instance.getResource("dungeons.yml"),dataFile);
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
			dungeon.id = d;
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
			if (data.contains(d + ".modifiers")) for (String m : data.getString(d + ".modifiers").split(",")) dungeon.modifiers.add(DMobManager.modifiers.get(m));
			// DungeonMining setup here
			dungeon.mining.xp = data.getInt(d + ".mining.xp", 0);
			for (String o : data.getConfigurationSection(d + ".mining.ores").getKeys(false))
			{
				Material block = Material.valueOf(o);
				dungeon.mining.blocks.put(block, Material.valueOf(data.getString(d + ".mining.ores." + o + ".replacement", "BEDROCK")));
				dungeon.mining.ores.put(block, data.getString(d + ".mining.ores." + o + ".ore", "bad_item"));
				dungeon.mining.hardness.put(block, data.getInt(d + ".mining.ores." + o + ".hardness",1000));
			}
			dungeon.mining.requirement = data.getInt(d + ".mining.boss.requirement", 500);
			dungeon.mining.boss = data.getString(d + ".mining.boss.mob", "drenched0");
			String[] loc = data.getString(d + ".mining.boss.spawn","0,0,0").split(",");
			Location location = new Location(Bukkit.getWorld("hub"),Integer.parseInt(loc[0]),Integer.parseInt(loc[1]),Integer.parseInt(loc[2]));
			dungeon.mining.spawn = new SpawnPosition(location);
			
			// Dungeon Explorer stuff
			dungeon.icon_data = data.getString(d + ".explorer.icon.data");
			dungeon.icon_sig = data.getString(d + ".explorer.icon.sig");
			dungeon.expl_lore = data.getString(d + ".explorer.lore",ChatColor.RED + "PLACEHOLDER");
			dungeon.expl_coins = data.getInt(d + ".explorer.extracoins",0);
			dungeon.killsperlevel = data.getInt(d + ".explorer.killsperlevel",1);
			
			dungeon.requiredtutorial = data.getString(d + ".reqtutorial","none");
			
			String[] chance = data.getString(d + ".mining.rare.chance","0/1").split("/");
			dungeon.mining.chance = Integer.parseInt(chance[0]);
			dungeon.mining.outof = Integer.parseInt(chance[1]);
			dungeon.mining.rareore = Material.valueOf(data.getString(d + ".mining.rare.material","BEDROCK"));
			String[] cors = data.getString(d + ".mining.rare.correction","BEDROCK").split(",");
			List<Material> ms = new ArrayList<>();
			for (String c : cors) ms.add(Material.valueOf(c));
			dungeon.mining.rarecorrection = ms;
			
			dungeon.unfinished = data.getBoolean(d + ".unfinished", false);
			
			dungeon.fishingmobs = data.getString(d + ".fishingmobs","").split(",");
			
			warps.put(d, dungeon);
			dungeons.put(hash, dungeon);
			ordered.add(dungeon);
		}
	}
	
	public int hash(int z)
	{
		return Math.round((z-20000)/1000f);
	}
}
