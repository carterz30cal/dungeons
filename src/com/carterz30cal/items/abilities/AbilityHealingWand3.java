package com.carterz30cal.items.abilities;

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

public class AbilityHealingWand3 extends AbsAbility {

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Healer");
		d.add("Right click to heal for");
		d.add("70 + 52% of your maximum mana");
		d.add("Also heals nearby players");
		d.add("for 33% of that.");
		d.add(ChatColor.BLUE + "Consumes 50% of your mana");
		return d;
	}
	
	public boolean onInteract(DungeonsPlayer d, PlayerInteractEvent e) 
	{
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if (!d.useMana((int) (d.stats.mana * 0.50))) return false;
			int healing = (int) (70 + (d.stats.mana * 0.52));
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
					du.heal(healing/3);
					totalheal += healing/3;
				}
			}
			if (totalheal > 0) d.player.sendMessage(ChatColor.GREEN + "You healed players around you by " + totalheal + "!");
		}
		return true;
	}

}
