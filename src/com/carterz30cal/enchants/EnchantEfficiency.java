package com.carterz30cal.enchants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;

import com.carterz30cal.player.DungeonsPlayer;

public class EnchantEfficiency extends AbsEnch {

	@Override
	public List<String> description() {
		List<String> l = new ArrayList<>();
		l.add("Grants" + ChatColor.BLUE + " +" + level*10 + " Mining Speed.");
		return l;
	}
	
	public void stats(DungeonsPlayer d,Map<String,Double> attributes)
	{
		add(attributes,"miningspeed",10*level);
	}
	
	public int max() {return 2;}

}
