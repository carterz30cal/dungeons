package com.carterz30cal.enchants;

import org.bukkit.entity.ArmorStand;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStatBank;

public abstract class AbsEnchant
{
	public int level;
	
	// Returns a description of the enchantments effects
	public abstract String description();
	public abstract String name();
	// Enchantment data
	public abstract int max();
	public abstract int catalyst();
	public abstract int rarity();
	public abstract String type();
	// hooks that enchantments can use
	public abstract DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank);
	public abstract DungeonMiningTable onMine(DungeonMiningTable mine);
	public double setRareOreMultiplier(DungeonsPlayer player,double chance)
	{
		return chance;
	}
	public boolean hide()
	{
		return false;
	}
	public int onHit(DungeonsPlayer player,DMob hit)
	{
		return 0;
	}
	public void afterHit(DungeonsPlayer player,DMob hit)
	{
		
	}
	public void onDamaged(DungeonsPlayer player,DMob damager)
	{
		
	}
	public void onHitAfter(DungeonsPlayer player,DMob hit,ArmorStand ind) {}
	public void onKill(DungeonsPlayer player,DMob kill)
	{
		
	}
	public DungeonsPlayerStatBank onFinalBank(DungeonsPlayerStatBank bank)
	{
		return null;
	}
}
