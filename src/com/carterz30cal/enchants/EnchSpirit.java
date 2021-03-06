package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchSpirit extends AbsEnchant {

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "+" + (level*3) + " mana per damage, -" + level + " mana per armour";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Spirit " + level;
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
		return "armour";
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank)
	{
		bank.mana += level * bank.base.getOrDefault("damage", 0d) * 3;
		bank.mana -= level * bank.base.getOrDefault("armour", 0d);
		return bank;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		// TODO Auto-generated method stub
		return null;
	}

}
