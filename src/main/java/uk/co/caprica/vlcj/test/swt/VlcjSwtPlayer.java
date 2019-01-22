package uk.co.caprica.vlcj.test.swt;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.factory.swt.SwtMediaPlayerFactory;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.linux.LinuxVideoSurfaceAdapter;
import uk.co.caprica.vlcj.player.embedded.videosurface.swt.CompositeVideoSurface;
import uk.co.caprica.vlcj.test.swt.SwtVideoCanvas;

// On OSX, pass -XstartOnFirstThread when starting the JVM

public class VlcjSwtPlayer {

    private static SwtMediaPlayerFactory factory;

    private static EmbeddedMediaPlayer mediaPlayer;

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Specify an MRL");
            System.exit(1);
        }

        System.setProperty("sun.awt.xembedserver", "true");

        final SwtVideoCanvas videoCanvas = new SwtVideoCanvas();
        videoCanvas.setPreferredSize(new Dimension(800, 600));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(videoCanvas, BorderLayout.CENTER);

        panel.add(new JLabel("I am a Swing application"), BorderLayout.NORTH);
        panel.add(new JLabel("The video above is embedded inside an SWT Composite"), BorderLayout.SOUTH);

        // Add container to Frame
        JFrame frame = new JFrame("vlcj SWT");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panel);
        frame.pack();

        factory = new SwtMediaPlayerFactory();
        mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();

        // This is VERY important: Make the frame visible BEFORE connecting the SWT Shell and starting the event loop
        frame.setVisible(true);
        videoCanvas.connect();

        VideoSurface videoSurface = factory.swt().newCompositeVideoSurface(videoCanvas.getVideoSurface());
        mediaPlayer.videoSurface().setVideoSurface(videoSurface);

        Media media = factory.media().newMedia("/home/mark/1.mp4");
        mediaPlayer.media().set(media);
        mediaPlayer.controls().play();

        Thread.currentThread().join();
    }
}
