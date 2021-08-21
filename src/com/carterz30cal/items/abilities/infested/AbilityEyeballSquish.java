package com.carterz30cal.items.abilities.infested;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilityEyeballSquish extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Squish");
		d.add("Deals 4x damage to Sathlar's Eyes");
		return d;
	}
	
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		if (dMob.type.id.equals("temple_boss_eye")) return damage * 4;
		return damage;
	}

}
