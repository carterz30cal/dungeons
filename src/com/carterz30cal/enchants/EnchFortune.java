package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchFortune extends AbsEnchant {

	@Override
	public String description() {
		return "While mining, 10% chance to " + (level+1) + "x drops";
	}

	@Override
	public String name() {
		return "Fortune " + level;
	}

	@Override
	public int max() {
		return 3;
	}

	@Override
	public int catalyst() {
		return 0;
	}
	@Override
	public String type()
	{
		return "tool";
	}
	@Override
	public int rarity() {
		return level;
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		return null;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		if (Math.random() <= 0.1)
		{
			for (String loot : mine.loot.keySet()) mine.loot.put(loot, mine.loot.get(loot) * (level+1));
		}
		return mine;
	}


}
