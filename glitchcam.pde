import processing.video.*;

int w = 320;
int h = 240;

int fps = 15;

float xoff = 0.0;

Capture video;
Glitch glitch;

void setup()
{
  size(w*2 + 20, h*2, P2D);
  
  video = new Capture(this, w, h, fps);

  colorMode(RGB, 255);
  background(0);
}

void draw()
{
  if(video.available()) {
    video.read();
    video.loadPixels();
    loadPixels();
    
    Glitch g = new Glitch(video);
    
    g.decompose();

    g._r.position = g._g.position = g._b.position = w*h/2;
    g._r.glitch(Glitch.MESS, 4);
    g._g.glitch(Glitch.MESS, 2);
    g._b.glitch(Glitch.MESS, 4);

    g.compose(g._r, g, g._b);
    g.glitch(Glitch.MIX, 4);
    //g.restore();
    image(g, 5, 200);
    image(video, w + 15, 200);
    //image(video, 0, 0);
  }
}


