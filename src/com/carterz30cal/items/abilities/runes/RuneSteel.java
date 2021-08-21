package com.carterz30cal.items.abilities.runes;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.player.DungeonsPlayerStats;

public class RuneSteel extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(rune + "Steel");
		d.add("Gain 100 armour.");
		return d;
	}
	
	@Override
	public void stats(DungeonsPlayerStats s) 
	{
		s.armour += 100;
	}
}
