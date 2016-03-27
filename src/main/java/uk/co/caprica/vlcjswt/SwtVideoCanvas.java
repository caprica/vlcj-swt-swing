package uk.co.caprica.vlcjswt;

import java.awt.Canvas;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Extension of an AWT Canvas that integrates SWT.
 * <p>
 * Most of this is boiler-plate.
 */
public class SwtVideoCanvas extends Canvas {

    private Thread swtThread;

    private Composite composite;

    public void connect() {
        if (swtThread == null) {
            final Canvas canvas = this;
            swtThread = new Thread() {
                @Override
                public void run() {
                    try {
                        Display display = new Display();
                        Shell shell = SWT_AWT.new_Shell(display, canvas);
                        shell.setLayout(new FillLayout());

                        synchronized (this) {
                            // swtBrowser = new Browser(shell, SWT.NONE);
                            composite = new Composite(shell, SWT.EMBEDDED | SWT.NO_BACKGROUND);
                            composite.setLayout(new GridLayout(1, true));
                            composite.setLayoutData(new GridData(GridData.FILL_BOTH));
                            Color black = display.getSystemColor(SWT.COLOR_BLUE);
                            composite.setBackground(black);

//                            System.out.println("COMPOSITE EMBEDDED HANDLE " + composite.embeddedHandle);

                            this.notifyAll();
                        }

                        shell.open();
                        while (!isInterrupted() && !shell.isDisposed()) {
                            if (!display.readAndDispatch()) {
                                display.sleep();
                            }
                        }
                        shell.dispose();
                        display.dispose();
                    }
                    catch (Exception e) {
                        interrupt();
                    }
                }
            };
            this.swtThread.start();

            synchronized (this.swtThread) {
                while (this.composite == null) {
                    try {
                        this.swtThread.wait(100);
                    }
                    catch(InterruptedException e) {
                        this.composite = null;
                        this.swtThread = null;
                        break;
                    }
                }
            }
        }
    }

    public void disconnect() {
        if (swtThread != null) {
            swtThread.interrupt();
            swtThread = null;
        }
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        disconnect();
    }

    public Composite getVideoSurface() {
        return composite;
    }
}
