package com.carterz30cal.mobs.packet;

import com.carterz30cal.dungeons.Dungeons;

import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.EntitySpider;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_16_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_16_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_16_R3.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;

public class EntityTarantula extends EntitySpider implements CustomEntity
{
	
	public static net.minecraft.server.v1_16_R3.Entity spawn()
	{
		return new EntityTarantula(EntityTypes.SPIDER,((CraftWorld)Dungeons.w).getHandle());
	}

	public EntityTarantula(EntityTypes<? extends EntitySpider> entitytypes, World world) {
		super(entitytypes, world);
		// TODO Auto-generated constructor stub
	}
	
	@Override// <- not necessary but I like putting it here
	public void initPathfinder()
	{
		goalSelector.a(0, new PathfinderGoalFloat(this));
        goalSelector.a(2, new PathfinderGoalMeleeAttack(this, 1d,true));
        goalSelector.a(10, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
	}
	


}
