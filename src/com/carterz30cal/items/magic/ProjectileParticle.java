package com.carterz30cal.items.magic;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.DamageType;
import com.carterz30cal.player.DungeonsPlayer;

public class ProjectileParticle extends BukkitRunnable
{
	public Location current;
	public Vector direction;
	
	public DungeonsPlayer owner;
	public int damage;
	public int pierces;
	public Color colour;
	public double speed;
	
	public AbsAbility mod;
	
	private ArrayList<DMob> hit;
	public ProjectileParticle(DungeonsPlayer o,ItemSpell spell,AbsAbility wand)
	{

		owner = o;
		current = owner.player.getEyeLocation();
		direction = owner.player.getEyeLocation().getDirection().normalize();
		
		damage = spell.damage;
		pierces = spell.pierces;
		speed = spell.speed;
		colour = spell.colour;
		mod = wand;
		
		for (AbsAbility a : owner.stats.abilities) a.onMagic(owner, this);
		if (mod != null) mod.onMagic(owner, this);
		
		
		direction = direction.multiply(speed);
		runTaskTimer(Dungeons.instance,0,1);
		
		hit = new ArrayList<DMob>();
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		current = current.add(direction);
		Dungeons.w.spawnParticle(Particle.REDSTONE,current, 0,1, 0, 0, 0,new Particle.DustOptions(colour, 1));
		check();
	}
	
	public void check()
	{
		if (owner.player.getLocation().distance(current) > 30) cancel();
		if (!Dungeons.w.getBlockAt(current).isPassable()) cancel();
		Collection<Entity> entities = Dungeons.w.getNearbyEntities(current, 0.5, 0.5, 0.5);
		for (Entity e : entities)
		{
			DMob m = DMobManager.get(e);
			if (m != null && !hit.contains(m))
			{
				m.damage(damage,owner,DamageType.MAGIC);
				pierces--;
				for (AbsAbility a : owner.stats.abilities) a.onMagicHit(owner,m);
				if (mod != null) mod.onMagicHit(owner,m);
				if (pierces < 0) cancel();
				hit.add(m);
				return;
			}
		}
	}
}
