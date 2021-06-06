package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchSteel extends AbsEnchant
{

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Adds " + (5*level) + " + " + (2*level) + "% armour to this piece.";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Steel " + level;
	}

	@Override
	public int max() {
		// TODO Auto-generated method stub
		return 4;
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
		return "armour";
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		bank.armour += Math.round((bank.base.getOrDefault("armour",0d))*(0.02*level));
		bank.armour += 5*level;
		return bank;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		// TODO Auto-generated method stub
		return null;
	}

}
