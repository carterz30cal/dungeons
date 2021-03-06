package com.carterz30cal.items.abilities.runes;

import java.util.ArrayList;
import java.util.Collection;

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
		d.add("Deals 3 extra damage for every enemy");
		d.add("within 9 blocks");
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
		
		Collection<Entity> near = Dungeons.w.getNearbyEntities(d.player.getLocation(), 9, 9, 9);
		int e = 0;
		for (Entity i : near) if (i != dMob.entities.get(0) && DMobManager.get(i) != null) e++;
		
		return damage + (3*e);
	} 
}
