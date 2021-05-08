package com.carterz30cal.player;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.carterz30cal.dungeons.DungeonManager;
import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.utility.StringManipulator;

import net.md_5.bungee.api.ChatColor;

public class DungeonsPlayerExplorer 
{
	public HashMap<String,Integer> areaPoints;
	public Player player;
	
	public DungeonsPlayerExplorer(Player p)
	{
		areaPoints = new HashMap<String,Integer>();
		player = p;
		
		if (!Dungeons.instance.getPlayerConfig().contains(p.getUniqueId() + ".explorer")) return;
		for (String d : Dungeons.instance.getPlayerConfig().getConfigurationSection(p.getUniqueId() + ".explorer").getKeys(false))
		{
			areaPoints.put(d,Dungeons.instance.getPlayerConfig().getInt(p.getUniqueId() + ".explorer." + d));
		}
	}
	
	public int areas()
	{
		return areaPoints.size();
	}
	
	public int getAreaLevel(String area)
	{
		int points = areaPoints.getOrDefault(area,0);
		return Math.min(20, points / DungeonManager.i.warps.getOrDefault(area,DungeonManager.i.hub).killsperlevel);
	}
	public void add(String area, int amount)
	{
		int level = getAreaLevel(area);
		areaPoints.put(area, areaPoints.getOrDefault(area, 0)+amount);
		if (getAreaLevel(area) > level)
		{
			player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "EXPLORER LEVEL UP");
			player.sendMessage(ChatColor.AQUA + Integer.toString(DungeonManager.i.warps.get(area).killsperlevel*getAreaLevel(area)) + " kills obtained in "
					+ StringManipulator.capitalise(area) + "!");
		}
	}
	public int getKillsForNext(String area)
	{
		int kpl = DungeonManager.i.warps.get(area).killsperlevel;
		int points = areaPoints.getOrDefault(area,0);
		
		if (getAreaLevel(area) == 20) return -1;
		else return kpl - (points%kpl);
	}
	
	
	public int bonusCoins(String area)
	{
		int level = getAreaLevel(area);

		return Math.floorDiv(level, 10) * DungeonManager.i.warps.getOrDefault(area,DungeonManager.i.hub).expl_coins;
	}
	public double bonusXp(String area)
	{
		int al = getAreaLevel(area);
		return 0.01*al;
	}
}
