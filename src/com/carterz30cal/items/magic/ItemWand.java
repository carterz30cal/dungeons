package com.carterz30cal.items.magic;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.Item;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.items.ability.AbilityManager;
import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.player.DungeonsPlayer;

import net.md_5.bungee.api.ChatColor;

public class ItemWand extends Item
{
	public static final NamespacedKey kSpell = new NamespacedKey(Dungeons.instance,"spell");
	public static final NamespacedKey kModifier = new NamespacedKey(Dungeons.instance,"modifier");
	
	public int mana;
	public int damage;
	
	public int cost(ItemStack wand)
	{
		return cost(wand.getItemMeta());
	}
	public int cost(ItemMeta wand)
	{
		ItemSpell spell = (ItemSpell)ItemBuilder.get(wand,kSpell);
		AbsAbility mod = getMod(wand);
		int cost = 0;
		if (spell == null) cost = mana;
		else if (mod == null) cost = mana+spell.mana;
		else cost = mana+spell.mana+mod.magicCost(wand);
		
		return Math.max(1, cost);
	}
	public int costNoAbs(ItemMeta wand)
	{
		ItemSpell spell = (ItemSpell)ItemBuilder.get(wand,kSpell);
		int cost = 0;
		if (spell == null) cost = mana;
		else cost = mana+spell.mana;

		return Math.max(1, cost);
	}
	public AbsAbility getMod(ItemMeta wand)
	{
		Item mod = ItemBuilder.get(wand,kModifier);
		if (mod == null) return null;
		return AbilityManager.get((String) mod.data.get("ability"));
	}
	public void use(DungeonsPlayer player,ItemStack wand)
	{	
		ItemSpell spell = (ItemSpell)ItemBuilder.get(wand,kSpell);
		if (spell == null) return;
		if (player.useMana(cost(wand)))
		{
			new ProjectileParticle(player,this,(ItemSpell)ItemBuilder.get(wand,kSpell),getMod(wand.getItemMeta()));
		}
		else
		{
			player.player.sendMessage(ChatColor.RED + "Not enough mana!");
		}
	}
}
