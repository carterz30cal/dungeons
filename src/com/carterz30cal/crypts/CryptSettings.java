package com.carterz30cal.crypts;

public class CryptSettings
{
	public int z;
	public int y;
	public int sizex;
	public int sizez;
	
	public CryptLootTable loot;
	public CryptMobs mobs;
	public CryptBlocks blocks;
	
	public int room_lb;
	public int room_ub;
	public int room_amount;
	public int room_lootfreq; // how often do we have 'hard' rooms that contain more loot?
	
	public int deadends; // how much to loop through the pathways until we give up removing dead ends.#
	
	public boolean canHaveRune = true;
	
	
	public void init(CryptLootTable l,CryptMobs m,CryptBlocks b)
	{
		loot = l;
		loot.init();
		mobs = m;
		blocks = b;
	}
	
	public void size(int x,int z)
	{
		sizex = x;
		sizez = z;
	}
	
	public void position(int pz,int py)
	{
		z = pz;
		y = py;
	}
	
	public void room(int amount,int small,int large,int loot)
	{
		room_amount = amount;
		room_lb = small;
		room_ub = large;
		room_lootfreq = loot;
	}
	
}
