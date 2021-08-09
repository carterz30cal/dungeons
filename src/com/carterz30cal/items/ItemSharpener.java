package com.carterz30cal.items;

import java.util.HashMap;

import org.bukkit.ChatColor;

public class ItemSharpener 
{
	public String plus;
	public ChatColor plusColour;
	
	public HashMap<String,Double> attributes;
	
	public String id;
	
	public ItemSharpener()
	{
		attributes = new HashMap<String,Double>();
		plus = "+";
		plusColour = ChatColor.MAGIC;
	}
}
