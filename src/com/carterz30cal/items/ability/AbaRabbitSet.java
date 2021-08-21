package com.carterz30cal.items.ability;

import java.util.ArrayList;

import com.carterz30cal.player.DungeonsPlayerStats;

public class AbaRabbitSet extends AbsAbility {

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Coward");
		d.add("Gain" + textArmour(100) + " if below 40% health.");
		return d;
	}
	
	public void stats(DungeonsPlayerStats s) 
	{
		if (s.o.getHealthPercent() <= 0.4) s.armour += 100;
	}

}
