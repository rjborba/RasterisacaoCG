import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

public class Drawer {
	public static int[] firstDot;
	public static int[] lastDot;
	
	public static int width, height;
	
	public static void setup( GL2 gl2, int width_, int height_) {
		GL gl;
		
        gl2.glMatrixMode( GL2.GL_PROJECTION );
        gl2.glLoadIdentity();

        // coordinate system origin at lower left with width and height same as the window
        GLU glu = new GLU();
        glu.gluOrtho2D( 0.0f, width, 0.0f, height );

        gl2.glMatrixMode( GL2.GL_MODELVIEW );
        gl2.glLoadIdentity();
        gl2.getGL();

        gl2.glViewport( 0, 0, width, height );
        
        firstDot = new int[2];
        lastDot = new int[2];
        
        width = width_;
        height = height_;
        
    }
	
	public static void renderDda(GL2 gl2, int X1, int Y1, int X2, int Y2, int width, int mash) {
		int variation = Math.round(width/mash);
		int Length, I;
		float X,Y,Xinc,Yinc;
		
		gl2.glColor3f(0, 0, 1);
		
		Length = Math.abs(X2 - X1);
		if (Math.abs(Y2 -Y1) > Length){
			Length = Math.abs(Y2-Y1);
		}
		
		Xinc = (X2 - X1)/(float) Length;
		Yinc = (Y2 -Y1)/(float) Length;
		
		X = X1;
		Y = Y1;
		
		firstDot[0] = Math.round(X) * variation;
		firstDot[1] = Math.round(Y) * variation;
		while(X<=X2){
			lastDot[0] = Math.round(X) * variation;
			lastDot[1] = Math.round(Y) * variation;
			
			drawCircle(gl2, Math.round(X) * variation, Math.round(Y) * variation, Math.round(variation/3));
			X = X + Xinc;
			Y = Y + Yinc;	
		}
	}
	
	public static void renderPm(GL2 gl2, int X1, int Y1, int X2, int Y2, int width, int mash){
		int variation = Math.round(width/mash);
		
		int l = X2 - X1 ;
	    int a = Y2 - Y1 ;
	    int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
	    
	    gl2.glColor3f(0, 0, 0);
	    
	    if (l<0) 
	        dx1 = -1 ; 
	    else if (l>0) 
	        dx1 = 1 ;
	    if (a<0) 
	        dy1 = -1 ; 
	    else if (a>0) 
	        dy1 = 1 ;
	    if (l<0) 
	        dx2 = -1 ; 
	    else if (l>0) 
	        dx2 = 1 ;
	    
	    int distante = Math.abs(l) ;
	    int perto = Math.abs(a) ;
	    
	    if (!(distante>perto)) {
	        distante = Math.abs(a) ;
	        perto = Math.abs(l) ;
	        if (a<0)
	            dy2 = -1; 
	        else if (a>0) 
	            dy2 = 1 ;
	        dx2 = 0 ;            
	    }
	    
	    int numerator = distante >> 1 ;
	    
	    for (int i=0;i<=distante;i++) {
	    	drawCircle(gl2, Math.round(X1) * variation, Math.round(Y1) * variation, Math.round(variation/4));
	        numerator += perto ;
	        if (!(numerator<distante)) {
	            numerator -= distante ;
	            X1 += dx1 ;
	            Y1 += dy1 ;
	        } else {
	            X1 += dx2 ;
	            Y1 += dy2 ;
	        }
	    }
		
	}
	
	public static void renderRt(GL2 gl2, int X1, int Y1, int X2, int Y2, int width, int mash){
		int variation = Math.round(width/mash);
		
		int y, m, x, q;
				
		gl2.glColor3f(0, 1, 0);
		
		 if(X1 > X2){
	        int tmp1 = X1;
	        X1 = X2;
	        X2 = tmp1;
	        
	        tmp1 = Y1;
	        Y1 = Y2;
	        Y2 = tmp1;
	    }
	    float a = (Y2-Y1)/((float) (X2-X1));
	    float b = Y1 - a * X1;
	    if(X1 == X2) {
	        if(Y1>Y2){
	            int tmp = Y1;
	            Y1=Y2;
	            Y2=tmp;
	        }            
	        while(Y1 <= Y2) {
	        	drawCircle(gl2, X1 * variation,Y1 * variation, Math.round(variation/6));
	            Y1++;
	        }
	    }
	    else {
	        while(X1 <= X2){
	            Y1 = Math.round(a * X1 + b);
	            drawCircle(gl2, X1 * variation, Y1 * variation, Math.round(variation/6));
	            X1++;
	        }    
	    }
	}
	
	public static void drawMatix(GL2 gl2, int size, int malha){
		int y = 0;
		int x = 0;
		
		int variation = Math.round(size / malha);
		
		gl2.glColor3f(1, 1, 1);
	
		gl2.glBegin(GL.GL_POINTS);
		
		while(x < size){
			x = x + variation;
			while(y<size){
				//drawCircle(gl2, Math.round(X), Math.round(Y), 5);
				gl2.glVertex2f(Math.round(x), Math.round(y));
				y++;
			}
			y = 0;
		}
		
		x = 0;
		while(y < size){
			y = y + variation;
			while(x<size){
				//drawCircle(gl2, Math.round(X), Math.round(Y), 5);
				gl2.glVertex2f(Math.round(x), Math.round(y));
				x++;
			}
			x = 0;
		}
		gl2.glEnd();
	}
	
	public static void drawCircle(GL2 gl2, int centerX, int centerY, int radius) {
		float DEG2RAD = (float) (3.14159/180F);
		float degInRad;
		
		gl2.glBegin(GL.GL_TRIANGLE_FAN);
		 
		   for (int i=0; i < 360; i++)
		   {
		      degInRad = i*DEG2RAD;
		      gl2.glVertex2f((float) Math.cos(degInRad)*radius + centerX ,(float) Math.sin(degInRad)*radius + centerY);
		   }
		 
		 gl2.glEnd();
	}
	
	public static void drawLine(GL2 gl2, int x1, int x2, int y1, int y2, int width, int mash) {
		int variation = Math.round(width/mash);
		
		gl2.glBegin (GL2.GL_LINES);
		
		gl2.glColor3f(1, 0, 0);
		
		gl2.glVertex3f(x1 * variation, x2 * variation, 0);
		gl2.glVertex3f(y1 * variation, y2 * variation, 0);
		
		gl2.glEnd();
		   
	}
}



