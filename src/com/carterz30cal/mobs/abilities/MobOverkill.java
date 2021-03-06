package com.carterz30cal.mobs.abilities;

import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;

import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;

import net.md_5.bungee.api.ChatColor;

public class MobOverkill extends DMobAbility
{
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
			if (reward > 0) player.player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Overkill! " + ChatColor.RESET + "" + ChatColor.GOLD + "+" + reward + " coins!");
			player.coins += reward;
			return (int) (modthres / dmod);
		}
		return (int) (damage / dmod);
	}
}
