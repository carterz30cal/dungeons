package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.player.DungeonsPlayerStatBank;
import com.carterz30cal.utility.RandomFunctions;

public class EnchPaper extends AbsEnchant {

	@Override
	public String description() {
		return "You also mine 1-" + (level+1) + " wet paper.";
	}

	@Override
	public String name() {
		return "Paper " + level;
	}

	@Override
	public int max() {
		return 3;
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
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		return null;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		mine.loot.put("wetpaper", RandomFunctions.random(1, level+1));
		return mine;
	}


}
