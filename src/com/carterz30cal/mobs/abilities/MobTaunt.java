package com.carterz30cal.mobs.abilities;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.carterz30cal.mobs.DMob;
import com.carterz30cal.utility.RandomFunctions;

import net.md_5.bungee.api.ChatColor;

public class MobTaunt extends DMobAbility
{
	public Map<DMob,Integer> cooldowns = new HashMap<>();
	public String[] taunts;
	public int cooldown;
	public MobTaunt(FileConfiguration data, String path) {
		super(data, path);
		
		taunts = data.getString(path + ".taunts", "").split(";");
		cooldown = data.getInt(path + ".cooldown", 200);
	}
	
	public void tick(DMob mob)
	{
		int c = cooldowns.getOrDefault(mob, 0);
		if (c == 0)
		{
			String s = RandomFunctions.get(taunts);
			for (Player p : Bukkit.getOnlinePlayers())
			{
				if (p.getLocation().distance(mob.entities.get(0).getLocation()) < 10) p.sendMessage(ChatColor.RED + "[" + mob.type.name + "]: " + ChatColor.GOLD + s);
			}
			cooldowns.put(mob, cooldown);
		}
		else cooldowns.put(mob, c-1);
	}

}
