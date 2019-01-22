# vlcj-swt
Support classes for vlcj to use an SWT Composite as a video surface in a vlcj EmbeddedMediaPlayer.

This project provides a new factory, SwtMediaPlayerFactory, that can be used to create a new
CompositeVideoSurface.

This video surface can then be attached to a standard vlcj EmbeddedMediaPlayer with no further changes.

You may still need various boiler-plate in your own application to embed the video component correctly
in an SWT application (e.g. to create a Shell with an event loop and so on).

## Important
Check the pom.xml for the architecture-specific dependencies for SWT.

At the present time, this project defaults to 64-bit Linux.

Other changes may need to be made for other platforms.

## OSX
You may need to pass -XstartOnFirstThread when starting the JVM.
