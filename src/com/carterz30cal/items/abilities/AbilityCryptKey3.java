package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import net.md_5.bungee.api.ChatColor;

public class AbilityCryptKey3 extends AbsAbility 
{

	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<String>();
		d.add(prefix + "Crypt Key");
		d.add("Allows you to open a crypt");
		d.add("This key opens an incredibly tough crypt");
		//d.add(ChatColor.RED + "Single use!");
		d.add(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Not yet implemented!");
		
		return d;
	}

}
