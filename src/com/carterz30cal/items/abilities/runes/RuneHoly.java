package com.carterz30cal.items.abilities.runes;

import java.util.ArrayList;

import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.player.DungeonsPlayerStats;

public class RuneHoly extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(rune + "Holy");
		d.add("Gain +13% health.");
		return d;
	}
	
	public void finalStats(DungeonsPlayerStats s) 
	{
		s.health *= 1.13;
	}
}
