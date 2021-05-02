package com.carterz30cal.items.abilities.runes;

import java.util.ArrayList;

import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.player.DungeonsPlayer;

public class RuneSlayer extends AbsAbility
{

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<String>();
		d.add(rune + "Slayer");
		d.add("Overkill is 15% easier to activate");
		return d;
	}

	public void onTick  (DungeonsPlayer d)
	{
		d.stats.overkiller += 0.15;
	}
}
