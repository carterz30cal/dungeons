package com.carterz30cal.player.skilltree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStats;

import org.bukkit.ChatColor;

public abstract class AbsSkill
{
	public static Map<String,AbsSkill> skills = new HashMap<>();
	public AbsSkill()
	{
		skills.put(id(), this);
	}
	
	public static void init()
	{
		new SkillXpBoost();
		new SkillKnight();
		new SkillGolem();
		new SkillVitality();
		new SkillWealth();
		new SkillCharismatic();
		new SkillHeavy();
		new SkillTraining();
		new SkillEfficientMiner();
		new SkillFortune();
		new SkillBloodlust();
		new SkillFocus();
		new SkillHealing();
		new SkillWarrior();
		new SkillBlessing();
		new SkillIronskin();
	}
	
	public List<String> template(DungeonsPlayer player,int level)
	{
		List<String> d = new ArrayList<>();
		d.add(level(level));
		d.add("");
		d.add(description(level));
		if (!player.skills.containsKey(skillreq()) && !skillreq().equals("none")) 
		{
			d.add("");
			d.add(ChatColor.RED + "Unlock previous skill first!");
		}
		else if (player.level.level < levelreq()) 
		{
			d.add("");
			d.add(ChatColor.RED + "Requires level " + levelreq());
		}
		return d;
	}
	
	
	public abstract String id();
	public abstract String name();
	protected abstract String description(int level);
	public abstract String skillreq();
	public int levelreq() {return 0;}
	
	public abstract Position position();
	
	public abstract int max();
	
	// various hooks
	public void stats(int level,DungeonsPlayerStats bank) {}
	public int onAttack(int level,DungeonsPlayer player,int damage) {return damage;}
	
	
	protected static String locked()
	{
		return ChatColor.RED + "Skill is locked!";
	}
	protected String level(int level)
	{
		return ChatColor.DARK_GRAY + "Level " + level + "/" + max();
	}

}
