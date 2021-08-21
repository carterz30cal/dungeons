package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.RandomFunctions;

public class AbilityHandsomeReward extends AbsAbility
{

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Handsome Reward");
		d.add("50% chance to deal no damage");
		return d;
	}
	@Override
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage)
	{
		if (RandomFunctions.random(0, 1) == 0) return 0;
		return damage;
	}
}
