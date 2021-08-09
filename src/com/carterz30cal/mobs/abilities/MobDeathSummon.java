package com.carterz30cal.mobs.abilities;

import org.bukkit.configuration.file.FileConfiguration;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.SpawnPosition;

public class MobDeathSummon extends DMobAbility
{
	public int count;
	public String type;
	public MobDeathSummon(FileConfiguration data, String path) {
		super(data, path);
		
		count = data.getInt(path + ".count", 1);
		type = data.getString(path + ".type", "drenched1");
	}

	public void killed(DMob mob)
	{
		if (!Dungeons.instance.isEnabled()) return;
		for (int i = 0; i < count;i++) 
		{
			DMob m = DMobManager.spawn(type, new SpawnPosition(mob.entities.get(0).getLocation()));
			m.owner = mob.owner;
			mob.owner.mob = m;
		}
		mob.owner = null;
	}
}
