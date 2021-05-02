package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DamageType;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchPrecursed extends AbsEnchant {

	@Override
	public String description()
	{
		return "Deal " + (5*level) + " true damage to ancient creatures";
	}

	@Override
	public String name() {
		return "Precursed " + level;
	}

	@Override
	public int max()
	{
		return 5;
	}

	@Override
	public int catalyst()
	{
		return 0;
	}

	@Override
	public int rarity() {
		return level;
	}

	@Override
	public String type() {
		return "weapon";
	}
	public int onHit(DungeonsPlayer player,DMob hit) 
	{
		if (hit.type.tags.contains("ancient")) 
		{
			hit.damage(5*level,player,DamageType.TRUE,false);
		}
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
