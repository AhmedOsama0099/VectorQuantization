package com.vectorquantization;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class IOFile implements Serializable {
    int numOfColumns;
    ArrayList<int [][]>codeBook;
    ArrayList<String>imgCode;

    public IOFile(int numOfColumns, ArrayList<int[][]> codeBook, ArrayList<String> imgCode) {
        this.numOfColumns = numOfColumns;
        this.codeBook = codeBook;
        this.imgCode = imgCode;
    }
    public static IOFile ReadFromFile(String path) throws IOException, ClassNotFoundException {
        // Read objects
        FileInputStream fi = new FileInputStream(new File(path));
        ObjectInputStream oi = new ObjectInputStream(fi);
        IOFile ioFile = (IOFile) oi.readObject();
        oi.close();
        fi.close();
        return ioFile;
    }
    public void writToFile(String path) throws IOException {
        FileOutputStream f = new FileOutputStream(new File(path));
        ObjectOutputStream o = new ObjectOutputStream(f);
        // Write objects to file
        o.writeObject(this);
        o.close();
        f.close();
        /*FileInputStream fi = new FileInputStream(new File("myObjects.txt"));
        ObjectInputStream oi = new ObjectInputStream(fi);*/
    }

   /* private static void writeImage(int[][] imagePixels, String outPath) {
        int oldH = imagePixels.length;
        int oldW = imagePixels[0].length;
        BufferedImage img = new BufferedImage(oldW, oldH, BufferedImage.TYPE_3BYTE_BGR);

        for (int y = 0; y < oldH; y++) {
            for (int x = 0; x < oldW; x++) {

                int a = 255;
                int pix = imagePixels[y][x];
                int p = (a << 24) | (pix << 16) | (pix << 8) | pix;

                img.setRGB(x, y, p);
            }
        }

        File f = new File(outPath);

        try {
            ImageIO.write(img, "jpg", f);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }*/
}
