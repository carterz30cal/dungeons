package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.player.DungeonsPlayerStats;


public class AbilityReaperLeggings extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Might of Souls");
		d.add("Your souls deal 20% more damage.");
		d.add("Gain 2 damage per alive soul.");
		return d;
	}
	
	public void stats(DungeonsPlayerStats s) 
	{
		int alive = 0;
		for (Soul soul : AbilityReaperBlade.souls.getOrDefault(s.o, new ArrayList<>())) if (soul.summoned) alive++;
		s.damage += 2*alive;
	}

}
