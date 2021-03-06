package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.items.Rarity;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchIronskin extends AbsEnchant {

	@Override
	public String description() {
		return "For every " + (60 - (level*5)) + " base health, grant 2 armour";
	}

	@Override
	public String name() {
		return "Ironskin " + level;
	}

	@Override
	public int max() {
		return 8;
	}

	@Override
	public int catalyst() {
		return 0;
	}
	@Override
	public String type()
	{
		return "armour";
	}
	@Override
	public int rarity() {
		if (level >= 8) return Rarity.LEGENDARY.ordinal();
		return Math.min(level - 1,4);
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		int pts = (int)Math.round(bank.base.getOrDefault("health",0d) / (60 - (level*5)));
		bank.armour += pts*2;
		return bank;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		// TODO Auto-generated method stub
		return null;
	}


}
