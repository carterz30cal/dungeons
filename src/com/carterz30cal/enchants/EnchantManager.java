package com.carterz30cal.enchants;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.items.ItemBuilder;

public class EnchantManager
{
	public static EnchantManager i;
	public static HashMap<String,Class<? extends AbsEnchant>> enchantments;
	
	public EnchantManager() 
	{
		i = this;
		enchantments = new HashMap<String,Class<? extends AbsEnchant>>();
		
		// catalyst=0 enchants
		enchantments.put("midastouch", EnchMidasTouch.class);
		enchantments.put("sweeping", EnchSweeping.class);
		enchantments.put("blade", EnchBlade.class);
		enchantments.put("protection", EnchProtection.class);
		enchantments.put("ironskin", EnchIronskin.class);
		enchantments.put("lucky", EnchLucky.class);
		enchantments.put("fortune", EnchFortune.class);
		enchantments.put("paper",EnchPaper.class);
		enchantments.put("trunk", EnchTrunk.class);
		enchantments.put("execution", EnchExecution.class);
		enchantments.put("titan", EnchTitan.class);
		enchantments.put("tough", EnchTough.class);
		enchantments.put("polished", EnchPolished.class);
		enchantments.put("shocking", EnchShocking.class);
		enchantments.put("strength",EnchStrength.class);
		enchantments.put("precursed", EnchPrecursed.class);
		enchantments.put("spirit", EnchSpirit.class);
		enchantments.put("vitals", EnchVitals.class);
		enchantments.put("armourpolish", EnchArmourPolish.class);
		enchantments.put("shredding", EnchShredding.class);
		enchantments.put("cryptwarrior", EnchCryptWarrior.class);
		enchantments.put("cubism", EnchCubism.class);
		enchantments.put("efficiency", EnchEfficiency.class);
		enchantments.put("coarse", EnchCoarse.class);
	}
	@Deprecated
	public static int catalyst(ItemStack item, ItemStack book)
	{
		ArrayList<AbsEnchant> ench = get(item.getItemMeta().getPersistentDataContainer());
		ench.addAll(get(book.getItemMeta().getPersistentDataContainer()));
		
		int catalyst = 0;
		for (AbsEnchant e : ench) catalyst = Math.max(e.catalyst(), catalyst);
		
		return catalyst;
	}
	public static int catalyst(ItemStack book)
	{
		ArrayList<AbsEnchant> ench = get(book.getItemMeta().getPersistentDataContainer());
		
		int catalyst = 0;
		for (AbsEnchant e : ench) catalyst = Math.max(e.catalyst(), catalyst);
		
		return catalyst;
	}
	
	public static ArrayList<AbsEnchant> get(PersistentDataContainer con)
	{
		if (con == null) return null;
		String[] enchants = con.getOrDefault(ItemBuilder.kEnchants, PersistentDataType.STRING, "").split(";");
		
		ArrayList<AbsEnchant> enchantL = new ArrayList<AbsEnchant>();
		for (String e : enchants)
		{
			String[] es = e.split(",");
			if (es.length == 1) continue;
			int l = Integer.parseInt(es[1]);
			
			AbsEnchant enchant = null;
			try 
			{
				enchant = enchantments.get(es[0]).newInstance();
			} 
			catch (InstantiationException | IllegalAccessException e1) 
			{
				e1.printStackTrace();
			}
			
			enchant.level = l;
			enchantL.add(enchant);
		}
		
		return enchantL;
	}
	
	public static AbsEnchant get(String ench)
	{
		
		AbsEnchant enchant = null;
		try 
		{
			enchant = enchantments.get(ench).newInstance();
		} 
		catch (InstantiationException | IllegalAccessException e1) 
		{
			e1.printStackTrace();
		}
		
		enchant.level = enchant.max();
		return enchant;
	}
}
