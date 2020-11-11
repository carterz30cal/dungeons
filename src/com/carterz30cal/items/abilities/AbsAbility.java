package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.carterz30cal.player.DungeonsPlayer;

public abstract class AbsAbility
{
	public static String prefix = ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Ability: " + ChatColor.RESET + ChatColor.LIGHT_PURPLE;
	
	public abstract ArrayList<String> description();
	
	// HOOKS
	public void onTick  (DungeonsPlayer d) {} // every tick
	public void onAttack(DungeonsPlayer d) {} // when the player attacks
	public boolean onDamage(DungeonsPlayer d, DamageCause c) { return false; } // when the player is hit
	public void onSneak (DungeonsPlayer d) {} // on sneaking
}
