package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchSquash extends AbsEnchant
{

	@Override
	public String description()
	{
		return "Deal +" + (level*5) + "% damage to mobs in the infested caverns";
	}

	@Override
	public String name() {
		return "Squash " + level;
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
		return level-1;
	}

	@Override
	public String type() {
		// TODO Auto-generated method stub
		return "weapon";
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		if (bank.d == null || bank.d.area == null || bank.d.area.id == null) return bank;
		if (bank.d.area.id.equals("infestedcaverns"))
		{
			bank.damagemod += 0.05*level;
			return bank;
		}
		return null;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		// TODO Auto-generated method stub
		return null;
	}

}
