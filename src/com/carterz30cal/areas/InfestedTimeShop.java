package com.carterz30cal.areas;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Stray;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.carterz30cal.dungeons.DungeonManager;
import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.gui.ShopGUI;
import com.carterz30cal.items.ShopManager;
import com.carterz30cal.utility.ArmourstandFunctions;

public class InfestedTimeShop extends AbsDungeonEvent 
{
	public static Stray npc;
	public static ArmorStand display;
	public static ArmorStand display2;
	
	public static int tick = 0;
	public boolean alive;
	// appears every 30 minutes for 5 minutes
	
	
	public void end()
	{
		if (npc != null) npc.remove();
		if (display != null) display.remove();
		if (display2 != null) display2.remove();
	}
	
	public void spawnAlive()
	{
		if (npc != null) npc.remove();
		if (display != null) display.remove();
		if (display2 != null) display2.remove();
		
		Location location = new Location(Dungeons.w,1,113,22994,-120,0);
		npc = (Stray)location.getWorld().spawnEntity(location, EntityType.STRAY);

		npc.setAI(false);
		npc.setSilent(true);
		npc.setInvulnerable(true);
		npc.setRemoveWhenFarAway(false);
		npc.getEquipment().clear();
		npc.getEquipment().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
		
		
		display = ArmourstandFunctions.create(location.clone().add(0,2.2,0));
		display.setMarker(true);
		display.setCustomName(ChatColor.GOLD + "Infrequent Merchant");
		display2 = ArmourstandFunctions.create(location.clone().add(0,1.9,0));
		display2.setCustomName(ChatColor.BLUE + "" +  ChatColor.BOLD + "Shop");
		display2.setMarker(true);
	}
	
	public void tick()
	{
		tick++;
		if (alive && npc == null && DungeonManager.i.warps.get("infestedcaverns").players.size() > 0)
		{
			spawnAlive();
		}
		else if ((alive && npc != null && DungeonManager.i.warps.get("infestedcaverns").players.size() == 0) || (!alive && npc != null))
		{
			npc.remove();
			display.remove();
			display2.remove();
			npc = null;
		}
		if (!alive && tick >= 36000) 
		{
			alive = true;
			tick = 0;
		}
		else if (alive && tick >= 6000)
		{
			alive = false;
			tick = 0;
		}
		
	}
	
	public boolean eventInteract(PlayerInteractEvent e)
	{
		if (!alive) return false;
		if (e.getPlayer().getLocation().distance(new Location(Dungeons.w,1,113,22994,-120,0)) < 5)
		{
			new ShopGUI(ShopManager.shops.get("infrequent"),e.getPlayer());
			return true;
		}
		return false;
	}
}
