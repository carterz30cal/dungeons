package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class SpecialEnchLivingWood extends AbsEnchant {

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Lose all positive stats except Health. Gain 50% more health on this piece.";
	}

	@Override
	public String name() {
		return "Living Wood";
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
		return "armour";
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		bank.armour = Math.min(0, bank.armour);
		bank.regen = Math.min(0, bank.regen);
		bank.mana = Math.min(0, bank.mana);
		bank.killcoins = Math.min(0, bank.killcoins);
		bank.xpbonus = Math.min(0, bank.xpbonus);
		bank.damage = Math.min(0, bank.damage);
		bank.damagemod = Math.min(0, bank.damagemod);
		bank.sweep = Math.min(0, bank.sweep);
		
		bank.health *= 1.5;
		return bank;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		// TODO Auto-generated method stub
		return null;
	}

}
