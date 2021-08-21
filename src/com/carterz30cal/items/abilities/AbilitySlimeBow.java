package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.entity.Slime;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;

public class AbilitySlimeBow extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> des = new ArrayList<String>();
		des.add(prefix + "Slime Hunt");
		des.add("Multiplies damage to slimes");
		des.add(" depending on their size");
		return des;
	}

	@Override
	public int onAttack(DungeonsPlayer d,DMob mob,int damage)
	{
		if (mob.type.tags.contains("slime")) 
		{
			Slime slime = (Slime)mob.entities.get(0);
			return (int)(damage * slime.getSize());
		}
		else return damage;
	}
}
