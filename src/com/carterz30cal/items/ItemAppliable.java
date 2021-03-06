package com.carterz30cal.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;

public class ItemAppliable extends Item
{
	public String prefix;
	public int order;
	
	public List<Material> suitable;
	public HashMap<String,Double> app_attributes;
	
	public ItemAppliable()
	{
		suitable = new ArrayList<>();
		app_attributes = new HashMap<>();
	}
}
