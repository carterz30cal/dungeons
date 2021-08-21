package com.carterz30cal.enchants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;

import com.carterz30cal.player.DungeonsPlayer;

public class EnchantFortune extends AbsEnch {

	@Override
	public List<String> description() {
		List<String> l = new ArrayList<>();
		l.add("Grants" + ChatColor.BLUE + " +" + level*4 + " Mining Fortune.");
		return l;
	}
	
	public void stats(DungeonsPlayer d,Map<String,Double> attributes)
	{
		add(attributes,"fortune",4*level);
	}
	
	public int max() {return 5;}

}
