package jp.motlof.aw;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import jp.kotmw.core.nms.DetailsColor;
import jp.kotmw.core.nms.DetailsColor.DetailsColorType;

public class RotateCube extends BukkitRunnable {

	private ArmorStand stand;
	private Location location;
	private double x, y, z;
	
	public RotateCube(Location location) {
		this.location = new Location(location.getWorld(), location.getX(), location.getY()+1, location.getZ());
		ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(this.location, EntityType.ARMOR_STAND);
		stand.setHelmet(new ItemStack(Material.STONE));
		stand.setGravity(false);
		stand.setSmall(true);
		stand.setBasePlate(false);
		this.stand = stand;
	}
	
	@Override
	public void run() {
		if(stand.isDead()) {
			this.cancel();
			return;
		}
		DetailsColor color = DetailsColorType.WoolColor_BLACK.getColor();
		if(x >= 360) x = 0;
		if(y >= 360) y = 0;
		if(z >= 360) z = 0;
		stand.setHeadPose(getAngle(x, y, z));
		Location location = this.location.clone();
		location.add(0, Math.cos(Math.toRadians(x+180))*Main.radius, Math.sin(Math.toRadians(x+180))*Main.radius);
		stand.teleport(location);
		location.add(0, Math.cos(Math.toRadians(x))*Main.radius, Math.sin(Math.toRadians(x))*Main.radius).add(0, 1, 0);
		location.getWorld().spawnParticle(Particle.REDSTONE, location, 0, (double)color.getRed(), (double)color.getGreen(), (double)color.getBlue(), 1);
		x++;
	}
	
	static EulerAngle getAngle(double x, double y, double z) {
		return new EulerAngle(Math.toRadians(x), Math.toRadians(y), Math.toRadians(z));
	}
}
