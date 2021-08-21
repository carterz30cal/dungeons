package com.carterz30cal.items.abilities.infested;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStats;

public class AbilityFangedAxe extends AbsAbility
{

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Bloodlust");
		d.add("Gain 1 damage for every level");
		d.add("over 20. Heal 11❤ every hit.");
		return d;
	}
	
	public void stats(DungeonsPlayerStats s) 
	{
		s.damage += s.o.level.level() - 20;
	}
	
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		d.heal(11);
		return damage;
	}

}
