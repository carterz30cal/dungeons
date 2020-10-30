package com.carterz30cal.mobs;

import org.bukkit.Location;

import com.carterz30cal.dungeons.Dungeons;

public class SpawnPosition
{
	public Location position;
	public DungeonMob mob;
	
	public SpawnPosition(Location pos)
	{
		position = pos;
		mob = null;
	}
	
	public SpawnPosition (int x, int y, int z)
	{
		position = new Location(Dungeons.w,x,y,z);
	}
	public SpawnPosition (double x, double y, double z)
	{
		position = new Location(Dungeons.w,x,y,z);
	}
}
