package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStatBank;
import com.carterz30cal.utility.RandomFunctions;

public class EnchCoarse extends AbsEnchant
{

	@Override
	public String description() 
	{
		return "Grants a 10% chance to deal " + (level*4) + " extra damage on hit!";
	}

	@Override
	public String name()
	{
		// TODO Auto-generated method stub
		return "Coarse " + level;
	}

	@Override
	public int max() 
	{
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public int catalyst()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int rarity() 
	{
		// TODO Auto-generated method stub
		return level-1;
	}

	@Override
	public String type()
	{
		return "weapon";
	}
	@Override
	public int onHit(DungeonsPlayer player,DMob hit)
	{
		if (RandomFunctions.random(1, 10) != 1) return 0;
		return 4*level;
	}
	/*
	@Override
	public void onHitAfter(DungeonsPlayer player, DMob hit, ArmorStand ind)
	{
		if (RandomFunctions.random(1, 10) != 1) return;
		int damage = level*3;
		
		ind.setCustomName(Integer.toString(damage + hit.lastDamage));
		for (DMobAbility mab : hit.type.abilities) damage = mab.damaged(hit, player,damage);
		hit.damage(damage, player.player);
	}
	*/
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
