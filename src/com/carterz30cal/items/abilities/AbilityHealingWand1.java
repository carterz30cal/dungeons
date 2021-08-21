package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.Sound;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.player.DungeonsPlayer;

import net.md_5.bungee.api.ChatColor;

public class AbilityHealingWand1 extends AbsAbility {

	@Override
	public ArrayList<String> description()
	{
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Healer");
		d.add("Right click to heal for");
		d.add("40 + 28% of your maximum mana");
		d.add(ChatColor.BLUE + "Consumes 60% of your mana");
		return d;
	}
	
	public boolean onInteract(DungeonsPlayer d, PlayerInteractEvent e) 
	{
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if (!d.useMana((int) (d.stats.mana * 0.6))) return false;
			int healing = (int) (40 + (d.stats.mana * 0.28));
			d.player.playSound(d.player.getLocation(),Sound.BLOCK_NOTE_BLOCK_PLING,0.65f,1.3f);
			d.player.sendMessage(ChatColor.GREEN + "You healed for " + healing + " health!");
			d.heal(healing);
		}
		return true;
	}

}
