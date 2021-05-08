package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchCryptLord extends AbsEnchant {

	@Override
	public String description() {
		return "Gain " + (3*level) + " armour and health while in a crypt";
	}

	@Override
	public String name() {
		return "Crypt Lord " + level;
	}

	@Override
	public int max() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public int catalyst() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int rarity() {
		// TODO Auto-generated method stub
		return level+1;
	}

	@Override
	public String type() {
		// TODO Auto-generated method stub
		return "armour";
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		if (bank.d.inCrypt)
		{
			bank.armour += 3*level;
			bank.health += 3*level;
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
