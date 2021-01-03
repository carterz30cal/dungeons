package com.carterz30cal.npcs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.carterz30cal.dungeons.Dungeon;
import com.carterz30cal.dungeons.DungeonManager;
import com.carterz30cal.dungeons.Dungeons;

import org.bukkit.ChatColor;

public class NPCManager
{
	public static NPCManager i;

	public ArrayList<NPC> npcs;
	
	public NPCManager()
	{
		i = this;
		
		npcs = new ArrayList<NPC>();
		
		File npcFile = new File(Dungeons.instance.getDataFolder(), "npcs.yml");
		if (!npcFile.exists())
		{
			npcFile.getParentFile().mkdirs();
			Dungeons.instance.saveResource("npcs.yml",false);
		}
		
		FileConfiguration npc = new YamlConfiguration();
		try 
		{
			npc.load(npcFile);
        } 
		catch (IOException | InvalidConfigurationException e) 
		{
            e.printStackTrace();
        }
		
		for (String n : npc.getKeys(false))
		{
			String name = ChatColor.GOLD + npc.getString(n + ".name", "null");
			String[] l = npc.getString(n + ".position", "0,0,0,0,0").split(",");
			Location location = new Location(null, d(l[0]),d(l[1]),d(l[2]), f(l[3]), f(l[4]));
			String data = npc.getString(n + ".skin.data", "null");
			String signature = npc.getString(n + ".skin.sig", "null");
			
			NPC np = new NPC(name,data,signature,location);
			if (npc.contains(n + ".shop")) np.shopId = npc.getString(n + ".shop");
			DungeonManager.i.dungeons.getOrDefault(DungeonManager.i.hash(location.getBlockZ()),DungeonManager.i.hub).npcs.add(np);

			npcs.add(np);
		}
	}
	private double d(String i)
	{
		return Double.parseDouble(i);
	}
	private float f(String i)
	{
		return Float.parseFloat(i);
	}
	public static void sendNPCs()
	{
		for (Dungeon dungeon : DungeonManager.i.dungeons.values())
		{
			for (NPC n : dungeon.npcs) 
			{
				if (dungeon.players.isEmpty()) n.removeHitbox();
				else n.spawnHitbox();
			}
		}
		
		for (NPC n : DungeonManager.i.hub.npcs) 
		{
			if (DungeonManager.i.hub.players.isEmpty()) n.removeHitbox();
			else n.spawnHitbox();
		}
	}
	public static void sendall()
	{
		for (NPC n : NPCManager.i.npcs)
		{
			for (Player p : Bukkit.getOnlinePlayers())
			{
				n.send(p);
			}
		}
	}
	public static void purge()
	{
		for (NPC n : NPCManager.i.npcs)
		{
			n.removeHitbox();
			for (Player p : Bukkit.getOnlinePlayers())
			{
				n.remove(p);
			}
		}
	}
	
}
