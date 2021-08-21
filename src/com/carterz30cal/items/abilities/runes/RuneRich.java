package com.carterz30cal.items.abilities.runes;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Particle;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.RandomFunctions;

public class RuneRich extends AbsAbility
{
	
	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(rune + "Rich");
		d.add("Deals 2.65x damage");
		d.add("Costs 1 coin per 9 damage");
		return d;
	}
	
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		int cost = damage / 9;
		if (d.coins >= cost)
		{
			int p = 6;
			while (p > 0)
			{
				Dungeons.w.spawnParticle(Particle.REDSTONE, dMob.entities.get(0).getLocation().add(RandomFunctions.random(-0.4, 0.4), RandomFunctions.random(0.6, 1.8), RandomFunctions.random(-0.4, 0.4)), 2
						,new Particle.DustOptions(Color.YELLOW,1));
				p--;
			}
			
			d.coins -= cost;
			return (int) (damage * 2.65);
		}
		else return damage;
	} 
}
