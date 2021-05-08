package com.carterz30cal.items.abilities.runes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Particle;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.RandomFunctions;

public class RuneSweat extends AbsAbility
{

    public static Map<DMob,Integer> hits = new HashMap<>();
	
	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(rune + "Sweat");
		d.add("Deal 36 extra damage");
		d.add("Deal 2 less for every hit");
		return d;
	}
	
	
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		int p = 9;
		while (p > 0)
		{
			Dungeons.w.spawnParticle(Particle.DRIP_WATER, dMob.entities.get(0).getLocation().add(RandomFunctions.random(-0.3, 0.3), RandomFunctions.random(0.6, 1.8), RandomFunctions.random(-0.3, 0.3)), 3
					);
			p--;
		}
		hits.put(dMob, hits.getOrDefault(dMob, 0)+1);
		return damage + 36 - (2 * (hits.get(dMob)-1));
	} 

}
