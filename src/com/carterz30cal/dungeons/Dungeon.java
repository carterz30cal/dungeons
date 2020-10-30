package com.carterz30cal.dungeons;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.carterz30cal.mobs.SpawnPosition;
import com.carterz30cal.npcs.NPC;


public class Dungeon
{
	public String id;
	public String name;
	public Location spawn;
	public HashMap<SpawnPosition,String> spawns;
	
	public DungeonMining mining;
	public ArrayList<Player> players;
	public ArrayList<NPC> npcs;
	public boolean activated;
	
	public String icon_data;
	public String icon_sig;
	public String expl_lore;
	public int killsperlevel;
	public boolean unfinished;
	public Dungeon()
	{
		spawns = new HashMap<SpawnPosition,String>();
		players = new ArrayList<Player>();
		npcs = new ArrayList<NPC>();
		activated = true;
		mining = new DungeonMining();
		mining.dungeon = this;
	}
}
