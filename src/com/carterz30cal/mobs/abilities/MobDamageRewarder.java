package com.carterz30cal.mobs.abilities;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.InventoryHandler;

import net.md_5.bungee.api.ChatColor;

public class MobDamageRewarder extends DMobAbility
{
	public HashMap<DMob,HashMap<DungeonsPlayer,Integer>> damages;
	public String reward;
	public int threshold;
	
	
	public MobDamageRewarder(FileConfiguration data, String path)
	{
		super(data, path);
		
		damages = new HashMap<DMob,HashMap<DungeonsPlayer,Integer>>();
		reward = data.getString(path + ".reward", "BEDROCK");
		threshold = data.getInt(path + ".threshold", 1);
	}
	@Override
	public void add(DMob mob)
	{
		damages.put(mob, new HashMap<DungeonsPlayer,Integer>());
	}
	@Override
	public int damaged(DMob mob,DungeonsPlayer player,int damage)
	{
		if (!damages.containsKey(mob)) return damage;
		int current = damages.get(mob).getOrDefault(player, 0);
		current += mob.lastDamage;
		damages.get(mob).put(player, current);
		
		return damage;
	}
	@Override
	public void killed(DMob mob)
	{
		ItemStack itemReward = ItemBuilder.i.build(reward, 1);
		for (Entry<DungeonsPlayer,Integer> entry : damages.get(mob).entrySet())
		{
			if (entry.getValue() >= threshold)
			{
				DungeonsPlayer reci = entry.getKey();
				reci.player.sendMessage(ChatColor.AQUA + "You dealt " + entry.getValue() + 
						" damage to " + mob.type.name + " and received a " + 
						itemReward.getItemMeta().getDisplayName() + ChatColor.RESET + "" + ChatColor.AQUA + "!");
				InventoryHandler.addItem(reci, itemReward.clone());
			}
		}
		damages.remove(mob);
	}
}
