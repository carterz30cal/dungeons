package com.carterz30cal.items.abilities.infested;

import java.util.ArrayList;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

import net.md_5.bungee.api.ChatColor;

public class AbilityHealingWand5 extends AbsAbility {

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Sunray Heal");
		d.add("Right click to heal for");
		d.add("105 + 57% of your maximum mana");
		d.add("Also heals nearby players");
		d.add("for half of that.");
		d.add("Consumes " + ChatColor.LIGHT_PURPLE + "30% mana" + ChatColor.GRAY + " and " + luxiumCost(2));
		return d;
	}
	
	public boolean onInteract(DungeonsPlayer d, PlayerInteractEvent e) 
	{
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if (!d.canUseLuxium(2)) return false;
			if (!d.useMana((int) (d.stats.mana * 0.3))) return false;
			d.useLuxium(2);
			int healing = (int) (105 + (d.stats.mana * 0.57));
			d.player.playSound(d.player.getLocation(),Sound.BLOCK_NOTE_BLOCK_PLING,0.65f,1.3f);
			d.player.sendMessage(ChatColor.GREEN + "You healed for " + healing + " health!");
			d.heal(healing);
			
			int totalheal = 0;
			for (Entity en : Dungeons.w.getNearbyEntities(d.player.getLocation(),10,10,10))
			{
				if (!(en instanceof Player)) continue;
				DungeonsPlayer du = DungeonsPlayerManager.i.get((Player)en);
				if (du != null && du != d) 
				{
					du.heal(healing/2);
					totalheal += healing/2;
				}
			}
			if (totalheal > 0) d.player.sendMessage(ChatColor.GREEN + "You healed players around you by " + totalheal + "!");
		}
		return true;
	}

}
