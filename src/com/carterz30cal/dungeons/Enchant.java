package com.carterz30cal.dungeons;

import java.util.HashMap;



public class Enchant
{
	public String name;
	
	public HashMap<String,Double> effects;
	public int maxLevel;
	
	public double get(String effect,double d)
	{
		return (effects.getOrDefault(effect, (double) 0)*d);
	}
	
	public Enchant(String n,int m,HashMap<String,Double> e)
	{
		name = n;
		effects = e;
		maxLevel = m;
	}

}
