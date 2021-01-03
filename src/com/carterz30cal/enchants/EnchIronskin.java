package com.carterz30cal.enchants;

import org.bukkit.entity.ArmorStand;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.mobs.DungeonMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchIronskin extends AbsEnchant {

	@Override
	public String description() {
		return "Exchanges every " + (int)(6 - level) + " base health for 1 armour";
	}

	@Override
	public String name() {
		return "Ironskin " + level;
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
		return "armour";
	}
	@Override
	public int rarity() {
		return (level/2)+2;
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		int pts = (bank.base.getOrDefault("health", 0d)).intValue() / (6-level);
		bank.health -= (6 - level) * pts;
		bank.armour += pts;
		return bank;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		// TODO Auto-generated method stub
		return null;
	}


}