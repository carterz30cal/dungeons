package com.carterz30cal.enchants;

import org.bukkit.entity.ArmorStand;

import com.carterz30cal.dungeons.DungeonMiningTable;
import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DungeonMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStatBank;

import net.md_5.bungee.api.ChatColor;

public class EnchShocking extends AbsEnchant {

	@Override
	public String description() {
		return "Strikes boss mobs for " + (5*level) +  "% of their max hp on first hit!";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Shocking " + level;
	}

	@Override
	public int max() {
		// TODO Auto-generated method stub
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
	public DungeonsPlayerStatBank onBank(DungeonsPlayerStatBank bank) {
		// TODO Auto-generated method stub
		return bank;
	}

	@Override
	public DungeonMiningTable onMine(DungeonMiningTable mine) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onHitAfter(DungeonsPlayer player, DMob hit, ArmorStand ind) {
		// TODO Auto-generated method stub
		if (!hit.type.boss) return;
		else if (hit.health+player.stats.damage == hit.health())
		{
			int damage = (int)Math.round(hit.health * (0.05 * level));
			Dungeons.w.strikeLightningEffect(hit.entities.get(0).getLocation());
			ind.setCustomName(ChatColor.BLUE + Integer.toString(damage + hit.lastDamage));
			hit.damage(damage, player.player);
		}
	}

}
