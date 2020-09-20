package com.carterz30cal.player;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.entity.Player;

import com.carterz30cal.dungeons.Dungeons;

public class DungeonsPlayerPerks
{
	public HashMap<String,Integer> perks;
	
	public static int levelDifficulty = 200;
	public DungeonsPlayerPerks (Player p)
	{
		String path = p.getUniqueId() + ".perks";
		perks = new HashMap<String,Integer>();
		
		for (String perkKey : Dungeons.instance.getPlayerConfig().getConfigurationSection(path).getKeys(false))
		{
			perks.put(perkKey, Dungeons.instance.getPlayerConfig().getInt(path + "." + perkKey, 0));
		}
	}
	public void remove(String perk)
	{
		perks.remove(perk);
	}
	public void add(String perk)
	{
		if (perk == null) return;
		if (perk.equals("null")) return;
		int perkAm = getKills(perk);
		perks.put(perk, perkAm+1);
	}
	public Set<String> getPerks()
	{
		return perks.keySet();
	}
	public int getKills(String perk)
	{
		return perks.getOrDefault(perk, 0);
	}
	public int getKillsForLevel(int level)
	{
		return (level-1)*levelDifficulty;
	}
	public int getLevel(String perk)
	{
		return Math.min(10, ((int) Math.floor(perks.getOrDefault(perk, 0)/(float)levelDifficulty))+1);
	}
}
