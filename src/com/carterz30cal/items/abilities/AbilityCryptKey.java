package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;

import net.md_5.bungee.api.ChatColor;

public class AbilityCryptKey extends AbsAbility 
{

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Crypt Key");
		d.add("Allows you to open a crypt");
		d.add(ChatColor.RED + "Single use!");
		
		return d;
	}

}
