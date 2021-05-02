package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.inventory.meta.ItemMeta;

import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.items.magic.ItemWand;

public class ModManaEfficient extends AbsAbility
{

	
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<String>();
		d.add(modifier + "Mana-Efficient");
		d.add("Your spells cost 12 less mana");
		return d;
	}

	@Override
	public int magicCost(ItemMeta wand) 
	{
		return -12;
	} 

}
