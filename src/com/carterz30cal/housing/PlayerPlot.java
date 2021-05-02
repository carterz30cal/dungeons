package com.carterz30cal.housing;

import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.entity.Villager;

public class PlayerPlot
{
	public UUID owner;
	public Chunk chunk;
	
	public boolean loaded;
	public Villager npc;
	
	
	
	/*
	 * PLAYER HOUSING
	 * players can purchase a plot once they can access necropolis. a tutorial will tell them that this is available.
	 * players CAN own upto 9 plots, but each is more expensive
	 * plot 1: 75,000 coins
	 * each subsequent plot doubles in price
	 * 2: 150,000
	 * 3: 300,000
	 * 4: 600,000
	 * 5: 1,200,000
	 * 6: 2,400,000
	 * 7: 4,800,000
	 * 8: 9,600,000
	 * 9: 19,200,000
	 * these plots are joined together and the bedrock walls will be removed.
	 * they form in this pattern
	 * 1:
	 * O
	 * 2:
	 * O O
	 * 3:
	 * O O O
	 * 4:
	 *   O
	 * O O O
	 * 5:
	 *   O
	 * O O O
	 *   O
	 * etc etc
	 * 
	 * players must also buy any blocks they want to use on the plot, and broken blocks are simply destroyed, not returned
	 * some blocks are cheap (e.g. dirt - 1 coin) and some are really expensive (diamond blocks - 1,000 per)
	 * 
	 * players can set up shops
	 */
	
}
