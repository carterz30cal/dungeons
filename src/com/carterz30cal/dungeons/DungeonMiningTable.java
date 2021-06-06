package com.carterz30cal.dungeons;

import java.util.HashMap;

import com.carterz30cal.player.DungeonsPlayer;

public class DungeonMiningTable
{
	public DungeonsPlayer owner;
	public HashMap<String,Integer> loot;
	public int xp;
	
	public DungeonMiningTable()
	{
		loot = new HashMap<String,Integer>();
		xp = 0;
	}
}
