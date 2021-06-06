package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.player.DungeonsPlayerStatBank;
import com.carterz30cal.utility.RandomFunctions;

import net.md_5.bungee.api.ChatColor;

public class EnchLucky extends AbsEnchant {

	@Override
	public String description() {
		return "1% chance to dig up " + (75*level) + " coins.";
	}

	@Override
	public String name() {
		return "Lucky " + level;
	}

	@Override
	public int max() {
		return 5;
	}

	@Override
	public int catalyst() {
		return 0;
	}
	@Override
	public String type()
	{
		return "tool";
	}
	@Override
	public int rarity() {
		return level - 1;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		if (RandomFunctions.random(1, 100) == 50)
		{
			mine.owner.coins += 75*level;
			mine.owner.player.sendMessage(ChatColor.GOLD + "You dug up " + (75*level) + " coins!");
		}
		return null;
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		// TODO Auto-generated method stub
		return null;
	}



}
