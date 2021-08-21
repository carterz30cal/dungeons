package com.carterz30cal.mobs.abilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;

import net.md_5.bungee.api.ChatColor;

public class MobOverkill extends DMobAbility
{
	public Map<DMob,Map<DungeonsPlayer,Integer>> overkills = new HashMap<>();
	
	public int threshold;
	public int reward;
	
	public MobOverkill(FileConfiguration data, String path)
	{
		super(data, path);
		
		threshold = data.getInt(path + ".threshold", 100);
		reward = data.getInt(path + ".reward", 0);
	}

	@Override
	public int damaged(DMob mob,DungeonsPlayer player,int damage)
	{
		int modthres = (int) (threshold / player.stats.overkiller);
		double dmod = (1-mob.type.dmgresist);
		
		damage = (int) (damage * dmod);
		if (damage >= modthres && player.player.getGameMode() == GameMode.SURVIVAL)
		{
			if (reward > 0) 
			{
				int rew = reward;
				for (AbsAbility a : player.stats.abilities) rew += a.onOverkill(player, mob, rew);
				overkills.putIfAbsent(mob, new HashMap<>());
				overkills.get(mob).put(player,overkills.get(mob).getOrDefault(player,0) + rew);
			}
			return (int) (modthres / dmod);
		}
		return (int) (damage / dmod);
	}
	
	@Override
	public void killed(DMob mob)
	{
		if (overkills.containsKey(mob))
		{
			for (Entry<DungeonsPlayer,Integer> d : overkills.get(mob).entrySet())
			{
				d.getKey().player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Overkill! " + ChatColor.RESET + "" + ChatColor.GOLD + "+" + d.getValue() + " coins!");
				d.getKey().coins += d.getValue();
			}
		}
	}
}
