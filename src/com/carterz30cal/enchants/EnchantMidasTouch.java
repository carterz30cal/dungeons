package com.carterz30cal.enchants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;

import com.carterz30cal.player.DungeonsPlayer;

public class EnchantMidasTouch extends AbsEnch {

	@Override
	public List<String> description() {
		List<String> l = new ArrayList<>();
		if (level == 1) l.add("Grants" + ChatColor.GOLD + " +1" + ChatColor.GRAY + " coin per kill");
		else l.add("Grants" + ChatColor.GOLD + " +" + level + ChatColor.GRAY + " coins per kill");
		return l;
	}

	public void stats(DungeonsPlayer d,Map<String,Double> attributes)
	{
		add(attributes,"killcoins",level);
	}
	
	public int max() {return 4;}
}
