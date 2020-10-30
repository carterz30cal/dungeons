package com.carterz30cal.enchants;

import org.bukkit.entity.ArmorStand;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.mobs.DungeonMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchTough extends AbsEnchant {

	@Override
	public String description() {
		return "If this piece has 20 armour, grant " + (5+(5*level)) + " more";
	}

	@Override
	public String name() {
		return "Tough " + level;
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
	public int rarity() {
		return level;
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		if (bank.base.getOrDefault("armour", 0d) >= 20) bank.armour += 5 + (5*level);
		return null;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		return null;
	}

	@Override
	public void onHitAfter(DungeonsPlayer player, DungeonMob hit, ArmorStand ind) {
		
	}

}
