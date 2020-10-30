package com.carterz30cal.enchants;

import org.bukkit.entity.ArmorStand;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.mobs.DungeonMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchLucky extends AbsEnchant {

	@Override
	public String description() {
		return "Increases ore chance by " + (3*level) + "%";
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
	public int rarity() {
		return level/3;
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		bank.orechance += 0.03*level;
		return bank;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onHitAfter(DungeonsPlayer player, DungeonMob hit,ArmorStand ind) {
		// TODO Auto-generated method stub
		
	}

}
