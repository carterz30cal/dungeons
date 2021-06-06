package com.carterz30cal.crypts;

import org.bukkit.Material;

public class CryptBlocks
{
	public Material[] floor;
	public Material[] roof;
	public Material[] roomroof;
	public Material[] walls;
	
	public Material path;
	public Material support;
	
	public CryptBlocks()
	{
		
	}
	public CryptBlocks(CryptBlocks copy)
	{
		this.floor = copy.floor;
		this.roof = copy.roof;
		this.roomroof = copy.roomroof;
		this.walls = copy.walls;
		this.path = copy.path;
		this.support = copy.support;
	}
}
