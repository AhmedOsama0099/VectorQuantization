package com.vectorquantization;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class IOFile implements Serializable {


    private static void writeImage(int[][] imagePixels, String outPath) {
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
    }
}
