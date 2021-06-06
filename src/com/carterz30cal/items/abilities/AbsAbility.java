package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;

import com.carterz30cal.items.magic.ProjectileParticle;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobType;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStats;

public abstract class AbsAbility
{
	public static String prefix = ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Ability: " + ChatColor.RESET + ChatColor.LIGHT_PURPLE;
	public static String rune = ChatColor.RED + "" + ChatColor.BOLD + "Rune: " + ChatColor.RESET + ChatColor.RED;
	public static String modifier = ChatColor.RED + "Modifier: " + ChatColor.WHITE;
	public abstract ArrayList<String> description();
	
	// HOOKS
	public void onTick  (DungeonsPlayer d) {} // every tick
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) {return damage;} // when the player attacks returns new damage
	public int onArrowLand(DungeonsPlayer d,DMob mob,int damage) { return damage;} // exclusively when an arrow hits a mob (or trident ig). onAttack() is still fired
	public void onKill (DungeonsPlayer d,DMobType mob) {}
	public double onDamage(DungeonsPlayer d,double dealt, DamageCause c,DMobType mob) { return 1; } // when the player is hit
	public boolean onInteract(DungeonsPlayer d, PlayerInteractEvent e) {return false;}
	public void onSneak (DungeonsPlayer d) {} // on sneaking
	public void onMagic(DungeonsPlayer d,ProjectileParticle p) {} // on fire wand
	public void onMagicHit(DungeonsPlayer d,DMob hit) {}
	public void onMove(DungeonsPlayer d) {}
	public void stats(DungeonsPlayerStats s) {}
	public void finalStats(DungeonsPlayerStats s) {}
	public int magicCost(ItemMeta wand) {return 0;} //when testing cost
	public String onFishIn(DungeonsPlayer d,String current) { return current;}
	public void onEnd(DungeonsPlayer d) {}; // when server is closed.
	public void onLogOut(DungeonsPlayer d) {}; // run just before dungeonsplayer is saved.
	public boolean allowTarget(DungeonsPlayer d, DMob m) {return true;} // run when a mob decides to target this player
	
	
	public boolean isUnique() {return false;}
}
