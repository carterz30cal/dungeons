package com.carterz30cal.items.abilities.infested;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.player.DungeonsPlayerStats;

public class AbilityGuardiansSet extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Heavyweight");
		d.add("Gain 1% more armour for every");
		d.add("25 health over 200.");
		return d;
	}
	
	public void finalStats(DungeonsPlayerStats s) 
	{
		s.armour *= 1 + (s.health-200) * 0.0004;
	}

}
