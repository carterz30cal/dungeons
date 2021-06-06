package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DamageType;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchThorns extends AbsEnchant
{

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "When attacked, deal " + (int) (level*1.5d) + " damage to the attacker.";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Thorns " + level;
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
		return level-1;
	}

	@Override
	public String type() {
		// TODO Auto-generated method stub
		return "armour";
	}

	@Override
	public void onDamaged(DungeonsPlayer player,DMob damager)
	{
		if (damager == null) return;
		damager.damage((int) (level*1.5d), player, DamageType.PHYSICAL, false);
	}
	
	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		// TODO Auto-generated method stub
		return null;
	}

}
