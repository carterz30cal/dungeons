package com.carterz30cal.npcs;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.PlayerInteractManager;
import net.minecraft.server.v1_16_R3.WorldServer;

public class NPCp extends EntityPlayer {

	public NPCp(MinecraftServer minecraftserver, WorldServer worldserver, GameProfile gameprofile,
			PlayerInteractManager playerinteractmanager) 
	{
		
		super(minecraftserver, worldserver, gameprofile, playerinteractmanager);
		
		//DataWatcher watcher = getDataWatcher();
		//watcher.register(new DataWatcherObject<Byte>(13,DataWatcherRegistry.a),(byte)127);
	}

}
