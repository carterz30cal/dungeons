package com.carterz30cal.enchants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;

import com.carterz30cal.player.DungeonsPlayer;

public class EnchantBlade extends AbsEnch
{
	@Override
	public List<String> description() {
		List<String> l = new ArrayList<>();
		l.add("Grants" + ChatColor.RED + " +" + level*5 + "% Power.");
		return l;
	}
	
	public void stats(DungeonsPlayer d,Map<String,Double> attributes)
	{
		add(attributes,"damagep",0.05*level);
	}
	
	public int max() {return 2;}
}
