package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStats;

public class AbilityDevastation extends AbsAbility
{

	@Override
	public ArrayList<String> description() 
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Devastation");
		d.add("Lose 50% of your health");
		d.add("You have no regen.");
		d.add("Deal 10 more damage to");
		d.add("enemies with more than");
		d.add("1000 health.");
		return d;
	}
	
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage)
	{
		if (dMob.health > 1000) return damage + 10;
		else return damage;
	}
	
	public void finalStats(DungeonsPlayerStats s) 
	{
		s.health *= 0.5;
		s.regen = 0;
	}

}
