package com.carterz30cal.items.abilities.infested;

import java.util.ArrayList;

import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStats;

public class AbilityFangedAxe extends AbsAbility
{

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Bloodlust");
		d.add("Gain 1 damage for every 5 levels");
		d.add("Regain 11 health every hit");
		return d;
	}
	
	public void stats(DungeonsPlayerStats s) 
	{
		s.damage += s.o.level.level() / 5;
	}
	
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		d.heal(11);
		return damage;
	}

}
