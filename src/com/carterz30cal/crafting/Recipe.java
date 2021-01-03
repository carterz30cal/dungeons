package com.carterz30cal.crafting;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.items.Item;
import com.carterz30cal.items.ItemBuilder;

public class Recipe
{
	public String[] types;
	public int[] amounts;
	public ItemStack product;
	public int amount;
	public int xp;
	
	public int upgrade = 4;
	public Recipe (ItemStack p,int x,int[] a,String[] m)
	{
		amounts = a;
		product = p;
		amount = p.getAmount();
		xp = x;
		types = m;
	}
	
	public ItemStack upgradedProduct(ItemStack[] ingredients)
	{
		ItemStack up = product.clone();
		ItemStack u = ingredients[upgrade];
		ItemMeta upm = up.getItemMeta();
		Item i = ItemBuilder.get(u);
		if (i == null || !i.type.equals("weapon") || u == null || !u.hasItemMeta()) return up;
		for (NamespacedKey k : u.getItemMeta().getPersistentDataContainer().getKeys())
		{
			if (k.equals(ItemBuilder.kItem) || k.equals(ItemBuilder.kCustomName)) continue;
			upm.getPersistentDataContainer().set(k, PersistentDataType.STRING, u.getItemMeta().getPersistentDataContainer().get(k, PersistentDataType.STRING));
		}
		ItemBuilder.i.updateMeta(upm, null);
		up.setItemMeta(upm);
		return up;
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
