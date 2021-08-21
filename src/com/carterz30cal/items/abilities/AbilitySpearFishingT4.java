package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DamageType;
import com.carterz30cal.player.DungeonsPlayer;

import net.md_5.bungee.api.ChatColor;

public class AbilitySpearFishingT4 extends AbsAbility {

	
	
	
	
	
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
	public int onArrowLand(DungeonsPlayer d,DMob mob,int damage)
	{
		if (mob.type.tags.contains("spearfish")) mob.damage(7, d, DamageType.TRUE,false);
		return damage;
	}
}
