package com.carterz30cal.enchants;

import org.bukkit.entity.ArmorStand;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStatBank;

import org.bukkit.ChatColor;

public class EnchExecution extends AbsEnchant {

	@Override
	public String description() {
		return "Instakills mobs with less than " + (20*level) + " health after attack";
	}

	@Override
	public String name() {
		return "Execution " + level;
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
	public String type()
	{
		return "weapon";
	}
	@Override
	public int rarity() {
		// TODO Auto-generated method stub
		return level-1;
	}

	@Override
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		return null;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		return null;
	}

	@Override
	public void onHitAfter(DungeonsPlayer player, DMob hit,ArmorStand ind)
	{
		if (hit.health < 20*level && hit.health > 0 && !hit.type.boss)
		{
			hit.health = 0;
			hit.destroy(player.player);
			ind.setCustomName(ChatColor.RED + "EXEC" + ChatColor.GOLD + "UTED");
		}
	}

}