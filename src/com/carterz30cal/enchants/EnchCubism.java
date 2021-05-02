package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchCubism extends AbsEnchant {

	@Override
	public String description()
	{
		if (level == 8) return "What is wrong with you? +1 damage";
		return "Does nothing. Absolutely useless. Total waste of catalysts.";
	}

	@Override
	public String name() {
		return "Cubism " + level;
	}

	@Override
	public int max() {
		// TODO Auto-generated method stub
		return 8;
	}

	@Override
	public int catalyst() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int rarity() {
		if (level == 8) return 5;
		return level/2;
	}

	@Override
	public boolean hide()
	{
		return true;
	}
	@Override
	public String type() {
		// TODO Auto-generated method stub
		return "weapon";
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		if (level == 8)
		{
			bank.damage++;
			return bank;
		}
		return null;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		// TODO Auto-generated method stub
		return null;
	}

}
