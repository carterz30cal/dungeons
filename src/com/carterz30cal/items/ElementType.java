package com.carterz30cal.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.carterz30cal.gui.GUICreator;
import com.carterz30cal.utility.StringManipulator;

import net.md_5.bungee.api.ChatColor;

public enum ElementType
{
	FIRE,
	WATER,
	EARTH,
	AIR,
	VENOM,
	MIDAS,
	VOID;
	
	public String display()
	{
		String[] displays = {ChatColor.RED + "▼",ChatColor.AQUA + "○",ChatColor.GREEN + "◆",
				ChatColor.WHITE + "▲",ChatColor.DARK_GREEN + "∨",ChatColor.GOLD + "♣",
				ChatColor.DARK_GRAY + "■"};
		return displays[ordinal()];
	}
	
	
	
	
	public String pretty()
	{
		return StringManipulator.capitalise(name());
	}
	
	public ItemStack gui(int level)
	{
		ItemStack ret = null;
		String name = display() + " " + StringManipulator.capitalise(name()) + " " + level;
		
		if (this == ElementType.FIRE)       ret = GUICreator.item(Material.RED_CONCRETE,name,null);
		else if (this == ElementType.WATER) ret = GUICreator.item(Material.BLUE_CONCRETE,name,null);
		else if (this == ElementType.EARTH) ret = GUICreator.item(Material.LIME_CONCRETE,name,null);
		else if (this == ElementType.AIR)   ret = GUICreator.item(Material.WHITE_CONCRETE,name,null);
		else if (this == ElementType.VENOM) ret = GUICreator.item(Material.GREEN_CONCRETE,name,null);
		else if (this == ElementType.MIDAS) ret = GUICreator.item(Material.YELLOW_CONCRETE,name,null);
		else ret = GUICreator.item(Material.BLACK_CONCRETE,ChatColor.DARK_GRAY + "■ Void",null);

		ret.setAmount(level);
		return ret;
	}
	public ElementType shift()
	{
		switch (this)
		{
		case FIRE: return WATER;
		case WATER: return EARTH;
		case EARTH: return AIR;
		case AIR: return VENOM;
		case VENOM: return MIDAS;
		default: return VOID;
		}
	}
	public ElementType inverse()
	{
		switch (this)
		{
		case FIRE: return WATER;
		case WATER: return FIRE;
		case EARTH: return AIR;
		case AIR: return EARTH;
		default:
			return this;
		}
	}
}
