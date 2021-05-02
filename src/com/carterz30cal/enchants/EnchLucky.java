package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchLucky extends AbsEnchant {

	@Override
	public String description() {
		return "Make rare ores " + (1*level) + "% more likely to appear";
	}

	@Override
	public String name() {
		return "Lucky " + level;
	}

	@Override
	public int max() {
		return 5;
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
		return level/3;
	}

	public double setRareOreMultiplier(DungeonsPlayer player,double chance)
	{
		return chance + (level*0.01);
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		// TODO Auto-generated method stub
		return null;
	}



}
