package com.carterz30cal.bosses;

import java.util.HashMap;


public class BossManager
{
	public static BossManager i;
	public static HashMap<String,Class<? extends AbsBoss>> bossTypes;
	public static HashMap<String,AliveBossHandler> bosses;
	public BossManager()
	{
		i = this;
		bossTypes = new HashMap<String,Class<? extends AbsBoss>>();
		bosses = new HashMap<String,AliveBossHandler>();
		bossTypes.put("summonkey_waterway", BossWaterway.class);
	}
	
	public static boolean summon(String summon)
	{
		if (bosses.containsKey(summon)) return false;
		AbsBoss b = null;
		try 
		{
			b = bossTypes.get(summon).newInstance();
		} 
		catch (InstantiationException | IllegalAccessException e) {}
		if (b == null) return false;
		
		bosses.put(summon,b.abh);
		
		b.entry();
		return true;
	}
}
