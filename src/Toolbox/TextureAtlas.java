package Toolbox;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TextureAtlas {
	
	public static final String atlasFolder = "resources/res/TestAtlas/";
	public static int width = 128; //each face is 16 pixels this gives us 50 x 50 textures
	public static int height = 128;
	
	public static void main(String[] args) {
		String[] images = {"resources/res/TestAtlas/dirt.png", "resources/res/TestAtlas/grass_block_side.png", "resources/res/TestAtlas/grass_block_top.png"};
		createAtlas(images);
		
	}
	
	public static void createAtlas(String[] images) {
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = result.getGraphics();
		int x = 0, y = 0;
		try {
			for(String image : images) {
				File test = new File(image);
				
				BufferedImage bi = ImageIO.read(test);
				g.drawImage(bi, x, y, null);
				x += bi.getWidth(); //16 x 16 on each image
				if(x > width) {
					x = 0;
					y += bi.getHeight();
				}
				
			}
			
			ImageIO.write(result,"png", new File(atlasFolder + "textureAtlas1.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}

	}
	
}
