package com.carterz30cal.mobs.abilities;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.SpawnPosition;

public class MobSummons extends DMobAbility
{
	public static HashMap<DMob,ArrayList<DMob>> summons;
	
	public String summon;
	public Location spawn;
	public int count;
	
	public int respawn;
	
	public MobSummons(FileConfiguration data, String path)
	{
		super(data, path);
		
		if (summons == null) summons = new HashMap<DMob,ArrayList<DMob>>();
		
		summon = data.getString(path + ".summon");
		count = data.getInt(path + ".count", 1);
		
		respawn = data.getInt(path + ".respawn", -1);
		
		if (data.contains(path + ".x"))
		{
			spawn = new Location(Dungeons.w,
					data.getDouble(path + ".x"),
					data.getDouble(path + ".y"),
					data.getDouble(path + ".z"));
		}
		else spawn = null;
		
	}
	
	@Override
	public void tick(DMob mob)
	{
		if (respawn == -1) return;
		ArrayList<DMob> sum = summons.get(mob);
		if (sum == null) return;
		
		boolean alive = false;
		for (DMob m : sum)
		{
			if (m.health > 0) alive = true;
		}
		if (alive) return;
		
		sum.clear();
		summons.put(mob, null);
		Dungeons.instance.getServer().getScheduler().scheduleSyncDelayedTask(Dungeons.instance,
				new Runnable()
				{
					@Override
					public void run()
					{
						add(mob);
					}
				},respawn);
	}
	@Override
	public void add(DMob mob)
	{
		if (mob.health < 1) return;
		ArrayList<DMob> sum = new ArrayList<DMob>();
		for (int c = 0; c < count; c++)
		{
			if (spawn != null) sum.add(DMobManager.spawn(summon, new SpawnPosition(spawn)));
			else sum.add(DMobManager.spawn(summon, new SpawnPosition(((LivingEntity)mob.entities.get(0)).getEyeLocation())));
		}
		summons.put(mob, sum);
	}
}
