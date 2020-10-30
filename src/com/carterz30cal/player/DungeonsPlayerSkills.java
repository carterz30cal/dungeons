package com.carterz30cal.player;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.utility.StringManipulator;

public class DungeonsPlayerSkills
{
	public static int levelCap = 25;
	private static final int levelDifficulty = 60;
	
	private HashMap<String,Integer> skills;
	public DungeonsPlayer d;
	public DungeonsPlayerSkills(Player p)
	{
		skills = new HashMap<String,Integer>();
		String path = p.getUniqueId() + ".skills";
		for (String skill : Dungeons.instance.getPlayerConfig().getConfigurationSection(path).getKeys(false))
		{
			skills.put(skill, Dungeons.instance.getPlayerConfig().getInt(path + "." + skill));
		}
	}
	public int getSkillLevel(String skill)
	{
		int points = skills.getOrDefault(skill.toLowerCase(), 0);
		int level = 0;
		while (true)
		{
			points -= levelRequirement(level);
			if (points > 0) level++;
			else break;
		}
		return Math.min(levelCap, level);
	}
	public int getSkill(String skill)
	{
		return skills.getOrDefault(skill.toLowerCase(), 0);
	}
	public Set<String> getSkills()
	{
		return skills.keySet();
	}
	// Returns true if skill levelled up
	public boolean add(String skill,int points)
	{
		points = (int)Math.round(points * d.stats.miningXp);
		int total = skills.getOrDefault(skill,0)+points;
		int level = getSkillLevel(skill);
		skills.put(skill, total);
		if (getSkillLevel(skill) > level) 
		{
			return true;
		}
		else return false;
	}
	
	public void sendLevelMessage(String skill,Player p)
	{
		int level = getSkillLevel(skill);
		if (level == levelCap)
		{
			p.sendMessage(StringManipulator.rainbow(skill.toUpperCase() + " MAXED"));
		}
		else 
		{
			p.sendMessage(ChatColor.GREEN + "You are now " + ChatColor.GOLD +  StringManipulator.capitalise(skill) + 
					ChatColor.GREEN + " " + getSkillLevel(skill));
		}
	}
	
	
	private static int levelRequirement(int level) 
	{
		return (int)Math.pow((level+1)*levelDifficulty,1.5);
	}
	
	public static int getLevelRequirement(int level)
	{
		int points = 0;
		if (level == levelCap) return Integer.MAX_VALUE;
		for (int l = 0; l <= level;l++)
		{
			points += levelRequirement(l);
		}
		return points;
	}
}
