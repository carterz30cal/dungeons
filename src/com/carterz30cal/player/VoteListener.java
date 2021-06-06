package com.carterz30cal.player;

import java.time.Instant;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.carterz30cal.dungeons.Dungeons;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

public class VoteListener implements Listener
{
	@SuppressWarnings("deprecation")
	@EventHandler
    public void onVotifierEvent(VotifierEvent event) {
        Vote vote = event.getVote();
    
        // right, so we get the username of the voter and then we get their UUID.
        // next, we attach the expiry date of the vote to the DungeonsPlayer data
        // if you vote, you get a 35% xp boost for 6 hours. there are 4 sites, so voting for all gives a 24 hour xp boost period
        // and by then, you can vote again.
        UUID voter = Bukkit.getOfflinePlayer(vote.getUsername()).getUniqueId();
        DungeonsPlayer online = DungeonsPlayerManager.i.players.get(voter);
        if (online == null)
        {
        	FileConfiguration playerc = Dungeons.instance.getPlayerConfig();
        	Instant time = Instant.ofEpochMilli(playerc.getLong(voter.toString() + ".voteboost", 0));
        	if (time.isBefore(Instant.now())) playerc.set(voter.toString() + ".voteboost", Instant.now().plusSeconds(60*60*6).toEpochMilli());
        	else playerc.set(voter.toString() + ".voteboost",time.plusSeconds(60*60*6).toEpochMilli());
        }
        else
        {
        	if (online.voteBoost == null || online.voteBoost.isBefore(Instant.now())) online.voteBoost = Instant.now().plusSeconds(60*60*6);
        	else online.voteBoost.plusSeconds(60*60*6);
        }
    }
}
