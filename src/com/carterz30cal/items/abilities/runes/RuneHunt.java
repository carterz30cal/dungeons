package com.carterz30cal.items.abilities.runes;

import java.util.ArrayList;
import java.util.Collection;
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

public class RuneHunt extends AbsAbility {

	@Override
	public ArrayList<String> description() 
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(rune + "Hunt");
		d.add("Deals 30% more damage");
		d.add("7% less for every enemy within 6 blocks");
		return d;
	}

	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		
		int p = 12;
		while (p > 0)
		{
			Dungeons.w.spawnParticle(Particle.REDSTONE, dMob.entities.get(0).getLocation().add(RandomFunctions.random(-0.4, 0.4),
					RandomFunctions.random(0.6, 1.8), RandomFunctions.random(-0.4, 0.4)), 2
					,new Particle.DustOptions(Color.PURPLE,1));
			p--;
		}
		
		Collection<Entity> near = Dungeons.w.getNearbyEntities(d.player.getLocation(), 6, 6, 6);
		
		Set<DMob> mobs = new HashSet<>();

		for (Entity i : near)
		{
			DMob m = DMobManager.get(i);
			if (!mobs.contains(m) && m != dMob) mobs.add(dMob);
		}
		
		return (int) (damage * (1.30 - (0.07 * mobs.size())));
	} 
}
