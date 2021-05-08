package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchBlade extends AbsEnchant {

	@Override
	public String description() {
		return "Increases damage by " + 10*level + "%";
	}

	@Override
	public String name() {
		return "Blade " + level;
	}

	@Override
	public int max() {
		return 2;
	}

	@Override
	public int catalyst() {
		return 0;
	}
	@Override
	public String type()
	{
		return "weapon";
	}
	@Override
	public int rarity() {
		return level-1;
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		bank.damagemod += 0.1*level;
		return bank;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		// TODO Auto-generated method stub
		return null;
	}


}
