package com.carterz30cal.enchants;

import org.bukkit.entity.ArmorStand;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.mobs.DungeonMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public class EnchMidasTouch extends AbsEnchant
{

	@Override
	public String description()
	{
		if (level == 1) return "Grants an extra coin every kill";
		else return "Grants " + level + " coins every kill";
	}
	@Override
	public String name()
	{
		return "Midas Touch " + level; 
	}
	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank)
	{
		bank.killcoins += level;
		return bank;
	}

	@Override
	public int max()
	{
		return 5;
	}
	@Override
	public String type()
	{
		return "weapon";
	}
	@Override
	public int catalyst()
	{
		return 0;
	}
	@Override
	public int rarity()
	{
		return level / 2;
	}
	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		// TODO Auto-generated method stub
		return null;
	}

}
