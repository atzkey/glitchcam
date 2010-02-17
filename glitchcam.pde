import processing.video.*;

int w = 640;
int h = 480;

int fps = 15;

Capture video;

PImage lastFrame;

void setup()
{
  size(w, h);
  
  video = new Capture(this, w, h, fps);
  lastFrame = new PImage(w, h);

  background(0);
}

void draw()
{
  while(video.available()) {
    video.read();
    video.loadPixels();
    lastFrame.loadPixels();
    
    int start1 = (int)random(w*h);
    int start2 = (int)random(w*h);
    int amount = (int)random(min(w*h, w*h - max(start1, start2)));
    
    for (int i = 0; i < amount; i++) {
      lastFrame.pixels[start2 + i] = video.pixels[start2 + i];
      lastFrame.pixels[start1 + i] = lastFrame.pixels[start1 + i] | video.pixels[start2 + i];  
    }

    
    lastFrame.updatePixels();
    filter(EXCLUSION);
    image(lastFrame, 0, 0);
  }
}

