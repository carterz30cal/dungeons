package com.carterz30cal.items.abilities;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import com.carterz30cal.dungeons.DungeonManager;
import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.player.DungeonsPlayer;

import net.md_5.bungee.api.ChatColor;

public class AbilityStormfork extends AbsAbility
{

	@Override
	public ArrayList<String> description() {
		ArrayList<String> des = new ArrayList<String>();
		des.add(prefix + "Trident of the Storm");
		des.add("Strikes nearby mobs with lightning");
		des.add(" dealing 20% true damage to them");
		des.add(ChatColor.RED + "Only works during the rain");
		return des;
	}

	@Override
	public int onAttack(DungeonsPlayer d,DMob mob,int damage)
	{
		if (!(DungeonManager.i.hash(d.player.getLocation().getBlockZ()) == 1 && Dungeons.w.hasStorm())) return damage;
		ItemStack check = d.player.getInventory().getItemInMainHand();
		if (check != null && check.getType() == Material.TRIDENT) return damage;
		Collection<Entity> entities = mob.entities.get(0).getWorld().getNearbyEntities(mob.entities.get(0).getLocation(), 2, 2, 2);
		boolean strike = false;
		for (Entity e : entities)
		{
			if (e == mob.entities.get(0)) continue;
			if (DMobManager.mobs.containsKey(e.getUniqueId()))
			{
				strike = true;
				DMobManager.mobs.get(e.getUniqueId()).damage(damage / 5, d.player,true);
			}
		}
		if (strike) mob.entities.get(0).getWorld().strikeLightningEffect(mob.entities.get(0).getLocation());
		return damage;
	} 
}
