package com.carterz30cal.items.abilities.runes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.RandomFunctions;

public class RuneSnake extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(rune + "Snake");
		d.add("Deals 5% more damage per enemy");
		d.add("within 12 blocks. Capped at +175%.");
		return d;
	}
	
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		
		int p = 12;
		while (p > 0)
		{
			Dungeons.w.spawnParticle(Particle.REDSTONE, dMob.entities.get(0).getLocation().add(RandomFunctions.random(-0.3, 0.3), RandomFunctions.random(0.6, 1.8), RandomFunctions.random(-0.3, 0.3)), 2
					,new Particle.DustOptions(Color.GREEN,1));
			p--;
		}
		
		Set<DMob> nearby = new HashSet<>();
		for (Entity i : Dungeons.w.getNearbyEntities(d.player.getLocation(), 12, 12, 12)) 
		{
			DMob m = DMobManager.get(i);
			if (m != null && m != dMob && !nearby.contains(m)) nearby.add(m);
		}
		
		double gain = nearby.size() * 0.05;
		if (gain > 1.75) gain = 1.75;
		return (int) (damage * (1+gain));
	} 
}
