package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;

import net.md_5.bungee.api.ChatColor;

public class AbilityRainSummon extends AbsAbility {

	@Override
	public ArrayList<String> description() {
		ArrayList<String> desc = new ArrayList<String>();
		desc.add(prefix + "Summon Rain");
		desc.add("Brings the rain to Waterway");
		desc.add(" for 5 minutes.");
		desc.add(ChatColor.RED + "Must be in Waterway to summon.");
		return desc;
	}

}
