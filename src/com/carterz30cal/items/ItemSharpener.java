package com.carterz30cal.items;

import java.util.HashMap;

import org.bukkit.ChatColor;

public class ItemSharpener 
{
	public ChatColor plusColour;
	
	public HashMap<String,Double> attributes;
	
	public ItemSharpener()
	{
		attributes = new HashMap<String,Double>();
		plusColour = ChatColor.MAGIC;
	}
}
