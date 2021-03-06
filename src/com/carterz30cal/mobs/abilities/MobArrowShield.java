package com.carterz30cal.mobs.abilities;

import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.utility.RandomFunctions;

import net.md_5.bungee.api.ChatColor;

public class MobArrowShield extends DMobAbility
{
	public int range;
	public int healing;
	public int healvariation;
	
	public int cooldown;
	private int ticker;
	
	public MobArrowShield(FileConfiguration data, String path)
	{
		super(data, path);
		
		range = data.getInt(path + ".range", 3);
		healing = data.getInt(path + ".healing", 0);
		healvariation = data.getInt(path + ".healdiff", 0);
		cooldown = data.getInt(path + ".cooldown", 20);
	}
	
	@Override
	public void tick(DMob mob)
	{
		ticker++;
		if (ticker < cooldown) return;
		ticker = 0;
		
		for (Entity e : Dungeons.w.getNearbyEntities(mob.entities.get(0).getLocation(), range, range, range))
		{
			if (e.getType() != EntityType.ARROW) continue;
			
			
			LivingEntity shooter = (LivingEntity)((AbstractArrow)e).getShooter();
			if (shooter.getType() == EntityType.PLAYER)
			{
				e.remove();
				Dungeons.w.spawnParticle(Particle.SOUL, e.getLocation(), 3,0.1,0.1,0.1,0);
				int heal = RandomFunctions.random(healing-healvariation, healing+healvariation);
				mob.heal(heal);
				if (heal > 0)
				{
					((Player)shooter).sendMessage(ChatColor.RED + mob.type.name + "'s arrow shield absorbed your arrow and healed for " + heal + " health");
				}
			}
		}
	}

}
