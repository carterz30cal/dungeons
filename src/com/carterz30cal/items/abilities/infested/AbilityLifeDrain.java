package com.carterz30cal.items.abilities.infested;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;

import net.md_5.bungee.api.ChatColor;

public class AbilityLifeDrain extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Life Drain");
		d.add("Heal 30❤ for every hit that deals");
		d.add("more than 1000 damage.");
		d.add(ChatColor.RED + "You cannot apply runes to this item.");
		return d;
	}
	
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage)
	{
		if (damage > 1000) d.heal(30);
		return damage;
	}

}
