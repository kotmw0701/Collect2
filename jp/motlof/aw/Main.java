package jp.motlof.aw;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.EulerAngle;

public class Main extends JavaPlugin {
	
	public static Main main;
	public static double radius = 0.15;
	
	private RotateCube cube;
	private ImageParticle imageParticle;
	private StringParticle stringParticle;
	
	@Override
	public void onEnable() {
		main = this;
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
			if(imageParticle != null) imageParticle.cancel();
			if(args[1] == null || !(new File("C:\\Server\\"+args[1]+".png").exists()))
				return false;
			try {
				imageParticle = new ImageParticle(new File("C:\\Server\\"+args[1]+".png"), player.getLocation(), Double.parseDouble(args[2]));
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			imageParticle.runTaskTimer(Main.main, 0, 1);
		}
		if(args.length == 3 && "string".equalsIgnoreCase(args[0])) {
			if(stringParticle != null) stringParticle.cancel();
			if(args[1] == null)
				return false;
			stringParticle = new StringParticle(args[1], player.getLocation(), Double.parseDouble(args[2]));
			stringParticle.runTaskTimer(Main.main, 0, 1);
			return true;
		}
		return false;
	}
	
	Location getNextLocation(Location base, double x, double y, double z) {
		Location location = base.clone();
		location.add(0, Math.cos(Math.toRadians(x+180))*radius, Math.sin(Math.toRadians(x+180))*radius);
		return location;
	}
}
