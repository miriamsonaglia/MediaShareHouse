package com.miriamsonaglia.mediasharehouse.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

/**
 * Implementation of a customed button.
 *
 * @author Miriam Sonaglia
 */
public final class CustomButton extends JButton {

    private static final long serialVersionUID = 10L;

    /**
     * This method create a button with a text, abackground color and a border.
     *
     * @param text
     * @param customColor
     * @param customColor1
     * @param scale
     */
    public CustomButton(final String text, final Color customColor, final Color customColor1, final int scale) {
        super(text);
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setFont(new Font("Monospaced", Font.BOLD, 10 * scale));
        setForeground(customColor1);
        setBackground(customColor);
        setFocusable(false);
        final Border border = BorderFactory.createLineBorder(customColor1, 2, true);
        setBorder(border);
        setBorder(BorderFactory.createCompoundBorder(
                border,
                BorderFactory.createEmptyBorder(10, 10 * 2, 10, 10 * 2)));
    }
}
