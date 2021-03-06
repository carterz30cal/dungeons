package com.carterz30cal.utility;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

public class BoundingBox
{
	public Location first;
	public Location second;
	
	public BoundingBox(Location f,Location s)
	{
		first = f;
		second = s;
	}
	
	public boolean isInside(Location i)
	{
		int xs = Math.min(first.getBlockX(), second.getBlockX());
		int xl = Math.max(first.getBlockX(), second.getBlockX());
		int ys = Math.min(first.getBlockY(), second.getBlockY());
		int yl = Math.max(first.getBlockY(), second.getBlockY());
		int zs = Math.min(first.getBlockZ(), second.getBlockZ());
		int zl = Math.max(first.getBlockZ(), second.getBlockZ());
		
		int x = i.getBlockX();
		int y = i.getBlockY();
		int z = i.getBlockZ();
		if (x >= xs && x <= xl && y >= ys && y <= yl && z >= zs && z <= zl) return true;
		else return false;
	}
	public boolean isInside(int x,int z)
	{
		int xs = Math.min(first.getBlockX(), second.getBlockX());
		int xl = Math.max(first.getBlockX(), second.getBlockX());
		int zs = Math.min(first.getBlockZ(), second.getBlockZ());
		int zl = Math.max(first.getBlockZ(), second.getBlockZ());
		
		if (x >= xs && x <= xl && z >= zs && z <= zl) return true;
		else return false;
	}
	
	public ArrayList<DungeonsPlayer> getWithin()
	{
		ArrayList<DungeonsPlayer> within = new ArrayList<DungeonsPlayer>();
		for (Player p : Bukkit.getOnlinePlayers())
		{
			if (isInside(p.getLocation())) within.add(DungeonsPlayerManager.i.get(p));
		}
		return within;
	}
}
