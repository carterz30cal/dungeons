package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.RandomFunctions;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Particle;

public class AbilityStinger extends AbsAbility
{

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Stinger");
		d.add("Your arrows have a 33%");
		d.add("chance to poison enemies");
		d.add(ChatColor.GREEN + "Poisoned enemies take 75%");
		d.add(ChatColor.GREEN + "more damage from this bow");
		return d;
	}

	public int onArrowLand(DungeonsPlayer d,DMob mob,int damage) 
	{ 
		if (mob.effects.contains("stinger")) return (int) (damage * 1.75);
		else if (RandomFunctions.random(0, 2) != 2) return damage;
		int p = 14;
		while (p > 0)
		{
			Dungeons.w.spawnParticle(Particle.REDSTONE, mob.entities.get(0).getLocation().add(RandomFunctions.random(-0.2, 0.2), RandomFunctions.random(0.75, 1.9), RandomFunctions.random(-0.2, 0.2)), 3
					,new Particle.DustOptions(Color.GREEN,0.6f));
			p--;
		}
		mob.effects.add("stinger");
		return damage;
	}
}
