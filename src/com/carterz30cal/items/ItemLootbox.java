package com.carterz30cal.items;

import java.util.ArrayList;

public class ItemLootbox extends Item
{
	public ArrayList<String> items;
	public ArrayList<Integer[]> amounts;
	public ArrayList<Integer> chance;
	
	public ItemLootbox()
	{
		super();
		
		items = new ArrayList<String>();
		amounts = new ArrayList<Integer[]>();
		chance = new ArrayList<Integer>();
	}
}
