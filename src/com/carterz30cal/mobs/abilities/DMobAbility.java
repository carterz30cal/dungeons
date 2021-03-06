package com.carterz30cal.mobs.abilities;

import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;

public class DMobAbility
{
	public HashMap<DMob,int[]> mobs;
	
	public DMobAbility(FileConfiguration data, String path)
	{
		mobs = new HashMap<DMob,int[]>();
	}
	
	public void tick(DMob mob)
	{
		
	}
	
	public void add(DMob mob)
	{
		
	}
	public int damaged(DMob mob,DungeonsPlayer player,int damage)
	{
		return damage;
	}
	public void killed(DMob mob)
	{
		
	}
	public void trigger(Player player)
	{
		
	}
}
