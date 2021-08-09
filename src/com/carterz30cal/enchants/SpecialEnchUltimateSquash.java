package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class SpecialEnchUltimateSquash extends AbsEnchant {

	@Override
	public String description() {
		return "+75% damage stat whilst in Infested Caverns";
	}

	@Override
	public String name() {
		return "Ultimate Squash";
	}

	@Override
	public int max() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int catalyst() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int rarity() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public String type() {
		// TODO Auto-generated method stub
		return "weapon";
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank)
	{
		if (bank.d.area.id == null || !bank.d.area.id.equals("infestedcaverns")) return null;
		bank.damagemod += 0.75;
		return bank;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		// TODO Auto-generated method stub
		return null;
	}

}
