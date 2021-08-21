package com.carterz30cal.potions;

import java.util.List;

import org.bukkit.ChatColor;

import com.carterz30cal.items.ability.AbsAbility;

public abstract class AbsPotion extends AbsAbility
{
	public static String potionPrefix = ChatColor.RED + "";
	
	public abstract void text(List<String> lore);
	public int level = 1;
}
