package com.carterz30cal.dungeons;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.areas.AbsDungeonEvent;
import com.carterz30cal.areas.EventTicker;
import com.carterz30cal.bosses.AliveBossHandler;
import com.carterz30cal.bosses.BossManager;
import com.carterz30cal.items.abilities.AbsAbility;
import com.carterz30cal.npcs.NPC;
import com.carterz30cal.npcs.NPCManager;
import com.carterz30cal.packets.Packetz;
import com.carterz30cal.player.CharacterSkill;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;
import com.carterz30cal.player.PlayerDelivery;
import com.carterz30cal.quests.TutorialManager;
import com.carterz30cal.quests.TutorialTrigger;

import net.minecraft.server.v1_16_R3.PlayerConnection;

public class ListenerPlayerJoin implements Listener
{
	
	public static Map<UUID,PlayerDelivery> deliveries = new HashMap<>();
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		DungeonsPlayerManager.i.create(e.getPlayer());
		
		DungeonsPlayer d = DungeonsPlayerManager.i.get(e.getPlayer());
		if (d.newaccount) e.setJoinMessage(e.getPlayer().getDisplayName() + ChatColor.DARK_PURPLE + " has joined for the first time! Make sure to say hi!");
		else e.setJoinMessage( e.getPlayer().getDisplayName() + ChatColor.AQUA + " has joined");
		
		for (NPC n : NPCManager.i.npcs)
		{
			n.send(e.getPlayer());
		}
		
		TutorialManager.fireEvent(d, TutorialTrigger.JOIN, "");
	    PlayerConnection connection = ((CraftPlayer) e.getPlayer()).getHandle().playerConnection;
	    e.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
	    e.getPlayer().teleport(DungeonManager.i.dungeons.get(1).spawn);
	    e.getPlayer().getInventory().setItemInOffHand(null);
	    
	    if (deliveries.containsKey(e.getPlayer().getUniqueId()))
	    {
	    	PlayerDelivery delivery = deliveries.get(e.getPlayer().getUniqueId());
	    	new BukkitRunnable()
	    	{

				@Override
				public void run() {
					delivery.deliver();
				}
	    		
	    	}.runTaskLater(Dungeons.instance, 20);
	    	
	    	deliveries.remove(e.getPlayer().getUniqueId());
	    }
	    
	    connection.sendPacket(Packetz.joiner);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		DungeonsPlayer d = DungeonsPlayerManager.i.get(e.getPlayer());
		for (AliveBossHandler boss : BossManager.bosses.values()) if (boss != null && boss.boss != null) boss.boss.remove(d);
		for (AbsDungeonEvent ev : EventTicker.events) ev.onPlayerDeath(d);
		e.getPlayer().closeInventory();
		
		for (AbsAbility a : d.stats.abilities) a.onLogOut(d);
		
		DungeonsPlayerManager.i.save(d);
		DungeonsPlayerManager.i.players.remove(e.getPlayer().getUniqueId());
		
		CharacterSkill.removeFromBoard(d.level);
		e.setQuitMessage( e.getPlayer().getDisplayName() + ChatColor.DARK_AQUA + " has left");
	}
}
