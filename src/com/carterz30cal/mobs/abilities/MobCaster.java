package com.carterz30cal.mobs.abilities;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

public class MobCaster extends DMobAbility
{
	public int projectiles;
	public int range;
	public double speed;
	public int damage;
	public int cooldown;
	public int lifespan;
	
	public Color colour;
	
	/*
	 * data
	 * 0 = projectiles remaining
	 * 1 = cooldown
	 * 
	 * 
	 */
	public MobCaster(FileConfiguration data, String path)
	{
		super(data, path);
		
		projectiles = data.getInt(path + ".projectiles", 1);
		speed = data.getDouble(path + ".speed", 1);
		range = data.getInt(path + ".range",5);
		damage = data.getInt(path + ".damage",0);
		cooldown = data.getInt(path + ".cooldown",40);
		lifespan = data.getInt(path + ".lifespan",100);
		
		colour = data.getColor(path + ".colour",Color.WHITE);
	}
	@Override
	public void tick(DMob mob)
	{
		if (!mobs.containsKey(mob)) return;
		int[] data = mobs.get(mob);
		if (data[0] == 0) return; // check the mob has projectiles left // bypass is inital set to -1
		
		data[1] = data[1] + 1;
		if (data[1] < cooldown) return;
		for (Player p : Bukkit.getOnlinePlayers())
		{
			if (p.getLocation().distance(mob.entities.get(0).getLocation()) <= range)
			{
				new EnemyProjectile(mob.entities.get(0).getLocation().add(0, 1, 0),p,speed,colour,lifespan,this,true);
			}
		}
		data[0] = data[0] - 1;
		data[1] = 0;
		mobs.put(mob, data);
	}
	@Override
	public void add(DMob mob)
	{
		mobs.put(mob, new int[] {projectiles,0});
	}
	@Override
	public void trigger(Player player)
	{
		DungeonsPlayer d = DungeonsPlayerManager.i.get(player);
		
		player.damage(1);
		d.damage(damage, false);
	}
}
