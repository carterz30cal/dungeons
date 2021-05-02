package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchSweeping extends AbsEnchant
{

	@Override
	public String description() {
		return "Adds an additional " + (int)Math.floor(level * 1.5) + " damage to sweep attacks";
	}

	@Override
	public String name() {
		return "Sweeping " + level;
	}

	@Override
	public int max()
	{
		return 5;
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
		return level / 2;
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		bank.sweep += (int)Math.floor(level * 1.5);
		return bank;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		// TODO Auto-generated method stub
		return null;
	}


}
