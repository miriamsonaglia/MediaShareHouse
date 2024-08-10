package com.miriamsonaglia.mediasharehouse.view;

import java.io.File;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * Implementation of a panel with an image on the background.
 *
 * @author Miriam Sonaglia
 */
public final class ImagePanel extends JPanel {

    private static final long serialVersionUID = 10L;

    private transient BufferedImage backgroundImage;

    /**
     * reads the path of the image, if it doesn't exist it throws an exception.
     *
     * @param imagePath
     */
    public ImagePanel(final String imagePath) {
        try {
            backgroundImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            Logger.getLogger("IOException in ImagePanel constructor");
        }
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
