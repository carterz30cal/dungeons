package com.carterz30cal.enchants;

import org.bukkit.entity.ArmorStand;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.mobs.DungeonMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchPaper extends AbsEnchant {

	@Override
	public String description() {
		return "Gives a " + 20*level + "% chance to mine wet paper";
	}

	@Override
	public String name() {
		return "Paper " + level;
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
		return Math.max(0,level-1);
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		return null;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		if (Math.random() < 0.2*level) mine.loot.put("wetpaper", 1);
		return mine;
	}

	@Override
	public void onHitAfter(DungeonsPlayer player, DungeonMob hit,ArmorStand ind) {
		// TODO Auto-generated method stub
		
	}

}
