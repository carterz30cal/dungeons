package com.carterz30cal.mobs;

import org.bukkit.ChatColor;

public class DMobModifier 
{
	public static DMobModifier base;
	
	public double health;
	public double damage;
	public double dmgresist;
	public int coins;
	
	public ChatColor text;
	public String name;
	
	public DMobModifier()
	{
		health = 1;
		damage = 1;
		dmgresist = 0;
		coins = 0;
		
		text = ChatColor.WHITE;
		name = "";
	}
}
