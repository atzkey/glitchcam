/* TODO: documentation */
/* TODO: force myself to read something about javadoc */
import processing.core.PImage;
import java.util.Random;

class Glitch extends PImage {
  
  // glitch types
  static final int MESS = 0;
  static final int MIX = 1;
  static final int DECOLOUR = 2;
  static final int EXTRACT = 3;

  private final int RED = 16;
  private final int GREEN = 8;
  private final int BLUE = 0;
  private final int ALPHA = 32;
  
  
  private PImage orig;
  
  public Glitch _r, _g, _b;
  private boolean decomposed = false;
  
  void decompose() {
    decompose(false);
  }
  
  // Decomposing. See +component+ description
  void decompose(boolean force_refresh) {
    if(force_refresh || !decomposed) {
      _r = new Glitch(orig);
      _g = new Glitch(orig);
      _b = new Glitch(orig);
      _r.component(RED);
      _g.component(GREEN);
      _b.component(BLUE);
      decomposed = true;
    }
  }


  /**
   * In general case this routine is pretty useless;
   * but when the shift equals 16, 8 or 0 it replaces current image with grayscaled
   * Red, Green or Blue component respectively.
   * 
   */
  void component(int shift)
  {
    for (int i = 0; i < pixels.length; i++) {
            int col = (pixels[i] >> shift) & 0xFF;
            pixels[i] = 0xff000000 | (col << 16) | (col << 8) | col;
          }
  }
  
  /* Composes decomposed image back, taking Red, Green and Blue color information
   * from r, g and b arguments respectively.
   * Cool side effects can be achieved if you pass random image as one of the components.
   */
  public void compose(PImage r, PImage g, PImage b) {
    // throw if size mismatch
    r.loadPixels(); g.loadPixels(); b.loadPixels();
    for(int i = 0; i < pixels.length; i++) {
      pixels[i] = (r.pixels[i] & 0x00FF0000) | 
                  (g.pixels[i] & 0x0000FF00) | 
                  (b.pixels[i] & 0x000000FF) |
                  (orig.pixels[i] & 0xFF000000); // taking alpha from orig
    }
    updatePixels();
  }
  
  // TODO: Should be reconsidered
  public int position;
  
  
  // Constructor
  public Glitch(PImage img) {
    orig = img;
    
    init(img.width, img.height, ARGB);
    loadPixels();
    System.arraycopy(img.pixels, 0, pixels, 0, img.pixels.length);
    updatePixels();
  }
  
  // Glitchhub
  public void glitch(int kind) {
    loadPixels();
    
    switch(kind) {
      case MESS:
        pixelMess(1);
        break;
      case MIX:
        pixelMix(1);
        break;
      case DECOLOUR:
        colourMess();
        break;
    }
    
    updatePixels();
  }


  // Glitchhub
  public void glitch(int kind, float param) {
    loadPixels();
    
    int intParam = (int) param;
    
    switch(kind) {
      case MESS:
        if (intParam < 1) {
          throw new RuntimeException("If you want to mess the pixels, " + 
                                     "do it at least once");
        }
        pixelMess(intParam);
        break;
      case MIX:
        if (intParam < 1) {
          throw new RuntimeException("If you want to mix the pixels, " + 
                                     "do it at least once");
        }
  
        pixelMix(intParam);
        break;

    }
    
    updatePixels();
  }

  // Messes current Glitch pixels around
  protected void pixelMess(int times) {
    for(int i = 0; i < times; i++) {
      int wh = pixels.length - position;
      int start1 = (int)random(wh);
      int start2 = (int)random(wh);
      int amount = (int)random(Math.min(wh, wh - Math.max(start1, start2)));
      System.arraycopy(pixels, start1 + position, pixels, start2 + position, amount);
    }

  }

  // Replaces current Glitch pixels with corresponding ones from orig PImage
  protected void pixelMix(int times) {
    for(int i = 0; i < times; i++) {
      int wh = pixels.length - position;
      int start = (int)random(wh);
      int amount = (int)random(Math.min(wh, wh - start));
      System.arraycopy(orig.pixels, start + position, pixels, start + position, amount);
    }
  }

  /* TODO: optimize! */
  protected void colourMess() {
    // Shifts the h and s of hsb by 'position' pixels to the left
    //for(int i = 0; i < pixels.length - position; i++) {
      // pixels[i] = Color.HSBtoRGB(hue(orig.pixels[i + position]), saturation(orig.pixels[i + position]), brightness(pixels[i]));
      //pixels[i] = Color.HSBtoRGB(hue(pixels[i]), saturation(orig.pixels[i + position]), brightness(pixels[i]));
      // pixels[i] = Color.HSBtoRGB(hue(orig.pixels[i + position]), saturation(pixels[i]), brightness(pixels[i]));
    //}
    
//     for(int i = 0; i < pixels.length - position; i++) {
//        pixels[i] |= (orig.pixels[i + position] & 0xFFFF0000);
//        pixels[i + position] |= (orig.pixels[i] & 0xFF0000FF);
//      }

    for(int j = position/width; j < height-1; j++) {
      int shift = j;
      for(int i = j*width; i < (j+1)*width; i++) {
        pixels[i] |= (orig.pixels[i + shift] & 0xFFFF0000);
        //pixels[i] |= (orig.pixels[i + shift/2] & 0xFF0000FF);
        //pixels[i] |= (orig.pixels[i + shift/3] & 0xFF00FF00);
      }
    }
  }

  // Just restores pixels, taking them from original image
  public void restore()
  {
    System.arraycopy(orig.pixels, 0, pixels, 0, pixels.length);
  }

  // Generates random int
  private int random(int howbig) {
    return (int) (Math.random() * howbig);
  }
}
