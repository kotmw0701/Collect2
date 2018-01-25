package jp.motlof.aw.util;

import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.PacketPlayOutTileEntityData;

public class Test extends NMSBase {
	
	public static void show(BlockPosition position, NBTTagCompound compound) {
		@SuppressWarnings("unused")
		PacketPlayOutTileEntityData tileEntityData = new PacketPlayOutTileEntityData(position.up(), 1, compound);
		
	}
}
