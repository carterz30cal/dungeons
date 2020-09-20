package com.carterz30cal.items;

import java.util.HashMap;

public class ItemSet
{
	// set
	public String set_lore;
	public HashMap<String,Double> set_attributes;
	// synergy
	public String syn_lore;
	public HashMap<String,Double> syn_attributes;
	
	public ItemSet()
	{
		set_attributes = new HashMap<String,Double>();
		syn_attributes = new HashMap<String,Double>();
	}
}