package com.carterz30cal.items.abilities;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.meta.ItemMeta;

import com.carterz30cal.items.magic.ProjectileParticle;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobType;
import com.carterz30cal.player.DungeonsPlayer;

public abstract class AbsAbility
{
	public static String prefix = ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Ability: " + ChatColor.RESET + ChatColor.LIGHT_PURPLE;
	public static String modifier = ChatColor.RED + "Modifier: " + ChatColor.WHITE;
	public abstract ArrayList<String> description();
	
	// HOOKS
	public void onTick  (DungeonsPlayer d) {} // every tick
	public int onAttack(DungeonsPlayer d,DMob dMob,int damage) {return damage;} // when the player attacks returns new damage
	public void onKill (DungeonsPlayer d,DMobType mob) {}
	public double onDamage(DungeonsPlayer d,double dealt, DamageCause c,DMobType mob) { return 1; } // when the player is hit
	public void onSneak (DungeonsPlayer d) {} // on sneaking
	public void onMagic(DungeonsPlayer d,ProjectileParticle p) {} // on fire wand
	public void onMagicHit(DungeonsPlayer d,DMob hit) {}
	public int magicCost(ItemMeta wand) {return 0;} //when testing cost
}
