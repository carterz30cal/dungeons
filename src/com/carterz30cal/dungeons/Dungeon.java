package com.carterz30cal.dungeons;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.carterz30cal.mobs.DMobModifier;
import com.carterz30cal.mobs.SpawnPosition;
import com.carterz30cal.npcs.NPC;


public class Dungeon
{
	public String id;
	public String name;
	public Location spawn;
	public HashMap<SpawnPosition,String> spawns;
	public ArrayList<DMobModifier> modifiers;
	
	
	public DungeonMining mining;
	public ArrayList<Player> players;
	public ArrayList<NPC> npcs;
	public boolean activated;
	
	public String requiredtutorial;
	public String icon_data;
	public String icon_sig;
	public String expl_lore;
	public int killsperlevel;
	public int expl_coins;
	public boolean unfinished;
	
	public String[] fishingmobs;
	
	public Dungeon()
	{
		spawns = new HashMap<SpawnPosition,String>();
		modifiers = new ArrayList<DMobModifier>();
		players = new ArrayList<Player>();
		npcs = new ArrayList<NPC>();
		activated = true;
		mining = new DungeonMining();
		mining.dungeon = this;
	}
}
