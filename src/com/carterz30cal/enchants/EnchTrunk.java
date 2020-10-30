package com.carterz30cal.enchants;

import org.bukkit.entity.ArmorStand;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.mobs.DungeonMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchTrunk extends AbsEnchant {

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "+" + (level) + " armour per base item regen";
	}

	@Override
	public String name() {
		return "Trunk " + level;
	}

	@Override
	public int max() {
		return 6;
	}

	@Override
	public int catalyst() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int rarity() {
		// TODO Auto-generated method stub
		return level/2;
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		double reg = bank.base.getOrDefault("regen", 0d);
		bank.armour += reg * level;
		return bank;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		return null;
	}

	@Override
	public void onHitAfter(DungeonsPlayer player, DungeonMob hit,ArmorStand ind) {
		// TODO Auto-generated method stub
		
	}

}
