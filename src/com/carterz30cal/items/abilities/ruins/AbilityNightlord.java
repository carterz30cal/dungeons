package com.carterz30cal.items.abilities.ruins;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.player.DungeonsPlayerStats;

public class AbilityNightlord extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Lord of the Night");
		d.add("When in the dark, buff your damage by 50.");
		d.add("Buffs the explosion of the Punishing Strike.");
		return d;
	}
	
	public void stats(DungeonsPlayerStats s) 
	{
		if (s.o.player.getEyeLocation().getBlock().getLightLevel() < 8) s.damage += 50;
	}

}
