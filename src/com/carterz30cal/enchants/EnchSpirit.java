package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchSpirit extends AbsEnchant {

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "+" + (10*level) + "% mana for this piece";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Spirit " + level;
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
		return level;
	}

	@Override
	public String type() {
		// TODO Auto-generated method stub
		return "armour";
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank)
	{
		bank.mana += bank.base.getOrDefault("mana", 0d) * (0.1*level);
		return bank;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		// TODO Auto-generated method stub
		return null;
	}

}
