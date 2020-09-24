package com.carterz30cal.dungeons;

import java.util.HashMap;

import org.bukkit.Material;

import com.carterz30cal.mobs.SpawnPosition;

public class DungeonMining 
{
	public HashMap<Material,Material> blocks;
	public HashMap<Material,String> ores;
	public int xp;
	
	public SpawnPosition boss;
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
}
