package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchExplicious extends AbsEnchant {

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Gain " + level + " extra xp.";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Explicious " + level;
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
		return level;
	}

	@Override
	public String type() {
		// TODO Auto-generated method stub
		return "weapon";
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		// TODO Auto-generated method stub
		bank.d.stats.flatxp += level;
		return null;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		// TODO Auto-generated method stub
		return null;
	}

}
