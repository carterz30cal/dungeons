package com.carterz30cal.mobs.abilities;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Mob;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;

public class MobLauncher extends DMobAbility
{
	public static Map<DMob,Integer> cooldowns = new HashMap<>();
	
	public int volley;
	public int cooldown;
	public int spread;
	public MobLauncher(FileConfiguration data, String path) {
		super(data, path);
		
		volley = data.getInt(path + ".volley", 1);
		cooldown = data.getInt(path + ".cooldown", 20);
		spread = data.getInt(path + ".spread", 12);
	}
	
	public void tick(DMob mob)
	{
		int cd = cooldowns.getOrDefault(mob, cooldown);
		Mob m = (Mob)mob.entities.get(0);
		if (cd == 0 && m.getTarget() != null)
		{
			Vector direction = m.getTarget().getEyeLocation().add(0,0.6,0).toVector().subtract(m.getEyeLocation().toVector()).normalize();
			for (int v = 0; v < volley;v++) 
			{
				new BukkitRunnable()
				{
					@Override
					public void run() {
						Arrow a = Dungeons.w.spawnArrow(m.getEyeLocation(), direction, 1.3f, spread);
						a.setShooter(m);
						a.getPersistentDataContainer().set(DMobManager.arrowDamage, PersistentDataType.INTEGER, mob.type.damage);
					}
					
				}.runTaskLater(Dungeons.instance,v*2);
			}
			cooldowns.put(mob, cooldown);
		}
		else if (cd > 0) cooldowns.put(mob, --cd);
	}

}
