package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchEfficiency extends AbsEnchant {

	@Override
	public String description() {
		return "+" + (7*level) + " mining speed.";
	}

	@Override
	public String name() {
		return "Efficiency " + level;
	}

	@Override
	public int max() {
		return 5;
	}

	@Override
	public int catalyst() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int rarity() {
		// TODO Auto-generated method stub
		return level-1;
	}

	@Override
	public String type() {
		// TODO Auto-generated method stub
		return "tool";
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		// TODO Auto-generated method stub
		bank.miningspeed += 7*level;
		return bank;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		// TODO Auto-generated method stub
		return null;
	}

}
