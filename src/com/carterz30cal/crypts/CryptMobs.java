package com.carterz30cal.crypts;

import java.util.HashMap;

public class CryptMobs
{
	public HashMap<CryptRoomType,String[]> mobs;
	public String[] bosses;
	
	public CryptMobs()
	{
		mobs = new HashMap<CryptRoomType,String[]>();
	}
	@SuppressWarnings("unchecked")
	public CryptMobs(CryptMobs copy)
	{
		mobs = (HashMap<CryptRoomType, String[]>) copy.mobs.clone();
		bosses = copy.bosses;
	}
}
