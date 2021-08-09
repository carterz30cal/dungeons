package com.carterz30cal.mobs.abilities; 

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

import net.minecraft.server.v1_16_R3.PacketPlayOutEntityStatus;

public class MobDamageAura extends DMobAbility
{
	public HashMap<DMob,Integer> cooldowns;
	
	public int cooldown;
	public int damage;
	public int range;
	public boolean targetOnly;
	
	public MobDamageAura(FileConfiguration data, String path)
	{
		super(data, path);
		
		cooldowns = new HashMap<DMob,Integer>();
		damage = data.getInt(path + ".damage", 0);
		range = data.getInt(path + ".range", 0);
		cooldown = data.getInt(path + ".cooldown", 0);
		targetOnly = data.getBoolean(path + ".targetonly",false);

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

				CraftPlayer c = (CraftPlayer)p;
				PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus (c.getHandle(),(byte)2);
				for (Player k : Bukkit.getOnlinePlayers())
				{
					c = (CraftPlayer)k;
					c.getHandle().playerConnection.sendPacket(packet);
				}
			}
		}
		else cooldowns.put(mob, cd-1);
	}

}
