package com.carterz30cal.player;

import java.util.HashMap;
import java.util.Map;
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
	public void add(Map<String, Double> attributes)
	{
		health    += attributes.getOrDefault("health", 0d).intValue();
		mana      += attributes.getOrDefault("mana", 0d).intValue();
		armour    += attributes.getOrDefault("armour", 0d).intValue();
		regen     += attributes.getOrDefault("regen", 0d).intValue();
		damage    += attributes.getOrDefault("damage", 0d).intValue();
		sweep     += attributes.getOrDefault("sweep", 0d).intValue();
		attackspeed += attributes.getOrDefault("attackspeed", 0d).intValue();
		damagemod += attributes.getOrDefault("damagep", 0d);
		killcoins += attributes.getOrDefault("killcoins", 0d).intValue();
		fortune += attributes.getOrDefault("fortune", 0d).intValue();
		xpbonus   += attributes.getOrDefault("bonusxp", 0d);
		
		miningspeed += attributes.getOrDefault("miningspeed", 0d).intValue();
		
		luck += attributes.getOrDefault("luck", 0d).intValue();
		fishingspeed += attributes.getOrDefault("fishingspeed", 0d).intValue();
		
		for (Entry<String,Double> a : attributes.entrySet())
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
