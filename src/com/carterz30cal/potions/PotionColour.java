package com.carterz30cal.potions;

import java.util.List;

import org.bukkit.Color;

public class PotionColour
{
	public int r;
	public int g;
	public int b;
	
	public PotionColour (int cr,int cg,int cb)
	{
		r = cr;
		g = cg;
		b = cb;
	}
	public PotionColour (double cr,double cg,double cb)
	{
		r = (int)cr;
		g = (int)cg;
		b = (int)cb;
	}
	public Color asColour()
	{
		return Color.fromRGB(r, g, b);
	}
	public static PotionColour combine(List<PotionColour> colours)
	{
		int tr = 0;
		int tg = 0;
		int tb = 0;
		
		double t = (double)colours.size();
		for (PotionColour colour : colours)
		{
			tr += colour.r;
			tg += colour.g;
			tb += colour.b;
		}
		return new PotionColour(tr / t,tg / t,tb / t);
	}
}
