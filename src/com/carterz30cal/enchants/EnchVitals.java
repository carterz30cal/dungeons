package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchVitals extends AbsEnchant 
{

	@Override
	public String description() {
		return "+" + (level * 10) + " Health, +" + level + " Regen";
	}

	@Override
	public String name() {
		return "Vitals " + level;
	}

	@Override
	public int max() {
		return 2;
	}

	@Override
	public int catalyst() 
	{
		return 0;
	}

	@Override
	public int rarity()
	{
		return level;
	}

	@Override
	public String type() {
		return "armour";
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank)
	{
		bank.health += level * 10;
		bank.regen += level;
		return bank;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		// TODO Auto-generated method stub
		return null;
	}

}
