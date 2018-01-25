package jp.motlof.aw.util.particles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import jp.motlof.aw.util.DetailsColor;
import jp.motlof.aw.util.ParticleAPI.EnumParticle;
import jp.motlof.aw.util.ParticleAPI.Particle;
import jp.motlof.aw.util.Polar_coodinates;

public class PixelArtParticle {
	
	Location location;
	BufferedImage image;
	int w, h;
	double separate;
	
	public PixelArtParticle(File file, Location location, double separate) throws IOException {
		this.location = location.clone();
		this.separate = (separate <= 0) ? 0.2 : separate;
		image = ImageIO.read(file);
		w = image.getWidth();
		h = image.getHeight();
	}
	
	public PixelArtParticle(String string, Location location, double separate) {
		this.location = location.clone();
		this.separate = (separate <= 0) ? 0.2 : separate;
		w = string.length()*16;
		h = count(string, "%n")*16;
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2d = image.createGraphics();
		graphics2d.setBackground(new Color(255, 255, 255, 0));
		graphics2d.setColor(Color.black);
		int i = 0;
		for(String string2 : string.split("%n"))
			graphics2d.drawString(string2.replaceAll("_", " "), 0, 14+(16*++i));
	}
	
	public void show() {
		for(int x = 0; x < w; x++) {
			for(int y = 0; y < h; y++) {
				int pixel = image.getRGB(x, y);
				if(new Color(image.getRGB(x, y), true).getAlpha() == 0)
					continue;
				DetailsColor color = new DetailsColor((pixel >> 16) & 0xFF, (pixel >> 8) & 0xFF, pixel & 0xFF);
				Polar_coodinates pCoodinates = new Polar_coodinates(new Location(location.getWorld(), x*separate, y*separate, 0)).add(0, 0, Math.toRadians(180));
				sendParticle(location.clone().add(pCoodinates.convertLocation()), color);
			}
		}
	}
	
	private int count(String string, String target) {
		int i = (string.length() - string.replaceAll(target, "").length() / target.length());
		return i <= 0 ? 1 : i; 
	}
	
	private void sendParticle(Location location, DetailsColor color) {
		for(Player player : Bukkit.getOnlinePlayers())
			if(player.getWorld().getName().equalsIgnoreCase(location.getWorld().getName()))
				new Particle(EnumParticle.REDSTONE, location, color.getRed(), color.getGreen(), color.getBlue(), 1, 0).sendParticle(player);
	}
	
}
