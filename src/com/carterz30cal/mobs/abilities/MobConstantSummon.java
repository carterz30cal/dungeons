package com.carterz30cal.mobs.abilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.SpawnPosition;

public class MobConstantSummon extends DMobAbility 
{
	public static Map<DMob,Integer> timer = new HashMap<>();
	public static Map<DMob,List<DMob>> removing = new HashMap<>();
	
	public String type;
	public int cooldown;
	
	public MobConstantSummon(FileConfiguration data, String path) {
		super(data, path);
		
		type = data.getString(path + ".mob", "drenched1");
		cooldown = data.getInt(path + ".cooldown", 20);
	}
	
	public void tick(DMob mob)
	{
		int t = timer.getOrDefault(mob, cooldown);
		if (t == 0)
		{
			removing.putIfAbsent(mob, new ArrayList<>());
			removing.get(mob).add(DMobManager.spawn(type, new SpawnPosition(mob.entities.get(0).getLocation())));
			timer.put(mob, cooldown);
		}
		else timer.put(mob, --t);
	}

	public void killed(DMob mob)
	{
		for (DMob m : removing.getOrDefault(mob, new ArrayList<>())) m.remove();
	}
}
