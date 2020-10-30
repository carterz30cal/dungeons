package com.carterz30cal.enchants;

import org.bukkit.entity.ArmorStand;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.mobs.DungeonMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchTitan extends AbsEnchant
{

	@Override
	public String description() {
		return "If above 80% health, gain " + (3*level) + " armour";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Titan " + level;
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
		return level+1;
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		return null;
	}
	@Override 
	public DungeonsPlayerStatBank onFinalBank(DungeonsPlayerStatBank bank)
	{
		if (bank.d.getHealthPercent() >= 0.8) bank.armour += 3*level;
		return bank;
	}
	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onHitAfter(DungeonsPlayer player, DungeonMob hit, ArmorStand ind) {
		// TODO Auto-generated method stub
		
	}

}
