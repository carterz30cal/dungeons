package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class AbilityCryptQueenSword extends AbsAbility
{

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Crypt Queen");
		d.add("While in a crypt, double the regular");
		d.add("stats that this item grants.");
		d.add("Also gain 3 coins on every hit.");
		return d;
	}
	
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		d.coins += 3;
		return damage;
	}
	
	public void statbank(DungeonsPlayerStatBank s)
	{
		if (!s.d.inCrypt) return;
		s.armour *= 2;
		s.health *= 2;
		s.mana *= 2;
		s.damage *= 2;
		s.damagemod *= 2;
		s.regen *= 2;
		s.sweep *= 2;
	}
	
}
