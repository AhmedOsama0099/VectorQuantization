package com.vectorquantization;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GUI extends JFrame {
    private JTextField boockW;
    private JTextField bookH;
    private JTextField codeBookLength;
    private JButton compressImageButton;
    private JButton deCompressImageButton;
    /*private JTextField compressArea;
    private JTextField deCompressArea;
    private JButton browseButton;
    private JButton browseButton1;*/
    private JPanel panel1;

    public GUI() {
        setTitle("Arithmetic Coding");
        setSize(800, 600);
        add(panel1);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        btnActions();


    }

    private void btnActions() {
        compressImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser chooser = new JFileChooser();
                chooser.setMultiSelectionEnabled(false);
                chooser.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png"));
                chooser.showOpenDialog(null);
                File f = chooser.getSelectedFile();
                if (f != null) {
                    VectorQuantization vectorQuantization = new VectorQuantization();

                    try {
                        int width = Integer.parseInt(boockW.getText());
                        int height = Integer.parseInt(bookH.getText());
                        int length = Integer.parseInt(codeBookLength.getText());
                        vectorQuantization.compress(width, height, length, f.getPath());
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Enter Valid Data!");
                    }

                }
            }
        });
        deCompressImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser chooser = new JFileChooser();
                chooser.setMultiSelectionEnabled(false);
                chooser.showOpenDialog(null);
                File f = chooser.getSelectedFile();
                if (f != null) {
                    VectorQuantization vectorQuantization = new VectorQuantization();
                    vectorQuantization.deCompress(f.getPath());
                    System.out.println(f.getPath());

                }
            }
        });
        /*browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser chooser = new JFileChooser();
                chooser.setControlButtonsAreShown(false);
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.showOpenDialog(null);
                if (chooser.getCurrentDirectory() != null)
                    compressArea.setText(String.valueOf(chooser.getCurrentDirectory()));
            }
        });
        browseButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser chooser = new JFileChooser();
                chooser.setControlButtonsAreShown(false);
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.showOpenDialog(null);
                if (chooser.getCurrentDirectory() != null)
                    deCompressArea.setText(String.valueOf(chooser.getCurrentDirectory()));
            }
        });*/


    }

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GUI gui = new GUI();
                gui.setVisible(true);
            }
        });
    }
}
