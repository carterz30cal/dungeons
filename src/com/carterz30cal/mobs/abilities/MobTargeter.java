package com.carterz30cal.mobs.abilities;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

import com.carterz30cal.mobs.DMob;

public class MobTargeter extends DMobAbility
{
	public int range;
	
	public MobTargeter(FileConfiguration data, String path)
	{
		super(data, path);
		
		range = data.getInt(path + ".range", 10);
	}

	@Override
	public void tick(DMob mob)
	{
		if (((Mob) mob.entities.get(0)).getTarget() != null) return;
		for (Player p : Bukkit.getOnlinePlayers())
		{
			if (p.getGameMode() != GameMode.SURVIVAL) continue;
			if (p.getLocation().distance(mob.entities.get(0).getLocation()) <= range)
			{
				((Mob) mob.entities.get(0)).setTarget(p);
				break;
			}
		}
	}
}
