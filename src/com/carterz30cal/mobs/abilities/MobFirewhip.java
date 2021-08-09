package com.carterz30cal.mobs.abilities; 

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.utility.ParticleFunctions;

import net.minecraft.server.v1_16_R3.PacketPlayOutEntityStatus;

public class MobFirewhip extends DMobAbility
{
	public HashMap<DMob,Integer> cooldowns;
	
	public int cooldown;
	public int damage;
	public int range;
	public int minrange;
	public boolean targetOnly;
	public boolean soul;
	
	public MobFirewhip(FileConfiguration data, String path)
	{
		super(data, path);
		
		cooldowns = new HashMap<DMob,Integer>();
		damage = data.getInt(path + ".damage", 0);
		range = data.getInt(path + ".range", 0);
		minrange = data.getInt(path + ".minrange", 0);
		
		cooldown = data.getInt(path + ".cooldown", 0);
		targetOnly = data.getBoolean(path + ".targetonly",false);
		soul = data.getBoolean(path + ".soul",false);
	}
	
	public void tick(DMob mob)
	{
		int cd = cooldowns.getOrDefault(mob, 0);
		if (cd == 0)
		{
			cooldowns.put(mob, cooldown);
			for (Player p : Bukkit.getOnlinePlayers())
			{
				if (targetOnly && ((Mob)mob.entities.get(0)).getTarget() != p) continue;
				if (p.getLocation().distance(mob.entities.get(0).getLocation()) > range
						|| p.getLocation().distance(mob.entities.get(0).getLocation()) < minrange) continue;
				
				p.playSound(mob.entities.get(0).getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.7f, 1f);
				new Firewhip(mob.entities.get(0).getLocation().add(0,1,0),p.getEyeLocation(),p,damage,soul);
			}
		}
		else cooldowns.put(mob, cd-1);
	}

}

class Firewhip extends BukkitRunnable
{
	public Location target;
	public Location current;
	public Player p;
	public Vector direction;
	public int dm;
	public boolean s;
	
	public Firewhip(Location start,Location end,Player e,int dmg,boolean soul)
	{
		target = end;
		current = start;
		p = e;
		direction = end.toVector().subtract(start.toVector()).normalize().multiply(1.3);
		dm = dmg;
		s = soul;
		
		runTaskTimer(Dungeons.instance,1,1);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		current.add(direction);
		
		if (s) ParticleFunctions.stationary(current, Particle.SOUL_FIRE_FLAME, 1);
		else ParticleFunctions.stationary(current, Particle.FLAME, 1);
		if (current.distance(target) < 1)
		{
			direction = direction.rotateAroundY(90).multiply(0.05);
			for (int i = 0; i < 20;i++)
			{
				current.add(direction);
				if (s) ParticleFunctions.stationary(current, Particle.SOUL_FIRE_FLAME, 1);
				else ParticleFunctions.stationary(current, Particle.FLAME, 1);
			}
			DungeonsPlayer d = DungeonsPlayerManager.i.get(p);
			d.damage(dm, false);

			CraftPlayer c = (CraftPlayer)p;
			PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus (c.getHandle(),(byte)2);
			for (Player k : Bukkit.getOnlinePlayers())
			{
				c = (CraftPlayer)k;
				c.getHandle().playerConnection.sendPacket(packet);
			}
			cancel();
		}
	}
	
}
