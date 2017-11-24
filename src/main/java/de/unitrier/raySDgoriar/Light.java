package de.unitrier.raySDgoriar;

import java.awt.Color;

class Light {
   float red;
   float green;
   float blue;
   Vec3D position;
   Color color;
   public static final Light BACKGROUND_COLOR = new Light(0.05f, 0.05f, 0.05f);
   public static final Light AMBIENT_COLOR=new Light(0.01f,0.01f,0.01f);
   public static final Light BLACK = new Light(0.0f, 0.0f, 0.0f);
   
   Light(float r, float g, float b)
    { if (r>1) r=1; else if (r<0) r=0;
      if (g>1) g=1; else if (g<0) g=0;
      if (b>1) b=1; else if (b<0) b=0;
      red=r; green=g; blue=b;
    }
   
   Light(Vec3D pos, float r, float g, float b){
	   this(r,g,b);
	   position = pos;
   }
    
   Color getColor()
    { if (color!=null) return color;
      color=new Color((int) (red*255),(int) (green*255), (int) (blue*255));
      return color;
    }
   
   public static Light addColors(Light c1, Light c2, float ratio)
   { return new Light( (c1.red+c2.red*ratio),
             (c1.green+c2.green*ratio),
             (c1.blue+c2.blue*ratio));
    }

}