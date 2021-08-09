package com.carterz30cal.items.abilities.runes;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Particle;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStats;
import com.carterz30cal.utility.RandomFunctions;

public class RuneVoid extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(rune + "Void");
		d.add("Deal 3.5x damage");
		d.add("Your non-damage stats");
		d.add("are heavily reduced.");
		return d;
	}

	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		int p = 9;
		while (p > 0)
		{
			Dungeons.w.spawnParticle(Particle.REDSTONE, dMob.entities.get(0).getLocation().add(RandomFunctions.random(-0.4, 0.4), RandomFunctions.random(0.6, 1.8), RandomFunctions.random(-0.4, 0.4)), 2
					,new Particle.DustOptions(Color.BLACK,1));
			p--;
		}
			
		return (int) (damage * 3.5);
	} 
	
	public void finalStats(DungeonsPlayerStats s) 
	{
		s.health *= 0.2;
		s.armour *= 0.2;
		s.mana   *= 0.2;
	}
}
