package com.carterz30cal.items.abilities.runes;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.player.DungeonsPlayerStats;

public class RuneSpace extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(rune + "Space");
		d.add("For every 4 mana, gain");
		d.add("0.7% damage as a stat.");
		return d;
	}
	
	public void finalStats(DungeonsPlayerStats s) 
	{
		s.damagemod += (s.mana / 4) * 0.007;
	}
}
