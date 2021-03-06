package com.carterz30cal.items;

import java.util.HashMap;

import org.bukkit.Material;

public class Item
{
	public String type;
	public String name;
	public String[] description;
	public int id;
	
	
	public ItemSet set;
	public Material material;
	public Rarity rarity;
	public boolean glow;
	public boolean noDesc;
	public HashMap<String,Double> attributes; // this stores item stats
	public HashMap<String,Object> data; // stores type specific data (e.g. leather armour colour)
	public int combatReq;
	
	public boolean nolore;
	
	
	public Item()
	{
		attributes = new HashMap<String,Double>();
		data = new HashMap<String,Object>();
	}
}
