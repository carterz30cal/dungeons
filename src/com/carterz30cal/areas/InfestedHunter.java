package com.carterz30cal.areas;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.player.PlayerInteractEvent;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.gui.MonsterHunterGUI;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.SpawnPosition;
import com.carterz30cal.mobs.abilities.DMobAbility;
import com.carterz30cal.mobs.abilities.MobOwned;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.RandomFunctions;
import com.carterz30cal.utility.Square;

public class InfestedHunter extends AbsDungeonEvent 
{
	public static Map<DungeonsPlayer,DMob> active = new HashMap<>();
	public static Zombie npc;
	public static ArmorStand display;
	public static ArmorStand display2;
	
	public static final Square[] spawns = 
		{
				new Square(38,23003,46,23013,106)
		};
	
	@Override
	public void onPlayerDeath(DungeonsPlayer died)
	{
		DMob m = active.get(died);
		if (m != null) 
		{
			m.remove();
			active.remove(died);
			MobOwned.owner.remove(m);
			died.player.sendMessage(ChatColor.RED + "You failed to kill the tarantula!");
		}
	}
	
	public void end()
	{
		if (npc != null) npc.remove();
		if (display != null) display.remove();
		if (display2 != null) display2.remove();
	}
	public void tick()
	{
		//68.7,112,22987.7,133,0
		if (npc == null || !npc.isValid())
		{
			if (npc != null) npc.remove();
			if (display != null) display.remove();
			if (display2 != null) display2.remove();
			
			Location location = new Location(Dungeons.w,68.5,112,22985.5,82,0);
			npc = (Zombie)location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
			npc.setAdult();
			
			Location l = new Location(Dungeons.w,location.getX(),npc.getEyeLocation().getY() + 0.4, location.getZ(),location.getYaw(),location.getPitch());
			Location l2 = new Location(Dungeons.w,location.getX(),npc.getEyeLocation().getY() + 0.1, location.getZ(),location.getYaw(),location.getPitch());
			display = (ArmorStand)l.getWorld().spawnEntity(l, EntityType.ARMOR_STAND);
			display2 = (ArmorStand)l.getWorld().spawnEntity(l2, EntityType.ARMOR_STAND);
			npc.setAI(false);
			npc.setSilent(true);
			npc.setInvulnerable(true);
			npc.setRemoveWhenFarAway(false);
			npc.getEquipment().clear();
			
			display.setInvulnerable(true);
			display.setVisible(false);
			display.setGravity(false);
			display.setCustomName(ChatColor.GOLD + "Hunter Sophia");
			display.setCustomNameVisible(true);
			display.setRemoveWhenFarAway(false);
			display.setMarker(true);
			display2.setInvulnerable(true);
			display2.setVisible(false);
			display2.setGravity(false);
			display2.setCustomName(ChatColor.BLUE + "" +  ChatColor.BOLD + "Hunting");
			display2.setCustomNameVisible(true);
			display2.setRemoveWhenFarAway(false);
			display2.setMarker(true);
		}
	}
	
	public boolean eventInteract(PlayerInteractEvent e)
	{
		if (e.getPlayer().getLocation().distance(new Location(Dungeons.w,68.7,112,22987.7,133,0)) < 4)
		{
			new MonsterHunterGUI(e.getPlayer());
			return true;
		}
		return false;
	}
	
	public static void summon(DungeonsPlayer d,String type)
	{
		DMob boss = DMobManager.spawn(type, new SpawnPosition(RandomFunctions.get(spawns).getRandom()));
		
		
		for (DMobAbility a : DMobManager.types.get(type).abilities)
		{
			if (a instanceof MobOwned) 
			{
				if (d.coins < ((MobOwned)a).cost)
				{
					boss.remove();
					return;
				}
				d.coins -= ((MobOwned)a).cost;
			}
		}
		
		MobOwned.owner.put(boss, d);
		active.put(d, boss);
	}
}
