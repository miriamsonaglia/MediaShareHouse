package com.miriamsonaglia.mediasharehouse.view;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class AudioDisplayer implements ContentDisplayer {
    @Override
    public JPanel displayContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel("Riproduzione musicale non implementata in questo esempio.");
        label.setHorizontalAlignment(JLabel.CENTER);
        panel.add(label, BorderLayout.CENTER);

        return panel;
    }
}