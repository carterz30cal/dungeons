package com.carterz30cal.npcs;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.items.ItemBuilder;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_16_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import net.minecraft.server.v1_16_R3.PlayerInteractManager;
import net.minecraft.server.v1_16_R3.WorldServer;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;

public class NPC
{
	public NPCp npc;
	public Slime slime;
	
	public String shopId;
	public Location loc;
	
	public NPC(String name,String skinData,String skinSignature,Location location)
	{
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) Bukkit.getWorld("hub")).getHandle();
       
        PlayerInteractManager m = new PlayerInteractManager(world);
        GameProfile prof = new GameProfile(UUID.randomUUID(), name);
        prof.getProperties().put("textures", new Property("textures", skinData, skinSignature));

        loc = location.clone();
        location.setYaw(location.getYaw()+45);
        
        npc = new NPCp(server,world,prof,m);
        
        npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        
        //npc.setHeadRotation(((location.getYaw() * 256f) / 360f));
        
        
        shopId = "none";
    }
	public void spawnHitbox()
	{
		
		if (slime != null && slime.isValid()) return;
		
		Player p = npc.getBukkitEntity().getPlayer();
		slime = (Slime) p.getLocation().getWorld().spawnEntity(p.getLocation(), EntityType.SLIME);
		slime.setAI(false);
		slime.setGravity(false);
		slime.setSize(4);
		slime.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,1000000,0));
		slime.setInvulnerable(true);
		slime.getPersistentDataContainer().set(ItemBuilder.kItem, PersistentDataType.STRING, shopId);
	}
	public void removeHitbox()
	{
		if (slime == null) return;
		
		slime.remove();
		slime = null;
	}
	public void send(Player r)
	{
		PlayerConnection connection = ((CraftPlayer)r).getHandle().playerConnection;
		
		
		connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
        connection.sendPacket(new PacketPlayOutEntityHeadRotation(this.npc,(byte) ((loc.getYaw() * 256f) / 360f)));
        DataWatcher watcher = new DataWatcher(null);

        watcher.register(new DataWatcherObject<>(16, DataWatcherRegistry.a), (byte)127);
        connection.sendPacket(new PacketPlayOutEntityMetadata(npc.getId(), watcher, true));
        Bukkit.getScheduler().runTaskLater(Dungeons.instance, new Runnable() {
            
            @Override
            public void run() 
            {
                 connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, npc));
            }
         
        }, 50);
	}
	
	public void remove(Player r)
	{
		PlayerConnection connection = ((CraftPlayer)r).getHandle().playerConnection;
		
		connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, npc));
        connection.sendPacket(new PacketPlayOutEntityDestroy(npc.getId()));
	}
}
