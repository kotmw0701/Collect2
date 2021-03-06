package jp.motlof.aw;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import jp.kotmw.core.Polar_coordinate;
import jp.kotmw.core.api.particle.EnumParticle;
import jp.kotmw.core.api.particle.Particle;
import jp.kotmw.core.nms.DetailsColor;
import jp.kotmw.core.nms.DetailsColor.DetailsColorType;

public class WingParticle extends BukkitRunnable {
	
	Player player;
	BufferedImage image, image2;
	int w, h, w2, h2;
	double separate, rotation, height;
	
	public WingParticle(Player player, double separate) throws IOException {
		this.player = player;
		this.separate = (separate <= 0) ? 0.2 : separate;
		image = ImageIO.read(new File("C:\\Server\\wing2.png"));
		image2 = ImageIO.read(new File("C:\\Server\\wing3.png"));
		w = image.getWidth();
		h = image.getHeight();
		w2 = image2.getWidth();
		h2 = image2.getHeight();
	}

	@Override
	public void run() {
		DetailsColor color = DetailsColorType.WoolColor_WHITE.getColor();
		Location location = player.getLocation();
		for(int i = 0; i < 2; i++) {
			for(int x = 0; x < w; x++) {
				for(int y = 0; y < h; y++) {
					if(new Color(image.getRGB(x, y), true).getAlpha() == 0)
						continue;
					Polar_coordinate pCoodinates = new Polar_coordinate(new Location(location.getWorld(), x*separate, y*separate, 0)).add(0, 0, Math.toRadians(180));
					sendParticle(location.clone().add(pCoodinates.rotation_Yaxis(Math.toRadians(-30-location.getYaw()))).add(0, h*separate, 0), color);
					if(i == 1) {
						sendParticle(location.clone().add(pCoodinates.rotation_Yaxis(Math.toRadians(-150-location.getYaw()))).add(0, h*separate, 0), color);
					}
				}
			}
		}
		for(int i = 0; i < 2; i++) {
			for(int x = 0; x < w2; x++) {
				for(int y = 0; y < h2; y++) {
					if(new Color(image2.getRGB(x, y), true).getAlpha() == 0)
						continue;
					Polar_coordinate pCoodinates = new Polar_coordinate(new Location(location.getWorld(), x*separate, y*separate, 0)).add(0, 0, Math.toRadians(150));
					sendParticle(location.clone().add(pCoodinates.rotation_Yaxis(Math.toRadians(-60-location.getYaw()))).add(0, 2.5, 0), color);
					if(i == 1) {
						sendParticle(location.clone().add(pCoodinates.rotation_Yaxis(Math.toRadians(-120-location.getYaw()))).add(0, 2.5, 0), color);
					}
				}
			}
		}
	}
	
	private void sendParticle(Location location, DetailsColor color) {
		for(Player player : Bukkit.getOnlinePlayers())
			if(player.getWorld().getName().equalsIgnoreCase(location.getWorld().getName()))
				new Particle(EnumParticle.REDSTONE, location, color.getRed(), color.getGreen(), color.getBlue(), 1).sendParticle(player);
	}
}
