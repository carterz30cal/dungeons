package com.carterz30cal.items.abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.DamageType;
import com.carterz30cal.player.DungeonsPlayer;

import net.md_5.bungee.api.ChatColor;

public class AbilityHydraStar extends AbsAbility
{

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Magical Implosion");
		d.add("Deal your damage multiplied by a percentage of");
		d.add("your mana as true damage to all nearby mobs");
		d.add(ChatColor.BLUE + "Consumes 50 mana");
		return d;
	}

	public boolean onInteract(DungeonsPlayer d, PlayerInteractEvent e) 
	{
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if (!d.useMana(50)) return false;
			List<DMob> affected = new ArrayList<>();
			
			int finaldamage = (int) Math.round((double)d.stats.damage * ((double)d.stats.mana / 500d));
			for (Entity en : Dungeons.w.getNearbyEntities(d.player.getLocation(), 8, 8, 8))
			{
				DMob enm = DMobManager.get(en);
				if (enm != null && !affected.contains(enm))
				{
					enm.damage(finaldamage, d, DamageType.TRUE,false);
					affected.add(enm);
				}
			}
		}
		return false;
	}
}
