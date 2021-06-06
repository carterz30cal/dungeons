package com.carterz30cal.mobs.abilities; 

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

import net.minecraft.server.v1_16_R3.PacketPlayOutEntityStatus;

public class MobLaser extends DMobAbility
{
	public HashMap<DMob,Integer> cooldowns;
	
	public int cooldown;
	public int damage;
	public int range;
	public boolean targetOnly;
	public Color colour;
	
	public MobLaser(FileConfiguration data, String path)
	{
		super(data, path);
		
		cooldowns = new HashMap<DMob,Integer>();
		damage = data.getInt(path + ".damage", 0);
		range = data.getInt(path + ".range", 0);
		cooldown = data.getInt(path + ".cooldown", 0);
		targetOnly = data.getBoolean(path + ".targetonly",false);
		String[] col = data.getString(path + ".colour","255,0,0").split(",");
		colour = Color.fromRGB(Integer.parseInt(col[0]), Integer.parseInt(col[1]), Integer.parseInt(col[2]));
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
				if (p.getLocation().distance(mob.entities.get(0).getLocation()) > range) continue;
				DungeonsPlayer d = DungeonsPlayerManager.i.get(p);
				d.damage(damage, false);
				Location laser = p.getEyeLocation().clone();

				CraftPlayer c = (CraftPlayer)p;
				PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus (c.getHandle(),(byte)2);
				for (Player k : Bukkit.getOnlinePlayers())
				{
					c = (CraftPlayer)k;
					c.getHandle().playerConnection.sendPacket(packet);
				}
				
				Location l = ((LivingEntity)mob.entities.get(0)).getEyeLocation();
				double dx = (l.getX() - p.getEyeLocation().getX()) / 50;
				double dy = (l.getY() - p.getEyeLocation().getY()) / 50;
				double dz = (l.getZ() - p.getEyeLocation().getZ()) / 50;
				for (int x = 0; x < 50; x++) 
				{
					laser.add(dx, dy, dz);
					Dungeons.w.spawnParticle(Particle.REDSTONE, laser, 1, 0d, 0d, 0d, new DustOptions(colour,0.5f));
				}
			}
		}
		else cooldowns.put(mob, cd-1);
	}

}
