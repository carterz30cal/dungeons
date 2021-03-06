package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchStrength extends AbsEnchant {

	@Override
	public String description() {
		return "+" + (level * 2) + "% damage to this piece";
	}

	@Override
	public String name() {
		return "Strength " + level;
	}

	@Override
	public int max() 
	{
		return 5;
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
	public String type()
	{
		return "armour";
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		bank.damagemod += 0.02 * level;
		return bank;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		// TODO Auto-generated method stub
		return null;
	}

}
