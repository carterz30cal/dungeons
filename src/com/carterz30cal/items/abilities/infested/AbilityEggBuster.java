package com.carterz30cal.items.abilities.infested;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobType;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilityEggBuster extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Egg Buster");
		d.add("Deals 5x damage to spider eggs");
		d.add("Gain 4 more coins on kill.");
		return d;
	}
	
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) 
	{
		if (dMob.type.tags.contains("spideregg")) return damage * 5;
		return damage;
	}
	
	public void onKill (DungeonsPlayer d,DMobType mob) 
	{
		if (mob.tags.contains("spideregg"))
		{
			d.coins += 4;
		}
	}

}
