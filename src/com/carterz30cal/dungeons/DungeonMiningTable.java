package com.carterz30cal.dungeons;

import java.util.HashMap;

public class DungeonMiningTable
{
	public HashMap<String,Integer> loot;
	public int xp;
	
	public DungeonMiningTable()
	{
		loot = new HashMap<String,Integer>();
		xp = 0;
	}
}
