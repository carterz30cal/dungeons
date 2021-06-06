package com.carterz30cal.mobs.packet;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;

import net.minecraft.server.v1_16_R3.Entity;


public enum EntityTypes
{
    //NAME("Entity name", Entity ID, yourcustomclass.class);
    TARANTULA("Tarantula", EntityTarantula.class), //You can add as many as you want.
	BULL("Bull",EntityBull.class),
	SOUL("Soul",EntitySoul.class);

	
	private Class<? extends Entity> custom;
	private String name;
    private EntityTypes(String name, Class<? extends Entity> custom)
    {
    	this.custom = custom;
    	this.name = name;
    }
    
    public Class<? extends Entity> getEntityClass()
    {
    	System.out.println(name);
    	return custom;
    }
    
    public org.bukkit.entity.Entity spawnEntity(Location loc)
    {
    	Entity en = null;

    	switch (name)
    	{
    	case "Tarantula":
    		en = EntityTarantula.spawn();
    		break;
    	case "Bull":
    		en = EntityBull.spawn();
    		break;
    	case "Soul":
    		en = EntitySoul.spawn();
    		break;
    	}
    	if (en == null) return null;
    	en.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    	((CraftWorld)loc.getWorld()).getHandle().addEntity(en);
    	return (org.bukkit.entity.Entity)en.getBukkitEntity();
    }

}