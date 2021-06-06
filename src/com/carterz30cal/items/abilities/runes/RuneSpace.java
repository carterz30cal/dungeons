package com.carterz30cal.items.abilities.runes;

import java.util.ArrayList;

import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStats;
import com.carterz30cal.utility.RandomFunctions;

public class RuneSpace extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(rune + "Space");
		d.add("Deals from 0x damage up to");
		d.add("2.25x randomly.");
		d.add("You lose 40% of your mana.");
		return d;
	}

	public int onAttack(DungeonsPlayer d,DMob dMob,int damage)
	{
		int replacementdmg = (int) (RandomFunctions.random(0d, 2.25d) * damage);
		return replacementdmg;
	}
	
	public void finalStats(DungeonsPlayerStats s) 
	{
		s.mana *= 0.4;
	}
}
