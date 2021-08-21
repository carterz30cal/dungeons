package com.carterz30cal.items.abilities.runes;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Particle;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.RandomFunctions;

public class RuneAcid extends AbsAbility
{
	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(rune + "Acid");
		d.add("This weapon ignores 60% of armour");
		return d;
	}
	
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		int p = 13;
		while (p > 0)
		{
			Dungeons.w.spawnParticle(Particle.REDSTONE, dMob.entities.get(0).getLocation().add(RandomFunctions.random(-0.2, 0.2), RandomFunctions.random(0.75, 1.9), RandomFunctions.random(-0.2, 0.2)), 3
					,new Particle.DustOptions(Color.GREEN,0.8f));
			p--;
		}
		int arm = (int) (dMob.type.armour * 0.6);
		return damage + arm;
	} 
}
