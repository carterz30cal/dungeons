package com.carterz30cal.packets;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.carterz30cal.dungeons.Dungeons;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.SoundCategory;

import net.minecraft.server.v1_16_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import net.minecraft.server.v1_16_R3.Scoreboard;
import net.minecraft.server.v1_16_R3.ScoreboardTeam;
import net.minecraft.server.v1_16_R3.ScoreboardTeamBase.EnumNameTagVisibility;

public class Packetz 
{
	public static ScoreboardTeam s;
	public static PacketPlayOutScoreboardTeam joiner;
	
	public static void init()
	{
		ProtocolManager m = ProtocolLibrary.getProtocolManager();
		
		m.addPacketListener(new PacketAdapter(Dungeons.instance,ListenerPriority.NORMAL,PacketType.Play.Server.NAMED_SOUND_EFFECT)
		{
			@Override
			public void onPacketSending(PacketEvent e)
			{
				for (SoundCategory s : e.getPacket().getSoundCategories().getValues()) if (s == SoundCategory.WEATHER) e.setCancelled(true);
			}
		}
		);
		
		
		
		s = new ScoreboardTeam(new Scoreboard(),"");
	    s.setNameTagVisibility(EnumNameTagVisibility.NEVER);
	    joiner = new PacketPlayOutScoreboardTeam(s,0);
	    
	    for (Player player : Bukkit.getOnlinePlayers())
	    {
	        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
	        connection.sendPacket(new PacketPlayOutScoreboardTeam(s,1));
	        connection.sendPacket(joiner);
	    }
	}
}
