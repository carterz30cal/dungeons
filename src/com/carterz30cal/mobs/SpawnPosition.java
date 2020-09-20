package com.carterz30cal.mobs;

import org.bukkit.Location;

public class SpawnPosition
{
	public Location position;
	public DungeonMob mob;
	
	public SpawnPosition(Location pos)
	{
		position = pos;
		mob = null;
	}
}
