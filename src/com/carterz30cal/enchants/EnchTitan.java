package com.carterz30cal.enchants;


import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchTitan extends AbsEnchant
{

	@Override
	public String description() {
		return "If above 70% health, gain " + (5*level) + " armour";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Titan " + level;
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
	public String type()
	{
		return "armour";
	}
	@Override
	public int rarity() {
		// TODO Auto-generated method stub
		return level;
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		return null;
	}
	@Override 
	public DungeonsPlayerStatBank onFinalBank(DungeonsPlayerStatBank bank)
	{
		if (bank.d.getHealthPercent() >= 0.7) bank.armour += 5*level;
		return bank;
	}
	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		// TODO Auto-generated method stub
		return null;
	}
}
