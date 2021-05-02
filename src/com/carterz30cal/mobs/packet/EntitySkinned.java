package com.carterz30cal.mobs.packet;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.mobs.DMob;
import com.carterz30cal.mobs.SkinnedType;
import com.carterz30cal.packets.Packetz;
import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.utility.RandomFunctions;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;

import org.bukkit.attribute.AttributeModifier.Operation;

import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.EnumGamemode;
import net.minecraft.server.v1_16_R3.EnumItemSlot;
import net.minecraft.server.v1_16_R3.EnumProtocolDirection;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.NetworkManager;
import net.minecraft.server.v1_16_R3.PacketPlayOutAnimation;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_16_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_16_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import net.minecraft.server.v1_16_R3.PlayerInteractManager;
import net.minecraft.server.v1_16_R3.WorldServer;

public class EntitySkinned extends EntityPlayer 
{
	public static Map<Integer,EntitySkinned> alive;
	public Husk navigator;
	public DMob mob;
	
	public EntitySkinned(MinecraftServer minecraftserver, WorldServer worldserver, GameProfile gameprofile,
			PlayerInteractManager playerinteractmanager)
	{
		super(minecraftserver, worldserver, gameprofile, playerinteractmanager);
		
		if (alive == null) alive = new HashMap<>();
	}
	
	
	
