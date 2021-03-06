package com.carterz30cal.utility;

import org.bukkit.Location;

import com.carterz30cal.dungeons.Dungeons;

public class Square
{
	public int c1x;
	public int c2x;
	public int c1z;
	public int c2z;
	
	public int cy;
	
	public Square (int x1,int z1,int x2,int z2,int y)
	{
		c1x = x1;
		c2x = x2;
		c1z = z1;
		c2z = z2;
		
		cy = y;
	}
	
	public Location getRandom()
	{
		return new Location(Dungeons.w, RandomFunctions.random(c1x, c2x), cy, RandomFunctions.random(c1z, c2z));
	}
}
