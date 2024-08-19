package com.miriamsonaglia.mediasharehouse.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

public class AudioDisplayer implements ContentDisplayer {

    private File audioFile;
    private AdvancedPlayer player;
    private volatile boolean isPlaying = false;
    private volatile boolean isPaused = false;
    private JButton playButton;
    private JSlider progressSlider;
    private JLabel timeLabel;
    private long totalDuration;
    private long currentDuration = 0;

    public AudioDisplayer(File file) {
        this.audioFile = file;
        this.totalDuration = calculateTotalDuration();
    }

    @Override
    public JPanel displayContent() {
        JPanel panel = new JPanel(new BorderLayout());

        timeLabel = new JLabel("0:00 / " + formatDuration(totalDuration));
        progressSlider = new JSlider(0, 1000, 0);

        playButton = new JButton("Play");
        playButton.addActionListener(new PlayButtonListener());

        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.X_AXIS));
        controlsPanel.add(playButton);
        controlsPanel.add(progressSlider);
        controlsPanel.add(timeLabel);

        panel.add(controlsPanel, BorderLayout.CENTER);

        return panel;
    }

    private long calculateTotalDuration() {
        try (FileInputStream fis = new FileInputStream(audioFile)) {
            Bitstream bitstream = new Bitstream(fis);
            long duration = 0;
            while (bitstream.readFrame() != null) {
                duration += bitstream.readFrame().ms_per_frame();
            }
            return duration;
        } catch (IOException | BitstreamException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private String formatDuration(long millis) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes);
        return String.format("%d:%02d", minutes, seconds);
    }

    private void playAudio() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    FileInputStream fis = new FileInputStream(audioFile);
                    player = new AdvancedPlayer(fis);
                    if (isPaused) {
                        startPlaybackFrom(currentDuration);
                    } else {
                        startPlaybackFrom(0);
                    }
                } catch (JavaLayerException | IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                updateProgress();
            }
        };
        worker.execute();
    }

    private void startPlaybackFrom(long startPosition) {
        new Thread(() -> {
            try {
                FileInputStream fis = new FileInputStream(audioFile);
                player = new AdvancedPlayer(fis);
                player.play((int) (startPosition / 26.1224), Integer.MAX_VALUE); // 26.1224 ms per frame (approssimativamente)
            } catch (JavaLayerException | IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void updateProgress() {
        Timer timer = new Timer(1000, e -> {
            if (isPlaying && !isPaused) {
                currentDuration += 1000; // Supponendo che sia passato un secondo
                SwingUtilities.invokeLater(() -> {
                    int progress = (int) ((double) currentDuration / totalDuration * 1000);
                    progressSlider.setValue(progress);
                    timeLabel.setText(formatDuration(currentDuration) + " / " + formatDuration(totalDuration));
                });
            }
        });
        timer.start();
    }

    private void pauseAudio() {
        if (player != null) {
            player.close(); // Chiude il player, interrompendo la riproduzione
            isPaused = true;
        }
    }

    private class PlayButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!isPlaying) {
                playAudio();
                playButton.setText("Pause");
                isPlaying = true;
            } else if (isPaused) {
                playAudio();
                playButton.setText("Pause");
                isPaused = false;
            } else {
                pauseAudio();
                playButton.setText("Resume");
            }
        }
    }
}
