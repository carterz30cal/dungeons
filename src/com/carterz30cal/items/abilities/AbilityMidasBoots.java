package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilityMidasBoots extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Midas' Gift");
		d.add("Create a trail of gold!");
		return d;
	}

	public void onTick  (DungeonsPlayer d) 
	{
		Location particles = d.player.getLocation();
		particles.subtract(particles.getDirection().multiply(0.5).setY(0));
		Dungeons.w.spawnParticle(Particle.REDSTONE,particles, 6, 0.1, 0.05, 0.1,new Particle.DustOptions(Color.YELLOW, 0.6f));
	}
}
