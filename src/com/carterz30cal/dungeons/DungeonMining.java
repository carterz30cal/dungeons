package com.carterz30cal.dungeons;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.carterz30cal.mobs.DungeonMobCreator;
import com.carterz30cal.mobs.SpawnPosition;

import net.md_5.bungee.api.ChatColor;

public class DungeonMining 
{
	public Dungeon dungeon;
	
	public HashMap<Material,Material> blocks;
	public HashMap<Material,String> ores;
	public int xp;
	
	public String boss;
	public SpawnPosition spawn;
	public int requirement;
	public int progress;
	
	public DungeonMining()
	{
		blocks = new HashMap<Material,Material>();
		ores = new HashMap<Material,String>();
		
		xp = 0;
		boss = null;
		requirement = 10000;
		progress = 0;
	}
	
	public void progress()
	{
		progress++;
		if (progress >= requirement) 
		{
			for (Player p : dungeon.players) p.sendMessage(ChatColor.RED + "A miniboss is spawning in " + dungeon.name + "!");
			progress = 0;
			DungeonMobCreator.i.create(boss, spawn);
		}
	}
}
