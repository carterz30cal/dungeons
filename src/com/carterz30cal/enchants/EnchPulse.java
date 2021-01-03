package com.carterz30cal.enchants;

import org.bukkit.entity.ArmorStand;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.mobs.DungeonMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchPulse extends AbsEnchant {

	@Override
	public String description() {
		return "If this item has over 80 health, grant 5 damage";
	}

	@Override
	public String name() {
		return "Pulse 1";
	}

	@Override
	public int max() {
		return 1;
	}

	@Override
	public int catalyst() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int rarity() {
		return 1;
	}

	@Override
	public String type() {
		return "weapon";
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		if (bank.health > 80) bank.damage += 5;
		return bank;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		// TODO Auto-generated method stub
		return null;
	}


}
