package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;

import com.carterz30cal.items.magic.ProjectileParticle;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobType;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerStatBank;
import com.carterz30cal.player.DungeonsPlayerStats;

public abstract class AbsAbility
{
	public static String prefix = ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Ability: " + ChatColor.RESET + ChatColor.LIGHT_PURPLE;
	public static String rune = ChatColor.RED + "" + ChatColor.BOLD + "Rune: " + ChatColor.RESET + ChatColor.RED;
	public static String ritual = ChatColor.DARK_RED + "" + ChatColor.BOLD + "Ritual: " + ChatColor.RESET + ChatColor.DARK_RED;
	public static String modifier = ChatColor.BLUE + "" + ChatColor.BOLD + "Spell Modifier: " + ChatColor.RESET + ChatColor.BLUE;
	
	public abstract ArrayList<String> description();
	
	// HOOKS
	public void onTick  (DungeonsPlayer d) {} // every tick
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) {return damage;} // when the player attacks returns new damage
	public int onArrowLand(DungeonsPlayer d,DMob mob,int damage) { return damage;} // exclusively when an arrow hits a mob (or trident ig). onAttack() is still fired
	
	public void arrowOnGround(DungeonsPlayer d,Entity arrow) {};
	
	public void onKill (DungeonsPlayer d,DMobType mob) {}
	public void onKill2 (DungeonsPlayer d,DMob mob) {} // cause the other method sucks but i'm too lazy to fix every ability
	
	public void onHeal(DungeonsPlayer d,int amount) {}
	public double onDamage(DungeonsPlayer d,double dealt, DamageCause c,DMobType mob) { return 1; } // when the player is hit
	public boolean onInteract(DungeonsPlayer d, PlayerInteractEvent e) {return false;}
	public void onSneak (DungeonsPlayer d) {} // on sneaking
	public void onMagic(DungeonsPlayer d,ProjectileParticle p) {} // on fire wand
	public void onMagicHit(DungeonsPlayer d,DMob hit) {}
	public void onMove(DungeonsPlayer d) {}
	public void stats(DungeonsPlayerStats s) {}
	public void statbank(DungeonsPlayerStatBank s) {}
	public void finalStats(DungeonsPlayerStats s) {}
	public int onOverkill(DungeonsPlayer d,DMob mob,int overkillamount) {return 0;};
	public int magicCost(ItemMeta wand) {return 0;} //when testing cost
	public String onFishIn(DungeonsPlayer d,String current) { return current;}
	public void onEnd(DungeonsPlayer d) {}; // when server is closed.
	public void onLogOut(DungeonsPlayer d) {}; // run just before dungeonsplayer is saved.
	public boolean allowTarget(DungeonsPlayer d, DMob m) {return true;} // run when a mob decides to target this player
	
	
	public boolean isUnique() {return false;}
	public boolean isRitual() {return false;}
	
	protected String luxiumCost(int amount)
	{
		return ChatColor.YELLOW + Integer.toString(amount) + "✦" + ChatColor.GRAY;
	}
}
