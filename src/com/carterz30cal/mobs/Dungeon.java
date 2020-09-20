package com.carterz30cal.mobs;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;


public class Dungeon
{
	public String name;
	public Location spawn;
	public HashMap<SpawnPosition,String> spawns;
	
	public Material oreReplacement;
	public HashMap<Material,String> ores;
	public int orexp;
	
	public boolean activated;
	
	public Dungeon()
	{
		spawns = new HashMap<SpawnPosition,String>();
		ores = new HashMap<Material,String>();
		oreReplacement = Material.BEDROCK;
		activated = true;
	}
}