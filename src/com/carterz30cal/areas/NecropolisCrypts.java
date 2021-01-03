package com.carterz30cal.areas;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import com.carterz30cal.dungeons.DungeonManager;
import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ItemBuilder;
import com.carterz30cal.utility.ArmourstandFunctions;

import net.md_5.bungee.api.ChatColor;

public class NecropolisCrypts extends AbsDungeonEvent
{
	public ArmorStand display_name;
	public ArmorStand display_description;
	public ArmorStand display_warning;
	
	public boolean open;
	public int openticks;
	public NecropolisCrypts()
	{
		super();
	}
	
	@Override
	public void tick()
	{
		if (!open)
		{
			if (display_name == null || !display_name.isValid()) 
			{
				display_name = ArmourstandFunctions.create(new Location(Dungeons.w,29.7, 101.4, 22019.5));
				display_description = ArmourstandFunctions.create(new Location(Dungeons.w,29.7, 101, 22019.5));
				display_warning = ArmourstandFunctions.create(new Location(Dungeons.w,29.7, 99.5, 22019.5));
			}
			display_name.setCustomName(ChatColor.GOLD + "" +  ChatColor.BOLD + "CRYPTS");
			display_description.setCustomName(ChatColor.GOLD + "Insert a key to begin!");
			display_warning.setCustomName(ChatColor.RED + "Difficult!");
			
			openticks = 0;
		}
		else
		{
			if (display_name.isValid())
			{
				display_name.remove();
				display_description.remove();
				display_warning.remove();
			}
			openticks++;
		}
		
	}
	@Override
	public boolean eventInteract(PlayerInteractEvent e)
	{
		if (e.getAction() != Action.RIGHT_CLICK_AIR) return false;
		if (!e.hasItem() || e.getItem() == null) return false;
		
		String it = e.getItem().getItemMeta().getPersistentDataContainer().getOrDefault(ItemBuilder.kItem, PersistentDataType.STRING,"");
		if (it.equals("crypt_key") && DungeonManager.i.hash(e.getPlayer().getLocation().getBlockZ()) == 2
				&& e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.LODESTONE)
		{
			if (open) e.getPlayer().sendMessage(ChatColor.RED + "Crypt is still open");
			else
			{
				e.getItem().setAmount(e.getItem().getAmount()-1);
				open = true;
			}
		}
		return false;
	}
	@Override
	public void end()
	{
		display_name.remove();
		display_description.remove();
		display_warning.remove();
	}
}
