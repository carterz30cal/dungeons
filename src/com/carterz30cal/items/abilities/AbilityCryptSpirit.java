package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DamageType;
import com.carterz30cal.player.DungeonsPlayer;

import net.md_5.bungee.api.ChatColor;

public class AbilityCryptSpirit extends AbsAbility 
{

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Crypt Spirit");
		d.add("When your arrow hits a mob");
		d.add("consume all your remaining mana");
		d.add("and deal 120% of that as magic damage");
		d.add("You also take 5% of that as true");
		d.add("damage. " + ChatColor.RED + "Only works in crypts.");
		return d;
	}

	public int onArrowLand(DungeonsPlayer d,DMob mob,int damage) 
	{
		if (!d.inCrypt) return damage;
		int magic = (int) (d.getMana() * 1.2);
		d.useMana(d.getMana());
		mob.damage(magic, d, DamageType.MAGIC);
		d.damage((int) (magic / (1.2*20)), true);
		return damage;
	}
}
