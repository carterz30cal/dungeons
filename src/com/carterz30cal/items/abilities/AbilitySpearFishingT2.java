package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;

import net.md_5.bungee.api.ChatColor;

public class AbilitySpearFishingT2 extends AbsAbility {

	
	
	
	
	
	@Override
	public ArrayList<String> description() {
		ArrayList<String> desc = new ArrayList<String>();
		desc.add(prefix + "Fishin'");
		desc.add("Deals damage against fish");
		desc.add(ChatColor.BLUE + "Damage depends on the");
		desc.add(ChatColor.BLUE + " rarity of the spear used");
		return desc;
	}

	@Override
	public int onAttack(DungeonsPlayer d,DMob mob,int damage)
	{
		if (mob.type.tags.contains("spearfish")) return 2;
		else return damage;
	}
}
