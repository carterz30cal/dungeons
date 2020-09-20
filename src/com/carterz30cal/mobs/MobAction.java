package com.carterz30cal.mobs;

import java.util.HashMap;

import org.bukkit.Particle;

public class MobAction
{
	public String text;
	
	public String condAttr;
	public Object condAm;
	public boolean condTog;
	
	public Particle p;
	public HashMap<String,String> patterns;
	public HashMap<String,Object> effects;
	
	public MobAction ()
	{
		patterns = new HashMap<String,String>();
		effects = new HashMap<String,Object>();
	}
}
