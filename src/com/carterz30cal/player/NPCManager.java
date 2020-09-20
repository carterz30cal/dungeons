package com.carterz30cal.player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_16_R2.CraftServer;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ShopManager;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R2.EntityPlayer;
import net.minecraft.server.v1_16_R2.MinecraftServer;
import net.minecraft.server.v1_16_R2.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_16_R2.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_16_R2.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_16_R2.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_16_R2.PlayerConnection;
import net.minecraft.server.v1_16_R2.PlayerInteractManager;
import net.minecraft.server.v1_16_R2.WorldServer;

public class NPCManager
{
	public static NPCManager i;
	public static HashMap<Chunk,ArrayList<Slime>> reps;
	private ArrayList<EntityPlayer> npcs;
	
	public NPCManager()
	{
		i = this;
		
		npcs = new ArrayList<EntityPlayer>();
		
		File npcFile = new File(Dungeons.instance.getDataFolder(), "npcs.yml");
		if (!npcFile.exists())
		{
			npcFile.getParentFile().mkdirs();
			Dungeons.instance.saveResource("npcs.yml",false);
		}
		
		FileConfiguration npc = new YamlConfiguration();
		try 
		{
			npc.load(npcFile);
        } 
		catch (IOException | InvalidConfigurationException e) 
		{
            e.printStackTrace();
        }
		
		for (String n : npc.getKeys(false))
		{
			String name = ChatColor.GOLD + npc.getString(n + ".name", "null");
			String[] l = npc.getString(n + ".position", "0,0,0,0,0").split(",");
			Location location = new Location(null, d(l[0]),d(l[1]),d(l[2]), f(l[3]), f(l[4]));
			String data = npc.getString(n + ".skin.data", "null");
			String signature = npc.getString(n + ".skin.sig", "null");
			spawnNPC(name,data,signature,location).getBukkitEntity().getPlayer();
			
			if (npc.contains(n + ".shop"))
			{
				Slime rep = (Slime) Bukkit.getWorld("hub").spawnEntity(location, EntityType.SLIME);
				rep.setAI(false);
				rep.setGravity(false);
				rep.setSize(4);
				rep.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,100000,0));
				rep.setInvulnerable(true);
				rep.setRemoveWhenFarAway(false);
				//rep.setVisible(false);
				ShopManager.positions.put(rep, ShopManager.shops.get(npc.getString(n + ".shop")));
			}
		}
	}
	private double d(String i)
	{
		return Double.parseDouble(i);
	}
	private float f(String i)
	{
		return Float.parseFloat(i);
	}
	public static void sendNPCs(Player receiver)
	{
		PlayerConnection connection = ((CraftPlayer)receiver).getHandle().playerConnection;
		for (EntityPlayer npc : i.npcs)
		{
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
            Bukkit.getScheduler().runTaskLater(Dungeons.instance, new Runnable() {
                
                @Override
                public void run() 
                {
                     connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, npc));
                }
             
            }, 50);
		}
	}
	//public static void sendNPC(Player receiver, )
	public static void removeNPCs(Player receiver)
	{
		PlayerConnection connection = ((CraftPlayer)receiver).getHandle().playerConnection;
		for (EntityPlayer npc : i.npcs)
		{
			connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, npc));
            connection.sendPacket(new PacketPlayOutEntityDestroy(npc.getId()));
		}
	}
	public EntityPlayer spawnNPC(String name,String skinData,String skinSignature,Location location)
	{
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
       
        PlayerInteractManager m = new PlayerInteractManager(world);
        GameProfile prof = new GameProfile(UUID.randomUUID(), name);
        prof.getProperties().put("textures", new Property("textures", skinData, skinSignature));
        EntityPlayer npc = new EntityPlayer(server,world,prof,m);

        npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        
        npcs.add(npc);
        
        for (Player receiver : Bukkit.getOnlinePlayers())
        {
        	PlayerConnection connection = ((CraftPlayer)receiver).getHandle().playerConnection;
        	connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
            Bukkit.getScheduler().runTaskLater(Dungeons.instance, new Runnable() {
                
                @Override
                public void run() 
                {
                     connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, npc));
                }
             
            }, 50);
        }
        return npc;
    }
}
