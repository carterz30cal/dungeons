package com.carterz30cal.items.abilities.runes;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Particle;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.RandomFunctions;

public class RuneSweat extends AbsAbility
{

    public static List<DMob> hit = new ArrayList<>();
	
	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(rune + "Sweat");
		d.add("First hit deals 4x damage.");
		d.add("All subsequent hits deal 80% damage.");
		return d;
	}
	
	
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		if (hit.size() > 200) hit.clear();
		int p = 9;
		while (p > 0)
		{
			Dungeons.w.spawnParticle(Particle.FALLING_WATER, dMob.entities.get(0).getLocation().add(RandomFunctions.random(-0.3, 0.3), RandomFunctions.random(0.6, 1.8), RandomFunctions.random(-0.3, 0.3)), 3
					);
			p--;
		}
		if (hit.contains(dMob)) return (int) (damage * 0.8);
		else
		{
			hit.add(dMob);
			return damage * 4;
		}

	} 

}
