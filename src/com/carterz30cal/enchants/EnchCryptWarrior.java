package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchCryptWarrior extends AbsEnchant {

	@Override
	public String description() {
		return "While in a crypt, gain " + (3*level) + "% damage";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Crypt Warrior " + level;
	}

	@Override
	public int max() {
		// TODO Auto-generated method stub
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
		return level;
	}

	@Override
	public String type() {
		// TODO Auto-generated method stub
		return "weapon";
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank)
	{
		if (!bank.d.inCrypt) return null;
		bank.damagemod += 0.03*level;
		return bank;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		// TODO Auto-generated method stub
		return null;
	}

}
