package jp.motlof.aw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

import jp.motlof.aw.util.DetailsColor;
import jp.motlof.aw.util.Polar_coodinates;

public class StringParticle extends BukkitRunnable {
	
	Location location;
	BufferedImage image;
	int w, h;
	double separate;
	
	public StringParticle(String string, Location location, double separate) {
		this.location = location;
		this.separate = separate;
		w = string.length()*16;
		h = 16;
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2d = image.createGraphics();
		graphics2d.setBackground(new Color(255, 255, 255, 0));
		graphics2d.drawString(string, 0, 14);
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
