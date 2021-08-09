package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.items.Rarity;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchBoost extends AbsEnchant {

	@Override
	public String description() {
		return "Grants +" + (level*4) + " health and mana.";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Boost " + level;
	}

	@Override
	public int max() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int catalyst() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int rarity() {
		// TODO Auto-generated method stub
		if (level == 3) return Rarity.MYTHICAL.ordinal();
		return level+2;
	}

	@Override
	public String type() {
		// TODO Auto-generated method stub
		return "armour";
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		bank.health += level*4;
		bank.mana += level*4;
		return bank;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		// TODO Auto-generated method stub
		return null;
	}

}
