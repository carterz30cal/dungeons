package com.carterz30cal.items;

import java.util.HashMap;

import org.bukkit.Material;

public class ItemEngine extends Item
{
	public int capacity;
	
	
	public ItemEngine()
	{
		attributes = new HashMap<String,Double>();
		data = new HashMap<String,Object>();
	}
}
