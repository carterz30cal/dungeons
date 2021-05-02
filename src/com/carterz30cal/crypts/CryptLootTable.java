package com.carterz30cal.crypts;

import java.util.ArrayList;

public class CryptLootTable
{
	public int[] itemsPerChest;
	public ArrayList<CryptLoot> loot;
	
	public int randomizer; // all weights combined
	public CryptLootTable()
	{
		itemsPerChest = new int[] {4,9};
		loot = new ArrayList<CryptLoot>();
	}
	
	public void init()
	{
		for (CryptLoot l : loot) randomizer += l.rarity;
	}
	
	public CryptLoot get(int pick)
	{
		int st = 0;
		
		for (CryptLoot l : loot) 
		{
			if (st+l.rarity >= pick) return l;
			else st += l.rarity;
		}
		return null;
	}
	
	public void add(String item,int weight)
	{
		loot.add(new CryptLoot(item,weight));
	}
	public void add(String item,int weight,String enchants)
	{
		loot.add(new CryptLoot(item,enchants,weight));
	}
}
