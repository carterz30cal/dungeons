package com.carterz30cal.enchants;

import org.bukkit.entity.ArmorStand;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.mobs.DungeonMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchProtection extends AbsEnchant {

	@Override
	public String description() {
		return "Increases base armour by " + (int)(12.5*level) + "%";
	}

	@Override
	public String name() {
		return "Protection " + level;
	}

	@Override
	public int max() {
		return 4;
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
		return level-1;
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		bank.armour += Math.round((bank.base.getOrDefault("armour",0d))*(0.125*level));
		return bank;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		// TODO Auto-generated method stub
		return null;
	}


}
