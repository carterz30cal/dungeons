package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import net.md_5.bungee.api.ChatColor;

public class AbilityCryptKey2 extends AbsAbility 
{

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Crypt Key");
		d.add("Allows you to open a crypt");
		d.add("This key opens a tougher crypt");
		d.add(ChatColor.RED + "Single use!");
		
		return d;
	}

}
