package com.carterz30cal.items.abilities.runes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.Particle;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.RandomFunctions;

public class RuneHunt extends AbsAbility
{
	public static Map<DungeonsPlayer,DMob> hit = new HashMap<>();
	@Override
	public ArrayList<String> description() 
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(rune + "Hunt");
		d.add("Deals 3.1x damage if you keep");
		d.add("hitting the same enemy.");
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
		
		boolean same = hit.getOrDefault(d, null) == dMob;
		hit.put(d, dMob);
		return same ? (int)(damage * 3.1) : damage;
	} 
}
