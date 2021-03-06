package com.carterz30cal.quests;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;

import net.md_5.bungee.api.ChatColor;

public class QuestNpc extends BukkitRunnable
{
	public static NamespacedKey kQuest = new NamespacedKey(Dungeons.instance,"kQuest");
	public Location location; // location
	public EntityType type;
	public String name;
	
	public Sound speak;
	public LivingEntity questgiver;
	public ArmorStand display;
	public ArmorStand display2;
	
	public ArrayList<Quest> quests;
	
	
	public QuestNpc(Location questLocation,EntityType t,String nam,Sound sp)
	{
		location = questLocation;
		type = t;
		name = nam;
		
		speak = sp;
		quests = new ArrayList<Quest>();
		
		Quest.quests.put(name, this);
		spawn();
		
		runTaskTimer(Dungeons.instance,1,1);
	}
	@Override
	public void run() {
		if (!questgiver.isValid()) spawn();
		
	}

	private void spawn()
	{
		if (questgiver != null) questgiver.remove();
		if (display != null) display.remove();
		if (display2 != null) display2.remove();
		
		questgiver = (LivingEntity)location.getWorld().spawnEntity(location, type);
		
		Location l = new Location(Dungeons.w,location.getX(),questgiver.getEyeLocation().getY() + 0.4, location.getZ(),location.getYaw(),location.getPitch());
		Location l2 = new Location(Dungeons.w,location.getX(),questgiver.getEyeLocation().getY() + 0.1, location.getZ(),location.getYaw(),location.getPitch());
		display = (ArmorStand)l.getWorld().spawnEntity(l, EntityType.ARMOR_STAND);
		display2 = (ArmorStand)l.getWorld().spawnEntity(l2, EntityType.ARMOR_STAND);
		questgiver.setAI(false);
		questgiver.setSilent(true);
		questgiver.setInvulnerable(true);
		questgiver.setRemoveWhenFarAway(false);
		questgiver.getEquipment().clear();
		questgiver.getPersistentDataContainer().set(kQuest, PersistentDataType.STRING, name);
		if (questgiver instanceof Ageable) ((Ageable)questgiver).setAdult();
		display.setInvulnerable(true);
		display.setVisible(false);
		display.setGravity(false);
		display.setCustomName(ChatColor.GOLD + name);
		display.setCustomNameVisible(true);
		display.setRemoveWhenFarAway(false);
		display.setMarker(true);
		display2.setInvulnerable(true);
		display2.setVisible(false);
		display2.setGravity(false);
		display2.setCustomName(ChatColor.BLUE + "" +  ChatColor.BOLD + "Quest");
		display2.setCustomNameVisible(true);
		display2.setRemoveWhenFarAway(false);
		display2.setMarker(true);
	}
}
