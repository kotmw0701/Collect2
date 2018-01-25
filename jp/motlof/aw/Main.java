package jp.motlof.aw;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import jp.motlof.aw.util.particles.PixelArtParticle;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.TileEntity;
import net.minecraft.server.v1_12_R1.TileEntityEnchantTable;

public class Main extends JavaPlugin implements Listener{
	
	public static Main main;
	public static double radius = 0.15;
	
	private RotateCube cube;
	private BukkitRunnable iBukkitRunnable;
	private BukkitRunnable sBukkitRunnable;
	private WingParticle wingParticle;
	
	@Override
	public void onEnable() {
		main = this;
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable() {
		if(cube != null) cube.cancel();
	}
	
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if(!(s instanceof Player))
			return false;
		Player player = (Player) s;
		if(args.length < 1) {
			player.sendMessage("(´・ω・｀)引数置いてー？");
			return false;
		}
		if("rotate".equalsIgnoreCase(args[0])) {
			if(cube != null) cube.cancel();
			cube = new RotateCube(player.getLocation());
			cube.runTaskTimer(Main.main, 0, 1);
			return true;
		}
		if(args.length == 2 && "radius".equalsIgnoreCase(args[0])) {
			radius = Double.parseDouble(args[1]);
			return true;
		}
		if(args.length == 1 && "rotate2".equalsIgnoreCase(args[0])) {
			Location location = player.getLocation();
			for(int theta = 0; theta <= 360; theta += 10) {
				EulerAngle eulerAngle = RotateCube.getAngle(theta,0,0);
				ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(getNextLocation(new Location(player.getWorld(), location.getX(), location.getY()+1, location.getZ()), theta, 0, 0), EntityType.ARMOR_STAND);
				stand.setSmall(true);
				stand.setGravity(false);
				stand.setBasePlate(false);
				stand.setHelmet(new ItemStack(Material.STONE));
				stand.setHeadPose(eulerAngle);
			}
			return true;
		}
		if(args.length == 3 && "image".equalsIgnoreCase(args[0])) {
			if(iBukkitRunnable != null) iBukkitRunnable.cancel();
			if(args[1] == null || !(new File("C:\\Server\\"+args[1]+".png").exists()))
				return false;
			PixelArtParticle particle;
			try {
				particle = new PixelArtParticle(new File("C:\\Server\\"+args[1]+".png"), player.getLocation(), Double.parseDouble(args[2]));
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			iBukkitRunnable = new BukkitRunnable() {
				@Override
				public void run() {
					particle.show();
				}
			};
			iBukkitRunnable.runTaskTimer(Main.main, 0, 1);
		}
		if(args.length == 3 && "string".equalsIgnoreCase(args[0])) {
			if(sBukkitRunnable != null) sBukkitRunnable.cancel();
			if(args[1] == null)
				return false;
			PixelArtParticle stringParticle = new PixelArtParticle(args[1], player.getLocation(), Double.parseDouble(args[2]));
			sBukkitRunnable = new BukkitRunnable() {
				@Override
				public void run() {
					stringParticle.show();
				}
			};
			sBukkitRunnable.runTaskTimer(Main.main, 0, 1);
			return true;
		}
		if(args.length == 2 && "wing".equalsIgnoreCase(args[0])) {
			if(wingParticle != null) wingParticle.cancel();
			try {
				wingParticle = new WingParticle(player, Double.parseDouble(args[1]));
			} catch (IOException e) {
				e.printStackTrace();
			}
			wingParticle.runTaskTimer(Main.main, 0, 1);
		}
		return false;
	}
	
	Location getNextLocation(Location base, double x, double y, double z) {
		Location location = base.clone();
		location.add(0, Math.cos(Math.toRadians(x+180))*radius, Math.sin(Math.toRadians(x+180))*radius);
		return location;
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if(event.getBlock().getType() != Material.ENCHANTMENT_TABLE)
			return;
		Player player = event.getPlayer();
		Location location = event.getBlock().getLocation();
		CraftWorld world = (CraftWorld) player.getLocation().getWorld();
		TileEntity tileEntity = world.getTileEntityAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
		Bukkit.getScheduler().runTaskLater(this, () -> {
			NBTTagCompound tagCompound = tileEntity.d();
			tagCompound.setInt("x", location.getBlockX());
			tagCompound.setInt("y", location.getBlockY()+2);
			tagCompound.setInt("z", location.getBlockZ());
			tileEntity.load(tagCompound);
			TileEntityEnchantTable enchantTable = (TileEntityEnchantTable) tileEntity;
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(enchantTable.getUpdatePacket());
		}, 5);
	}
}
