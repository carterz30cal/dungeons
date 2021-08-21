package com.carterz30cal.enchants;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.utility.RandomFunctions;

public class EnchantBejeweled extends AbsEnch {

	@Override
	public List<String> description() {
		List<String> l = new ArrayList<>();
		l.add("When mining " + ChatColor.LIGHT_PURPLE + "Rough Gems" + ChatColor.GRAY + ", gain a " + level +"%");
		l.add("chance to dig up " + ChatColor.GOLD + "Golden Gemstones");
		return l;
	}
	
	public void onMine(DungeonMiningTable mine) 
	{
		if (mine.loot.containsKey("rough_gem") && RandomFunctions.random(1, 100) <= level)
		{
			int am = Math.floorDiv(mine.owner.stats.fortune,50);
			if (am == 1) mine.owner.player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "BEJEWELED! " + ChatColor.GOLD + "You dug up a Golden Gemstone");
			else mine.owner.player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "BEJEWELED! " + ChatColor.GOLD + "You dug up " + am + " Golden Gemstones");
		
			mine.loot.put("golden_gem", am);
		}
	};

	public int max() {return 4;}
}
