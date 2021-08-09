package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchBiomagnetic extends AbsEnchant {

	@Override
	public String description() {
		return "Doubles biomass drops.";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Biomagnetic";
	}

	@Override
	public int max() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int catalyst() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int rarity() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public String type() {
		// TODO Auto-generated method stub
		return "tool";
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		mine.loot.put("biomass", mine.loot.getOrDefault("biomass", 0)*2);
		return null;
	}

}
