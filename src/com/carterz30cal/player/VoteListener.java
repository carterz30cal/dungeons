package com.carterz30cal.player;

import java.time.Instant;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.carterz30cal.dungeons.Dungeons;
import com.carterz30cal.utility.Stats;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

import net.md_5.bungee.api.ChatColor;

public class VoteListener implements Listener
{
	@SuppressWarnings("deprecation")
	@EventHandler
    public void onVotifierEvent(VotifierEvent event) {
        Vote vote = event.getVote();
    
        // right, so we get the username of the voter and then we get their UUID.
        // next, we attach the expiry date of the vote to the DungeonsPlayer data
        // if you vote, you get a 35% xp boost for 24 hours. there are 1 SITES BECAUSE IM NOT OBNOXIOUS
        // and by then, you can vote again.
        UUID voter = Bukkit.getOfflinePlayer(vote.getUsername()).getUniqueId();
        DungeonsPlayer online = DungeonsPlayerManager.i.players.get(voter);
        if (online == null)
        {
        	FileConfiguration playerc = Dungeons.instance.getPlayerConfig();

        	playerc.set(voter.toString() + ".voteboost", Instant.now().plusSeconds(60*60*24).toEpochMilli());
        }
        else
        {
        	online.voteBoost = Instant.now().plusSeconds(60*60*24);
        	
        	Bukkit.broadcastMessage(ChatColor.GOLD + online.player.getName() + " voted for the server! Everyone gets 1000 coins :)");
        	for (DungeonsPlayer d : DungeonsPlayerManager.i.players.values()) d.coins += 1000;
        	Stats.servervotes++;
        }
    }
}
