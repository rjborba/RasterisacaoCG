import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLDrawableFactory;
import com.jogamp.opengl.GLProfile;

public class Main {
	static final int width = 500;
	static final int height = 500;
	
	static int mash = 20;
	
	static final int x1 = 0;
	static final int y1 = 0;
	static final int x2 = 10;
	static final int y2 = 15;
	
	public static void main( String [] args ) {
		Display display = new Display();
        final HomeScreen shell = new HomeScreen(display);
        shell.setText( "CG Rast - Borba" );
        shell.setLayout( new FillLayout() );
        shell.setSize(500, 500);

        Composite composite = new Composite(shell, SWT.NONE );
        composite.setLayout( new FillLayout() );

        GLData gldata = new GLData();
        gldata.doubleBuffer = true;
        // need SWT.NO_BACKGROUND to prevent SWT from clearing the window
        // at the wrong times (we use glClear for this instead)
        final GLCanvas glcanvas = new GLCanvas(composite, SWT.NO_BACKGROUND, gldata );
        glcanvas.setCurrent();
        GLProfile glprofile = GLProfile.getDefault();
        final GLContext glcontext = GLDrawableFactory.getFactory( glprofile ).createExternalGLContext();

        // fix the viewport when the user resizes the window
        glcanvas.addListener( SWT.Resize, new Listener() {
            public void handleEvent(Event event) {
                Rectangle rectangle = glcanvas.getClientArea();
                glcanvas.setCurrent();
                glcontext.makeCurrent();
                Drawer.setup(glcontext.getGL().getGL2(), width, height);
                glcontext.release();        
            }
        });

        // draw the triangle when the OS tells us that any part of the window needs drawing
        glcanvas.addPaintListener( new PaintListener() {
            public void paintControl( PaintEvent paintevent ) {
                Rectangle rectangle = glcanvas.getClientArea();
                glcanvas.setCurrent();
                glcontext.makeCurrent();
                
                Drawer.setup(glcontext.getGL().getGL2(), width, height);
                Drawer.drawMatix(glcontext.getGL().getGL2(), width, mash);
                
                Drawer.renderDda(glcontext.getGL().getGL2(), x1, y1, x2, y2, width, mash);
                Drawer.renderPm(glcontext.getGL().getGL2(), x1, y1, x2, y2, width, mash);
                Drawer.renderRt(glcontext.getGL().getGL2(), x1, y1, x2, y2, width, mash);
                
                Drawer.drawLine(glcontext.getGL().getGL2(), x1, y1, x2, y2, width, mash);
               
                glcanvas.swapBuffers();
                glcontext.release();        
            }
        });

        shell.open();

        while( !shell.isDisposed() ) {
            if( !display.readAndDispatch() )
                display.sleep();
        }

        glcanvas.dispose();
        display.dispose();
    }
        
}
