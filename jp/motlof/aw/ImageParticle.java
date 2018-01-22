package jp.motlof.aw;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

import jp.motlof.aw.util.DetailsColor;

public class ImageParticle extends BukkitRunnable {
	
	Location location;
	BufferedImage image;
	int w, h;
	
	public ImageParticle(File file, Location location) throws IOException {
		this.location = location.clone();
		image = ImageIO.read(file);
		w = image.getWidth();
		h = image.getHeight();
	}

	@Override
	public void run() {
		for(int y = 0; y < h; y++) {
			for(int x = 0; x < w; x++) {
				int pixel = image.getRGB(x, y);
				DetailsColor color = new DetailsColor((pixel >> 16) & 0xFF, (pixel >> 8) & 0xFF, pixel & 0xFF);
				location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(x*0.1, y*0.1, 0), 0, color.getRed(), color.getGreen(), color.getBlue(), 1);
			}
		}
	}
}
