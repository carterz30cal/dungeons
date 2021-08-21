package com.carterz30cal.items.abilities.runes;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Particle;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.RandomFunctions;

public class RuneBlood extends AbsAbility
{
	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(rune + "Blood");
		d.add("Deals 6% more damage for every");
		d.add("percent of health missing on the target.");
		return d;
	}
	
	
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		int p = 8;
		while (p > 0)
		{
			Dungeons.w.spawnParticle(Particle.REDSTONE, dMob.entities.get(0).getLocation().add(RandomFunctions.random(-0.3, 0.3), RandomFunctions.random(0.6, 1.8), RandomFunctions.random(-0.3, 0.3)), 3
					,new Particle.DustOptions(Color.RED,1));
			p--;
		}
		double missing = 1 - ((double)dMob.health / dMob.health());
		missing *= 6;
		return (int) (damage * (1 + missing));
	} 

}
