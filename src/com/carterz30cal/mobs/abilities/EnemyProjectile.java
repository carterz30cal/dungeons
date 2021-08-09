package com.carterz30cal.mobs.abilities;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.carterz30cal.dungeons.Dungeons;

public class EnemyProjectile extends BukkitRunnable
{
	public Location current;
	public Player target;
	
	public double speed;
	public Color colour;
	public int lifespan;
	
	private DMobAbility owner;
	private int lifetime;
	
	public boolean beautiful; // makes fast projectiles look better.
	public EnemyProjectile(Location start, Player end, double s,Color c,int lifes,DMobAbility o)
	{
		current = start;
		target = end;
		speed = s;
		lifetime = 0;
		lifespan = lifes;
		colour = c;
		owner = o;
		runTaskTimer(Dungeons.instance,1,1);
	}
	public EnemyProjectile(Location start, Player end, double s,Color c,int lifes,DMobAbility o,boolean b)
	{
		current = start;
		target = end;
		speed = s;
		lifetime = 0;
		lifespan = lifes;
		colour = c;
		owner = o;
		beautiful = b;
		runTaskTimer(Dungeons.instance,1,1);
	}
	@Override
	public void run()
	{
		lifetime++;
		if (beautiful)
		{
			for (int i = 0; i < 10;i++)
			{
				Dungeons.w.spawnParticle(Particle.REDSTONE,current, 0, 1, 0, 0, 0,new Particle.DustOptions(colour, 0.6f));
				Vector d = target.getEyeLocation().subtract(current).toVector().normalize();
				current = current.add(d.multiply(speed/100));
			}
		}
		else
		{
			Dungeons.w.spawnParticle(Particle.REDSTONE,current, 0, 1, 0, 0, 0,new Particle.DustOptions(colour, 0.8f));
			Vector d = target.getEyeLocation().subtract(current).toVector().normalize();
			current = current.add(d.multiply(speed/10));
		}
		
		if (current.distance(target.getEyeLocation()) < 0.5)
		{
			owner.trigger(target);
			cancel();
		}
		else if (lifetime == lifespan) cancel();
	}
}
