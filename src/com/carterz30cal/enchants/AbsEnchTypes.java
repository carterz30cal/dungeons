package com.carterz30cal.enchants;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.utility.Pair;

public enum AbsEnchTypes 
{
	BLADE(EnchantBlade.class,"Blade","weapon",0),
	MIDASTOUCH(EnchantMidasTouch.class,"Midas Touch","weapon",0),
	PROTECTION(EnchantProtection.class,"Protection","armour",0),
	EFFICIENCY(EnchantEfficiency.class,"Efficiency","tool",0),
	FORTUNE(EnchantFortune.class,"Fortune","tool",0),
	BEJEWELED(EnchantBejeweled.class,"Bejeweled","tool",0);
	
	
	public Class<? extends AbsEnch> enchant;
	public String name;
	public String type;
	public boolean special;
	public int catalyst;
	
	private AbsEnchTypes (Class<? extends AbsEnch> e,String n,String t,int c)
	{
		enchant = e;
		name = n;
		type = t;
		special = false;
		catalyst = c;
	}
	
	public static int catalyst(ItemStack book)
	{
		List<AbsEnchTypes> ench = getTypes(book.getItemMeta().getPersistentDataContainer());
		
		int catalyst = 0;
		for (AbsEnchTypes e : ench) catalyst = Math.max(e.catalyst, catalyst);
		
		return catalyst;
	}
	
	@SuppressWarnings("deprecation")
	public static List<AbsEnch> get(PersistentDataContainer con)
	{
		if (con == null) return null;
		String[] enchants = con.getOrDefault(ItemBuilder.kEnchants, PersistentDataType.STRING, "").split(";");
		
		List<AbsEnch> enchantL = new ArrayList<AbsEnch>();
		for (String e : enchants)
		{
			String[] es = e.split(",");
			if (es.length == 1) continue;
			int l = Integer.parseInt(es[1]);
			
			AbsEnch enchant = null;
			try 
			{
				enchant = valueOf(es[0]).enchant.newInstance();
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

	public static List<AbsEnchTypes> getTypes(PersistentDataContainer con)
	{
		if (con == null) return null;
		String[] enchants = con.getOrDefault(ItemBuilder.kEnchants, PersistentDataType.STRING, "").split(";");
		
		List<AbsEnchTypes> enchantL = new ArrayList<AbsEnchTypes>();
		for (String e : enchants)
		{
			String[] es = e.split(",");
			if (es.length == 1) continue;
			enchantL.add(valueOf(es[0]));
		}
		
		return enchantL;
	}
	
	@SuppressWarnings("deprecation")
	public static List<Pair<AbsEnchTypes,AbsEnch>> getPairs(PersistentDataContainer con)
	{
		if (con == null) return null;
		String[] enchants = con.getOrDefault(ItemBuilder.kEnchants, PersistentDataType.STRING, "").split(";");
		
		List<Pair<AbsEnchTypes,AbsEnch>> enchantL = new ArrayList<Pair<AbsEnchTypes,AbsEnch>>();
		for (String e : enchants)
		{
			String[] es = e.split(",");
			if (es.length == 1) continue;
			int l = Integer.parseInt(es[1]);
			
			AbsEnch enchant = null;
			try 
			{
				enchant = valueOf(es[0]).enchant.newInstance();
			} 
			catch (InstantiationException | IllegalAccessException e1) 
			{
				e1.printStackTrace();
			}
			
			enchant.level = l;
			enchantL.add(new Pair<AbsEnchTypes,AbsEnch> (valueOf(es[0]),enchant));
		}
		
		return enchantL;
	}
	
	@SuppressWarnings("deprecation")
	public static AbsEnch get(String ench)
	{
		
		AbsEnch enchant = null;
		try 
		{
			enchant = valueOf(ench).enchant.newInstance();
		} 
		catch (InstantiationException | IllegalAccessException e1) 
		{
			e1.printStackTrace();
		}
		
		enchant.level = enchant.max();
		return enchant;
	}
}
