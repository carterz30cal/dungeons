package com.carterz30cal.items.abilities.runes;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DamageType;
import com.carterz30cal.player.DungeonsPlayer;

public class RuneSlayer extends AbsAbility
{

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<String>();
		d.add(rune + "Slayer");
		d.add("Gain double coins from overkill");
		d.add("and heal for 20❤ every time it procs.");
		d.add("Also deal 5 true damage on every hit.");
		return d;
	}
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		dMob.damage(5, d, DamageType.TRUE);
		return damage;
	} 
	public int onOverkill(DungeonsPlayer d,DMob mob,int overkillamount) 
	{
		d.heal(20);
		return overkillamount;
	};
}
