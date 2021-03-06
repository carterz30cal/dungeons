package com.carterz30cal.crypts;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.carterz30cal.player.DungeonsPlayer;
import com.carterz30cal.player.DungeonsPlayerManager;


public class CryptMap extends MapRenderer
{
	public boolean drawn;
	
	public CryptGenerator crypt;
	
	public int[] previous;
	int offsetx;
	int offsetz;
	@SuppressWarnings("deprecation")
	@Override
	public void render(MapView arg0, MapCanvas arg1, Player arg2)
	{
		DungeonsPlayer d = DungeonsPlayerManager.i.get(arg2);
		if (d == null || !d.inCrypt) arg0.removeRenderer(this);
		try
		{
			if (!drawn)
			{
				
				
				arg0.setCenterX(200);
				arg0.setCenterZ(200);
				
				crypt = d.crypt;
				offsetx = (128 - crypt.sizex) / 2;
				offsetz = (128 - crypt.sizez) / 2;
				
				for (int x = 0; x < crypt.sizex; x++)
				{
					for (int z = 0; z < crypt.sizez; z++)
					{
						arg1.setPixel(x+offsetx, z+offsetz, getColour(x,z));
					}
				}
				drawn = true;
				previous = adjustPos(arg2.getLocation().getBlockX(),arg2.getLocation().getBlockZ());
			}
			
			arg1.setPixel(previous[0]+offsetx, previous[1]+offsetz,getColour(previous[0],previous[1]));
			previous = adjustPos(arg2.getLocation().getBlockX(),arg2.getLocation().getBlockZ());
			arg1.setPixel(previous[0]+offsetx, previous[1]+offsetz,MapPalette.RED);
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			arg0.removeRenderer(this);
		}
		
	}
	@SuppressWarnings("deprecation")
	private byte getColour(int x, int z)
	{
		if (crypt.crypt[x][z] > 0) return MapPalette.WHITE;
		else return MapPalette.DARK_GRAY;
	}
	private int[] adjustPos(int x,int z)
	{
		return new int[] {x - crypt.lx,z - crypt.lz};
	}
}

