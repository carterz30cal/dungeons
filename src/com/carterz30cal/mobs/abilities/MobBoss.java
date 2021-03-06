package com.carterz30cal.mobs.abilities;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobType;

import net.md_5.bungee.api.ChatColor;

public class MobBoss extends DMobAbility
{
	public static HashMap<DMobType,Boolean> alive;
	public static HashMap<DMob,BossBar> bars;
	
	public int range;
	public BarStyle style;
	public BarColor colour;
	public boolean unique;
	public MobBoss(FileConfiguration data, String path)
	{
		super(data, path);
		if (alive == null) 
		{
			alive = new HashMap<DMobType,Boolean>();
			bars = new HashMap<DMob,BossBar>();
		}
	
		range = data.getInt(path + ".range", 20);
		style = BarStyle.valueOf(data.getString(path + ".style", "SOLID"));
		colour = BarColor.valueOf(data.getString(path + ".colour", "RED"));
		unique = data.getBoolean(path + ".unique", true);
	}
	@Override
	public void add(DMob mob)
	{
		if (alive.getOrDefault(mob.type, false) && unique)
		{
			mob.destroy(null);
			return;
		}
		alive.put(mob.type, true);
		BossBar bar = Bukkit.createBossBar(ChatColor.GOLD + mob.type.name, colour, style, new BarFlag[0]);
		bar.setProgress(1);
		bars.put(mob, bar);
	}
	@Override
	public void killed(DMob mob)
	{
		BossBar bar = bars.get(mob);
		if (bar != null) bar.removeAll();
		
		bars.remove(mob);
		alive.put(mob.type, false);
	}
	@Override
	public void tick(DMob mob)
	{
		BossBar bar = bars.get(mob);
		if (bar == null) return;
		bar.setProgress(mob.health / (double)mob.health());
		for (Player p : Bukkit.getOnlinePlayers())
		{
			if (p.getLocation().distance(mob.entities.get(0).getLocation()) <= range)
			{
				bar.addPlayer(p);
			}
			else bar.removePlayer(p);
		}
	}
}
