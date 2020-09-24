package com.carterz30cal.dungeons;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.carterz30cal.mobs.SpawnPosition;


public class Dungeon
{
	public String name;
	public Location spawn;
	public HashMap<SpawnPosition,String> spawns;
	
	public DungeonMining mining;
	public ArrayList<Player> players;
	public boolean activated;
	
	public Dungeon()
	{
		spawns = new HashMap<SpawnPosition,String>();
		players = new ArrayList<Player>();
		activated = true;
		mining = new DungeonMining();
		mining.dungeon = this;
	}
}
