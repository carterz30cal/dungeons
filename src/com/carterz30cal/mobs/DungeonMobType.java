package com.carterz30cal.mobs;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.dungeons.SoundTask;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;

public class DungeonMobType
{
	public EntityType type;
	public String name;
	public String perk;
	
	public int maxHealth;
	public int damage;
	public int armour;
	public int xp;
	public double knockbackResist;
	public boolean baby;
	
	public ItemStack main;
	public ItemStack offhand;
	public ItemStack[] wearing;
	
	public ArrayList<MobDrop> drops;
	public ArrayList<String> tags;
	public MobAction[] actions;
	private Random r;
	
	public DungeonMobType()
	{
		r = new Random();
	}
	public void onKilled(Player killer,MobModifier modifier)
	{
		DungeonsPlayer d = DungeonsPlayerManager.i.get(killer);
		
		int pl = d.perks.getLevel(perk);
		d.perks.add(perk);
		if (d.perks.getLevel(perk) > pl)
		{
			new SoundTask(killer.getLocation(),killer,Sound.BLOCK_GLASS_BREAK,2f,1).runTaskLater(Dungeons.instance,20);
		}
		if (d.skills.add("combat", xp)) d.skills.sendLevelMessage("combat", killer);
		
		int coinreward = (maxHealth/20)+d.stats.bonuskillcoins;
		if (modifier != null) coinreward += 5;
		d.coins += coinreward;
		for (MobDrop drop : drops)
		{
			if (r.nextDouble() <= drop.chance)
			{
				ItemStack item = ItemBuilder.i.build(drop.item, d);
				item.setAmount(r.nextInt((drop.maxAmount+1)-drop.minAmount)+drop.minAmount);
				if (killer.getInventory().firstEmpty() == -1) killer.getWorld().dropItem(killer.getLocation(), item);
				else killer.getInventory().addItem(item);
				
				new SoundTask(killer.getLocation(),killer,Sound.ENTITY_ITEM_PICKUP,1,1).runTaskLater(Dungeons.instance, 3);
				if (drop.chance*100 < 1) killer.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "RARE DROP! " + ChatColor.RESET + item.getItemMeta().getDisplayName());
			}
		}
	}
}
