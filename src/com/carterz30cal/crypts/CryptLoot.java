package com.carterz30cal.crypts;

public class CryptLoot
{
	public String item;
	public String enchants;
	public int rarity;
	public int[] quantity;
	
	public CryptLoot(String i,int r)
	{
		item = i;
		rarity = r;
		quantity = new int[]{1,1};
	}
	public CryptLoot(String i,String e,int r)
	{
		item = i;
		enchants = e;
		rarity = r;
		quantity = new int[]{1,1};
	}
	public CryptLoot(String i,int r,int q)
	{
		item = i;
		rarity = r;
		quantity = new int[]{q,q};
	}
	public CryptLoot(String i,int r,int q1,int q2)
	{
		item = i;
		rarity = r;
		quantity = new int[]{q1,q2};
	}
}
