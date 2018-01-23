package jp.motlof.aw;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

import jp.motlof.aw.util.DetailsColor;
import jp.motlof.aw.util.Polar_coodinates;

public class ImageParticle extends BukkitRunnable {
	
	Location location;
	BufferedImage image;
	int w, h;
	double separate;
	
	public ImageParticle(File file, Location location, double separate) throws IOException {
		this.location = location.clone();
		this.separate = (separate <= 0) ? 0.2 : separate;
		image = ImageIO.read(file);
		w = image.getWidth();
		h = image.getHeight();
	}

	@Override
	public void run() {
		for(int x = 0; x < w; x++) {
			for(int y = 0; y < h; y++) {
				int pixel = image.getRGB(x, y);
				if(new Color(image.getRGB(x, y), true).getAlpha() == 0)
					continue;
				DetailsColor color = new DetailsColor((pixel >> 16) & 0xFF, (pixel >> 8) & 0xFF, pixel & 0xFF);
				Polar_coodinates pCoodinates = new Polar_coodinates(new Location(location.getWorld(), x*separate, y*separate, 0));
				location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(pCoodinates.rotation_Xaxis(Math.toRadians(180))), 0, color.getRed(), color.getGreen(), color.getBlue(), 1);
			}
		}
	}
}
