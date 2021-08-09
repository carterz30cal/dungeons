package com.carterz30cal.items;

import java.util.HashMap;

import org.bukkit.Material;

public class Item
{
	public String type;
	public String name;
	public String[] description;
	public String prefix;
	public int id;
	
	
	public ItemSet set;
	public Material material;
	public Rarity rarity;
	public boolean glow;
	public boolean noDesc;
	public boolean noStack;
	public boolean noRunic; // item cannot have a rune applied
	public HashMap<String,Double> attributes; // this stores item stats
	public HashMap<String,Object> data; // stores type specific data (e.g. leather armour colour)
	public int combatReq;
	public int slots = 0; // sharpener slots. default is 0 for most items and 1 for weapons.
	public String areaReq; // area requirement
	public String area; // the <area>/<file> bit
	
	public boolean nolore;
	
	
	public Item()
	{
		attributes = new HashMap<String,Double>();
		data = new HashMap<String,Object>();
	}
}
