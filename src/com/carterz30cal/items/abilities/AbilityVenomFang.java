package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilityVenomFang extends AbsAbility 
{

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Venom");
		d.add("Reduces next heal by 50%");
		return d;
	}
	
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		dMob.nextHealMultiplier *= 0.5;
		return damage;
	}

}