	@Override 
	public void tick() 
	{
	    super.tick();
	    
	    //teleportAndSync(navigator.getLocation().getX(), navigator.getLocation().getY(), navigator.getLocation().getZ());
	    entityBaseTick();
	    
	    Bukkit.getServer().getPluginManager().unsubscribeFromPermission("bukkit.broadcast.user", getBukkitEntity());
	    
	    if (navigator != null)
	    {
	    	Location l = navigator.getLocation();
		    short cx = (short) (l.getX() - this.locX());
		    short cy = (short) (l.getY() - this.locY());
		    short cz = (short) (l.getZ() - this.locZ());
		    //System.out.println(cx + " - " + cy + " - " + cz);
		    PacketPlayOutRelEntityMoveLook p = new PacketPlayOutRelEntityMoveLook(this.getId(),cx,cy,cz,(byte) ((l.getYaw() * 256f) / 360f),(byte)l.getPitch(),true);
		    PacketPlayOutEntityHeadRotation r = new PacketPlayOutEntityHeadRotation(this,(byte) ((l.getYaw() * 256f) / 360f));
		    this.teleportAndSync(l.getX(), l.getY(), l.getZ());
		    for (Player player : Bukkit.getOnlinePlayers()) 
		    {
		    	PlayerConnection c = ((CraftPlayer) player).getHandle().playerConnection;
		    	c.sendPacket(p);
		    	c.sendPacket(r);
		    }
	    }
	    
	    if (navigator == null || !navigator.isValid()) kill();
	    else navigator.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,1,0,true));
	    if (noDamageTicks > 0) 
	    {
	        --noDamageTicks;
	    }
	}
	
	// DMOB INTERACTIONS HERE
	// THIS STUFF WILL BREAK PROBABLY DEFINITELY
	
	public void kill()
	{
		if (!alive.containsKey(this.getId())) return;
		navigator.remove();
		navigator = null;
		getBukkitEntity().setHealth(0);
		
		new BukkitRunnable()
		{

			@Override
			public void run() {
				getBukkitEntity().remove();
				
				PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(getId());
				for (Player k : Bukkit.getOnlinePlayers())
				{
					PlayerConnection c = ((CraftPlayer)k).getHandle().playerConnection;
					c.sendPacket(packet);
				}
			}
			
		}.runTaskLater(Dungeons.instance, 40);
		alive.remove(this.getId());
	}
	
	public void remove()
	{
		if (navigator != null) navigator.remove();
		getBukkitEntity().remove();
		alive.remove(this.getId());
	}
	
	
	
	public void attack(DungeonsPlayer p)
	{
		p.damage(mob.type.damage, false);
		
		
		CraftPlayer l = (CraftPlayer)p.player;
		PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus (l.getHandle(),(byte)2);
		PacketPlayOutAnimation anim = new PacketPlayOutAnimation (this,(byte)0);
		for (Player k : Bukkit.getOnlinePlayers())
		{
			PlayerConnection c = ((CraftPlayer)k).getHandle().playerConnection;
			c.sendPacket(packet);
			c.sendPacket(anim);
		}
		
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void dress(ItemStack[] e,ItemStack main,ItemStack off)
	{
		List<Pair<EnumItemSlot, net.minecraft.server.v1_16_R3.ItemStack>> equip = new ArrayList<>();
		
		equip.add(new Pair(EnumItemSlot.MAINHAND,CraftItemStack.asNMSCopy(main)));
		equip.add(new Pair(EnumItemSlot.OFFHAND,CraftItemStack.asNMSCopy(off)));
		
		equip.add(new Pair(EnumItemSlot.HEAD,CraftItemStack.asNMSCopy(e[3])));
		equip.add(new Pair(EnumItemSlot.CHEST,CraftItemStack.asNMSCopy(e[2])));
		equip.add(new Pair(EnumItemSlot.LEGS,CraftItemStack.asNMSCopy(e[1])));
		equip.add(new Pair(EnumItemSlot.FEET,CraftItemStack.asNMSCopy(e[0])));
		
		
		PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(this.getId(), equip);
		
		for (Player k : Bukkit.getOnlinePlayers())
		{
			PlayerConnection c = ((CraftPlayer)k).getHandle().playerConnection;
			c.sendPacket(packet);
		}
	}
	
	
	
	
	
	
	
	
	
	@Override
    public void playerTick() {
		super.playerTick();
		/*
		entityBaseTick();
	    
	    if (this.hurtTicks > 0) {
	        this.hurtTicks -= 1;
	    }
	    tickPotionEffects();
	    
	    this.lastYaw = this.yaw;
	    this.lastPitch = this.pitch;
	    */
    }
	public static EntitySkinned createNPC(World world, Location location,DMob mob) 
	{

	    MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
	    WorldServer nmsWorld = ((CraftWorld) world).getHandle();
	    UUID ur = UUID.randomUUID();
	    GameProfile profile = new GameProfile(ur, ur.toString().substring(0,15));
	    
	    SkinnedType t = (SkinnedType)mob.type;
	    profile.getProperties().put("textures", new Property("textures", t.skin, t.sig));
	    
	    PlayerInteractManager interactManager = new PlayerInteractManager(nmsWorld);
	    //interactManager.setGameMode(EnumGamemode.SURVIVAL);
	    EntitySkinned entityPlayer = new EntitySkinned(nmsServer, nmsWorld, profile, interactManager);
	    entityPlayer.playerConnection = new PlayerConnection(nmsServer, new DummyManager(EnumProtocolDirection.CLIENTBOUND), entityPlayer);

	    entityPlayer.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(),
	        location.getPitch());
	    
	    //
	    Socket socket = new Socket();
        NetworkManager conn = null;
        try {
            conn = new DummyManager(EnumProtocolDirection.CLIENTBOUND);
            entityPlayer.playerConnection = new PlayerConnection(nmsServer, conn, entityPlayer);
            conn.setPacketListener(entityPlayer.playerConnection);
            socket.close();
        } catch (IOException e) {
            // swallow
        }
        
        nmsWorld.addEntity(entityPlayer);
		DataWatcher watcher = new DataWatcher(null);

        watcher.register(new DataWatcherObject<>(16, DataWatcherRegistry.a), (byte)127);
        for (Player player : Bukkit.getOnlinePlayers())
	    {
	        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
	        connection.sendPacket(new PacketPlayOutEntityMetadata(entityPlayer.getId(), watcher, true));
	    }
        
        /*
	    new BukkitRunnable()
	    {

			@Override
			public void run() {
				nmsWorld.addEntity(entityPlayer);
				DataWatcher watcher = new DataWatcher(null);

		        watcher.register(new DataWatcherObject<>(16, DataWatcherRegistry.a), (byte)127);
		        for (Player player : Bukkit.getOnlinePlayers())
			    {
			        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
			        connection.sendPacket(new PacketPlayOutEntityMetadata(entityPlayer.getId(), watcher, true));
			    }
			}
	    	
	    }.runTaskLater(Dungeons.instance, RandomFunctions.random(1, 20));
	    */
	    //nmsWorld.addAllEntitiesSafely(entityPlayer, SpawnReason.CUSTOM);
	    

	    PacketPlayOutPlayerInfo playerInfoAdd = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer);
	    PacketPlayOutNamedEntitySpawn namedEntitySpawn = new PacketPlayOutNamedEntitySpawn(entityPlayer);
	    PacketPlayOutEntityHeadRotation headRotation = new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) ((location.getYaw() * 256f) / 360f));
	    PacketPlayOutPlayerInfo playerInfoRemove = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer);
	    
	    
        
	    
	    ArrayList<String> st = new ArrayList<String>();
	    st.add(entityPlayer.getName());
	    
	    entityPlayer.navigator = (Husk) Dungeons.w.spawnEntity(location, EntityType.HUSK);
	    entityPlayer.navigator.setBaby();
	    entityPlayer.navigator.getEquipment().clear();
	    entityPlayer.navigator.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(new AttributeModifier("walkspeed",0.75*mob.type.speed-1,Operation.MULTIPLY_SCALAR_1));
	    
	    entityPlayer.navigator.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,1000000,0,true));
	    entityPlayer.navigator.setSilent(true);
	    entityPlayer.navigator.setInvulnerable(true);
	    entityPlayer.navigator.getPersistentDataContainer().set(DMob.identifier, PersistentDataType.STRING, "NAV_" + entityPlayer.getId());
	    
	    PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(entityPlayer.navigator.getEntityId());
		
	    for (Player player : Bukkit.getOnlinePlayers())
	    {
	        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
	        connection.sendPacket(playerInfoAdd);
	        connection.sendPacket(namedEntitySpawn);
	        connection.sendPacket(headRotation);
	        //connection.sendPacket(playerInfoRemove);
	        
	        connection.sendPacket(new PacketPlayOutScoreboardTeam(Packetz.s,st,3));
	        connection.sendPacket(destroy);
	    }
	    
	    
	    new BukkitRunnable()
	    {

			@Override
			public void run()
			{
				for (Player player : Bukkit.getOnlinePlayers())
			    {
			        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
			        connection.sendPacket(playerInfoRemove);
			    }
			}
	    	
	    }.runTaskLater(Dungeons.instance, 15); 
	    
	    
	    
	    alive.put(entityPlayer.getId(), entityPlayer);
	    
	    
	    
	    
	    entityPlayer.mob = mob;
	    return entityPlayer;
	}

}
