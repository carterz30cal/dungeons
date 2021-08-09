package com.carterz30cal.player;

import java.util.HashMap;
import java.util.Map.Entry;

public class DungeonsPlayerStatBank
{
	public DungeonsPlayer d;
	
	public int health;
	public int mana;
	public int armour;
	public int regen;
	
	public int damage;
	public int sweep;
	public double damagemod;
	public int attackspeed;
	
	public int killcoins;
	
	public int fortune;
	public double xpbonus;
	
	public int miningspeed;
	
	public int luck;
	public int fishingspeed;
	
	public HashMap<String,Double> base;
	
	public DungeonsPlayerStatBank()
	{
		base = new HashMap<String,Double>();
	}
	public void add(HashMap<String,Double> attr)
	{
		health    += attr.getOrDefault("health", 0d).intValue();
		mana      += attr.getOrDefault("mana", 0d).intValue();
		armour    += attr.getOrDefault("armour", 0d).intValue();
		regen     += attr.getOrDefault("regen", 0d).intValue();
		damage    += attr.getOrDefault("damage", 0d).intValue();
		sweep     += attr.getOrDefault("sweep", 0d).intValue();
		attackspeed += attr.getOrDefault("attackspeed", 0d).intValue();
		damagemod += attr.getOrDefault("damagep", 0d);
		killcoins += attr.getOrDefault("killcoins", 0d).intValue();
		fortune += attr.getOrDefault("fortune", 0d).intValue();
		xpbonus   += attr.getOrDefault("bonusxp", 0d);
		
		miningspeed += attr.getOrDefault("miningspeed", 0d).intValue();
		
		luck += attr.getOrDefault("luck", 0d).intValue();
		fishingspeed += attr.getOrDefault("fishingspeed", 0d).intValue();
		
		for (Entry<String,Double> a : attr.entrySet())
		{
			if (base.containsKey(a.getKey()))
			{
				double pv = base.get(a.getKey());
				base.put(a.getKey(), a.getValue()+pv);
			}
			else base.put(a.getKey(), a.getValue());
			
		}
	}
}
