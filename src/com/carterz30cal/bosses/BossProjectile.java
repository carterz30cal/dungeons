package com.carterz30cal.bosses;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;

public class BossProjectile extends BukkitRunnable
{
	public AbsBoss boss;
	public Location current;
	public Player target;
	
	public double speed;
	public double spread;
	public Particle particle;
	
	public BossProjectile(AbsBoss owner, Location start, Player end, double s, Particle visual)
	{
		boss = owner;
		current = start;
		target = end;
		speed = s;
		particle = visual;
		spread = 0.25;
		
		runTaskTimer(Dungeons.instance,1,1);
	}
	@Override
	public void run()
	{
		Dungeons.w.spawnParticle(particle,current,2,spread,spread,spread,0);
		Location d = target.getEyeLocation().subtract(current);
		current = current.add(d.multiply(speed/100));
		if (current.distance(target.getEyeLocation()) < 1)
		{
			boss.projectileHit(this, target);
			cancel();
		}
	}
}
