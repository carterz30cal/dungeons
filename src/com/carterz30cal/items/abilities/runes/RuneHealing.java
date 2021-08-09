package com.carterz30cal.items.abilities.runes;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.LivingEntity;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;

public class RuneHealing extends AbsAbility 
{

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(rune + "Healing");
		d.add("Regain 0.6% of your max health every hit.");
		return d;
	}

	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		Location laser = d.player.getEyeLocation().subtract(0, 0.6, 0);
		Location l = ((LivingEntity)dMob.entities.get(0)).getEyeLocation();
		double dx = (l.getX() - laser.getX()) / 10;
		double dy = (l.getY() - laser.getY()) / 10;
		double dz = (l.getZ() - laser.getZ()) / 10;
		for (int x = 0; x < 10; x++) 
		{
			laser.add(dx, dy, dz);
			Dungeons.w.spawnParticle(Particle.REDSTONE, laser, 2, 0d, 0d, 0d, new DustOptions(Color.GREEN,0.5f));
		}
		d.heal(0.006);
		return damage;
	} 
}
