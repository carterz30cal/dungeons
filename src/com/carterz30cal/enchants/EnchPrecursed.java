package com.carterz30cal.enchants;

import org.bukkit.entity.ArmorStand;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.abilities.DMobAbility;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchPrecursed extends AbsEnchant {

	@Override
	public String description()
	{
		return "Deal " + (2*level) + " true damage to ancient creatures";
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
	public void onHitAfter(DungeonsPlayer player,DMob hit,ArmorStand ind) 
	{
		if (hit.type.tags.contains("ancient")) 
		{
			for (DMobAbility mab : hit.type.abilities) mab.damaged(hit, player,2*level);
			hit.damage(2*level, player.player, true);
		}
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
