package com.carterz30cal.items.abilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Zombie;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ability.AbsAbility;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.DMobManager;
import com.carterz30cal.mobs.DMobType;
import com.carterz30cal.mobs.packet.EntityTypes;
import com.carterz30cal.player.CharacterSkill;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.ParticleFunctions;

import net.md_5.bungee.api.ChatColor;

public class AbilityReaperBlade extends AbsAbility
{
	public static Map<DungeonsPlayer,List<Soul>> souls = new HashMap<>();
	@Override
	public ArrayList<String> description() {
		ArrayList<String> d = new ArrayList<>();
		d.add(prefix + "Soul Reaper");
		d.add("Kill enemies to harvest their souls!");
		d.add("These souls can be summoned using");
		d.add("right click and will fight with you.");
		d.add("Each soul alive will consume 1 mana");
		d.add("per second. You don't regen mana");
		d.add("whilst these souls are alive.");
		d.add(ChatColor.GREEN + "Souls deal damage depending on their level");
		d.add(ChatColor.BLUE + "Max of 5 souls.");
		return d;
	}

	public void onKill (DungeonsPlayer d,DMobType mob) 
	{
		List<Soul> alive = souls.getOrDefault(d, new ArrayList<>());
		if (alive.size() >= d.stats.maxsouls) return;
		
		int level = mob.level;
		for (AbsAbility ab : d.stats.abilities) if (ab instanceof AbilityReaperSet) level += 2;
		
		d.player.sendMessage(ChatColor.GREEN + "You captured a level " + CharacterSkill.prettyText(level) + ChatColor.GREEN + " soul!");
		alive.add(new Soul(d,level,mob.level == level));
		
		souls.put(d, alive);
	}
	
	public boolean onInteract(DungeonsPlayer d, PlayerInteractEvent e)
	{
		List<Soul> alive = souls.getOrDefault(d, new ArrayList<>());
		if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && alive.size() > 0)
		{
			int added = 0;
			for (Soul soul : alive) if (soul.summon()) added++;
			if (added > 0) d.player.sendMessage(ChatColor.GREEN + "Summoned " + added + " souls!");
			return added > 0;
		}
		return false;
	}
	
	public void onEnd(DungeonsPlayer d) 
	{
		if (souls.containsKey(d)) for (Soul soul : souls.get(d)) soul.remove();
	}; 
	
	public void onLogOut(DungeonsPlayer d)
	{
		if (souls.containsKey(d)) for (Soul soul : souls.get(d)) soul.remove();
	}; 

}

class Soul
{
	public int level;
	public int health;
	
	public Mob entity;
	public ArmorStand display;

	private DungeonsPlayer owner;
	private DMob target;
	
	public boolean summoned;
	private boolean decaying;
	
	public Soul soul = this;
	
	public Soul(DungeonsPlayer d,int l,boolean decay)
	{
		//entity = EntityTypes.SOUL.spawnEntity(d.player.getLocation());

		owner = d;
		
		level = l;
		health = 30*level;
		
		decaying = decay;
		summoned = false;
	}
	public boolean summon()
	{
		if (summoned) return false;
		summoned = true;
		
		entity = (Mob)EntityTypes.SOUL.spawnEntity(owner.player.getLocation());
		//entity = (Mob)Dungeons.w.spawnEntity(owner.player.getLocation(), EntityType.ZOMBIE);
		entity.setSilent(true);
		entity.setInvulnerable(true);
		entity.getPersistentDataContainer().set(DMobManager.arrowDamage, PersistentDataType.STRING, owner.player.getUniqueId().toString() + "," + level);
		entity.setCanPickupItems(false);
		entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(new AttributeModifier("walkspeed",0.2,Operation.MULTIPLY_SCALAR_1));
		
		entity.setInvisible(true);
		ItemStack[] equipment = new ItemStack[4];
		equipment[3] = new ItemStack(Material.CHAINMAIL_HELMET);
		equipment[2] = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
		equipment[1] = new ItemStack(Material.CHAINMAIL_LEGGINGS);
		equipment[0] = new ItemStack(Material.CHAINMAIL_BOOTS);
		
		entity.getEquipment().setArmorContents(equipment);
		
		display = (ArmorStand)Dungeons.w.spawnEntity(owner.player.getLocation(), EntityType.ARMOR_STAND);
		display.setSmall(true);
		display.setMarker(true);
		display.setInvisible(true);
		display.setCustomNameVisible(true);
		
		for (Entity e : entity.getPassengers()) e.remove();
		((Zombie)entity).setAdult();
		
		
		new BukkitRunnable()
		{
			int cooldown;
			@Override
			public void run() {
				if (!entity.isValid() || !owner.player.isOnline() || health < 1 || !owner.playerHasMana() || owner.getMana() == 0) 
				{
					cancel();
					remove();
					
					AbilityReaperBlade.souls.get(owner).remove(soul);
				}
				if (target == null || target.health < 1)
				{
					DMob closest = null;
					for (Entity en : Dungeons.w.getNearbyEntities(owner.player.getLocation(), 10, 2, 10))
					{
						DMob test = DMobManager.get(en);
						if (closest == null) closest = test;
						else if (test != null 
								&& test.entities.get(0).getLocation().distance(owner.player.getLocation())
								< closest.entities.get(0).getLocation().distance(owner.player.getLocation()))
						{
							closest = test;
							break;
						}
					}
					target = closest;
				}
				if (target != null) 
				{
					entity.setTarget((LivingEntity)target.entities.get(0));
					if (entity.getLocation().distance(target.entities.get(0).getLocation()) > 10) 
					{
						//ParticleFunctions.moving(entity.getLocation(), Particle.SMOKE_NORMAL, 7, 0.2);
						entity.teleport(target.entities.get(0));
					}
				}
				display.setCustomName(CharacterSkill.prettyText(level) + ChatColor.GREEN + " Soul " + health + "❤");
				display.teleport(entity.getLocation().add(0, entity.getHeight() + 0.1d,0));
				
				
				cooldown++;
				if (cooldown == 20)
				{
					owner.useMana(1);
					if (decaying) health--;
					cooldown = 0;
				}
			}
			
		}.runTaskTimer(Dungeons.instance, 1, 1);
		
		return true;
	}
	public void remove()
	{
		if (!summoned) return;
		display.remove();
		entity.setHealth(0);
	}
}
