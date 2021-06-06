package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchFortune extends AbsEnchant {

	@Override
	public String description() {
		return "+" + (10*level) + " mining fortune.";
	}

	@Override
	public String name() {
		return "Fortune " + level;
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
		return level;
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		bank.fortune += 10*level;
		return bank;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		return null;
	}


}
