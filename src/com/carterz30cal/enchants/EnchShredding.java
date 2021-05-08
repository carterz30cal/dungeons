package com.carterz30cal.enchants;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStatBank;
import com.carterz30cal.utility.InventoryHandler;
import com.carterz30cal.utility.RandomFunctions;

public class EnchShredding extends AbsEnchant
{

	@Override
	public String description() 
	{
		return "+" + (3*level) + " damage. Gain 1 tissue on kill sometimes.";
	}

	@Override
	public String name()
	{
		return "Shredding " + level;
	}

	@Override
	public int max() {
		// TODO Auto-generated method stub
		return 5;
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
	public String type() {
		// TODO Auto-generated method stub
		return "weapon";
	}
	public void onKill(DungeonsPlayer player,DMob kill)
	{
		if (RandomFunctions.random(1, 5) != 1) return;
		InventoryHandler.addItem(player, ItemBuilder.i.build("tissue", 1), true);
	}
	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) 
	{
		bank.damage += level*3;
		return bank;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		// TODO Auto-generated method stub
		return null;
	}

}
