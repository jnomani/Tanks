import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;


public class ImageUtils{
	private ImageUtils(){
		//Disables the instantiation of the class.
	}
	
	/*
	 * Awt and fx implemented to a significant level in removing alpha bits from images.
	 * Standard native methods apply.
	 */
	public static BufferedImage makeTransparent(BufferedImage img){
		int color = img.getRGB(0, 0);
		Image image = makeColorTransparent(img, new Color(color));
		BufferedImage transparent = imageToBufferedImage(image);
		return transparent;
	}
	
	private static BufferedImage imageToBufferedImage(Image image) {

    	BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
    	Graphics2D g2 = bufferedImage.createGraphics();
    	g2.drawImage(image, 0, 0, null);
    	g2.dispose();

    	return bufferedImage;

    }
	
	private static Image makeColorTransparent(BufferedImage im, final Color color) {
    	ImageFilter filter = new RGBImageFilter() {

    		
    		public int markerRGB = color.getRGB() | 0xFF000000;

    		public final int filterRGB(int x, int y, int rgb) {
    			if ((rgb | 0xFF000000) == markerRGB) {
    				
    				return 0x00FFFFFF & rgb;
    			} else {
    				
    				return rgb;
    			}
    		}
    	};

    	ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
    	return Toolkit.getDefaultToolkit().createImage(ip);
    }
}
