package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.player.DungeonsPlayerStats;

public class AbilityDagger extends AbsAbility {

	@Override
	public ArrayList<String> description() 
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Dagger");
		d.add("You don't deal sweeping damage");
		return d;
	}
	
	public void finalStats(DungeonsPlayerStats s) 
	{
		s.damageSweep = 0;
	}

}
