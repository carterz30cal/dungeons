package com.carterz30cal.crafting;

import org.bukkit.inventory.ItemStack;

public class Recipe
{
	public int[] amounts;
	public ItemStack product;
	public int amount;
	public int xp;
	
	public Recipe (ItemStack p,int x,int[] a)
	{
		amounts = a;
		product = p;
		amount = p.getAmount();
		xp = x;
	}
	
	
	public boolean isCraftable(ItemStack[] ingredients)
	{
		for (int i = 0; i < 9; i++)
		{
			if (ingredients[i] == null) continue;
			else
			{
				if (ingredients[i].getAmount() < amounts[i]) return false;
			}
		}
		return true;
	}
	
	public ItemStack[] craft(ItemStack[] ingredients)
	{
		if (!isCraftable(ingredients)) return ingredients;
		
		for (int i = 0; i < 9; i++)
		{
			if (ingredients[i] == null) continue;
			else
			{
				ingredients[i].setAmount(ingredients[i].getAmount()-amounts[i]);
			}
		}
		return ingredients;
	}
}
