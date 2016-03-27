package uk.co.caprica.vlcjswt;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.player.embedded.videosurface.linux.LinuxVideoSurfaceAdapter;

// On OSX, pass -XstartOnFirstThread when starting the JVM

public class VlcjSwtPlayer {

    private static SwtEmbeddedMediaPlayer mediaPlayer;

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Specify an MRL");
            System.exit(1);
        }

        System.setProperty("sun.awt.xembedserver", "true");

        final SwtVideoCanvas browserCanvas = new SwtVideoCanvas();
        browserCanvas.setPreferredSize(new Dimension(800, 600));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(browserCanvas, BorderLayout.CENTER);

        panel.add(new JLabel("I am a Swing application"), BorderLayout.NORTH);
        panel.add(new JLabel("The video above is embedded inside an SWT Composite"), BorderLayout.SOUTH);

        // Add container to Frame
        JFrame frame = new JFrame("vlcj SWT");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panel);
        frame.pack();

        LibVlc libvlc = LibVlc.INSTANCE;
        libvlc_instance_t instance = libvlc.libvlc_new(0, null);

        mediaPlayer = new SwtEmbeddedMediaPlayer(libvlc, instance);

        // This is VERY important: Make the frame visible BEFORE connecting the SWT Shell and starting the event loop
        frame.setVisible(true);
        browserCanvas.connect();

//        Thread.sleep(1000);

        mediaPlayer.setVideoSurface(new CompositeVideoSurface(browserCanvas.getVideoSurface(), new LinuxVideoSurfaceAdapter()));

        mediaPlayer.playMedia(args[0]);

        Thread.currentThread().join();
    }
}
