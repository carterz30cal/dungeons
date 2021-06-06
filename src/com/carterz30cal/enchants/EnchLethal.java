package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DamageType;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchLethal extends AbsEnchant {

	@Override
	public String description() {
		return "Deal " + Math.round(level*2.25d) + " true damage";
	}

	@Override
	public String name() {
		return "Lethal " + level;
	}

	@Override
	public int max() {
		return 4;
	}

	@Override
	public int catalyst() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int rarity() {
		// TODO Auto-generated method stub
		return level;
	}

	@Override
	public String type() {
		// TODO Auto-generated method stub
		return "weapon";
	}
	@Override
	public int onHit(DungeonsPlayer player,DMob hit) 
	{
		hit.damage((int) Math.round(level*2.25d),player,DamageType.TRUE,false);
		return 0;
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
