package com.carterz30cal.mobs.abilities;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

import com.carterz30cal.mobs.DMob;

public class MobRegen extends DMobAbility
{
	public int amount;
	public int cooldown;
	
	private Map<DMob,Integer> tickers = new HashMap<>();
	public MobRegen(FileConfiguration data, String path)
	{
		super(data, path);
	
		amount = data.getInt(path + ".regen", 0);
		cooldown = data.getInt(path + ".cooldown", 20);
	}

	@Override
	public void tick(DMob mob)
	{
		int ticker = tickers.getOrDefault(mob, 0);
		if (ticker < cooldown)
		{
			tickers.put(mob, ++ticker);
		}
		else
		{
			tickers.put(mob, 0);
			mob.heal(amount);
		}
	}
}
