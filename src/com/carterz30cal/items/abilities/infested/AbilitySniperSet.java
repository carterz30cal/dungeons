package com.carterz30cal.items.abilities.infested;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStats;

public class AbilitySniperSet extends AbsAbility {

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Arrow Attunement");
		d.add("Your damage is multiplied by 75%");
		d.add("Deal 150% more damage with arrows.");
		d.add("You deal no sweeping damage.");
		return d;
	}

	public int onArrowLand(DungeonsPlayer d,DMob mob,int damage) 
	{ 
		return (int) (damage * 2.5);
	} 
	public void finalStats(DungeonsPlayerStats s) 
	{
		s.damage *= 0.75;
		s.damageSweep = 0;
	}
}
