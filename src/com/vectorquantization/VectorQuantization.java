package com.vectorquantization;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class VectorQuantization {
    static Image img = new Image();

    public void deCompress(String path) throws IOException, ClassNotFoundException {
        IOFile ioFile = IOFile.ReadFromFile(path);
        int[][] image = deCode(ioFile);
        writeImage(image,"qqqqqqq.jpg");
    }

    private int[][] deCode(IOFile ioFile) {
        int bookW = ioFile.codeBook.get(0)[0].length;
        int bookH = ioFile.codeBook.get(0).length;
        int imgWidth = ioFile.numOfColumns * bookW;
        int imgHeight = (ioFile.imgCode.size() / ioFile.numOfColumns) * bookH;
        int image[][] = new int[imgHeight][imgWidth];
        int y=0;
        int x = 0;
        int countRow=0;
        for (int i = 0; i < ioFile.imgCode.size(); i++) {
            int index = Integer.parseInt(ioFile.imgCode.get(i), 2);
            int[][] currBlock = ioFile.codeBook.get(index);
            if(i%ioFile.numOfColumns==0&&i!=0){
                countRow+=bookH;
                x=0;
            }
            y=countRow;
            int tmp=x;
            for (int bookY = 0; bookY < bookH; bookY++) {
                for (int bookX = 0; bookX < bookW; bookX++) {
                    image[y][x] = currBlock[bookY][bookX];
                    x++;
                }
                x = tmp;
                y++;
            }
            x+=bookW;
        }

        for (y = 0; y < imgHeight; y++) {
            for (x = 0; x < imgWidth; x++) {
                System.out.print(image[y][x] + " ");
            }
            System.out.println();
        }
        return image;
    }

    public void compress(int blockW, int blockH, int blockSize, String path) {
        convertImageToMatrix(path);
        convertImgToBlocks(blockW, blockH);
        ArrayList<Node> codeBooksWithChildren = split(blockSize, blockW, blockH);
        ArrayList<String> imageCodes = new ArrayList<>();
        for (int[][] imgBlock : img.imgBlocks) {
            for (int j = 0; j < codeBooksWithChildren.size(); j++) {
                Node currNode = codeBooksWithChildren.get(j);
                if (currNode.blocksOwns.contains(imgBlock)) {
                    imageCodes.add(Integer.toBinaryString(j));
                    break;
                }
            }
        }
        ArrayList<int[][]> codeBook = new ArrayList<>();
        for (Node currNode : codeBooksWithChildren) {
            codeBook.add(currNode.block);
        }
        IOFile ioFile = new IOFile(img.matrix.length / blockW, codeBook, imageCodes);
        try {
            ioFile.writToFile(new File(path).getName()+".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*for(int i=0;i<ss.size();i++){
            for(int y=0;y<blockH;y++){
                for (int x=0;x<blockW;x++)
                    System.out.print(ss.get(i).block[y][x]+" ");
                System.out.println();
            }
            System.out.println("--------------");
        }*/

    }

    private static void euclidean(ArrayList<Node> splitNode, int blockW, int blockH) {
        /*for(Node node:splitNode){
            int [][]mm=node.block;
            for(int y=0;y<blockH;y++){
                for(int x=0;x<blockW;x++){
                    System.out.print(mm[y][x]+" ");
                }
                System.out.println();
            }
            System.out.println("------");
        }
        System.out.println(";;;;;;;;;;")*/
        ;
        ArrayList<Double> distances = new ArrayList<>();
        double sum;
        for (int i = 0; i < img.imgBlocks.size(); i++) {
            int[][] currBlock = img.imgBlocks.get(i);
            distances.clear();
            sum = 0.0;
            for (Node currNode : splitNode) {
                for (int y = 0; y < blockH; y++) {
                    for (int x = 0; x < blockW; x++) {
                        sum += Math.pow(currNode.block[y][x] - currBlock[y][x], 2);
                    }
                }
                distances.add(Math.sqrt(sum));
                sum = 0.0;
            }
            int minIndex = distances.indexOf(Collections.min(distances));
            splitNode.get(minIndex).blocksOwns.add(currBlock);
        }


   /*     for(Node node:splitNode){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!");
            int [][]mm=node.block;
            for(int y=0;y<blockH;y++){
                for(int x=0;x<blockW;x++){
                    System.out.print(mm[y][x]+" ");
                }
                System.out.println();
            }
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!");
            for(int [][]xx:node.blocksOwns){
                for(int y=0;y<blockH;y++){
                    for(int x=0;x<blockW;x++){
                        System.out.print(xx[y][x]+" ");
                    }
                    System.out.println("");

                }
                System.out.println("------");
            }

        }*/


    }

    private static ArrayList<Node> split(int bookSize, int blockW, int blockH) {
        int numOfSplits = (int) Math.ceil(Math.log(bookSize) / Math.log(2));
        int[][] root = getAvgBlock(img.imgBlocks, blockW, blockH);
        Node node = new Node();
        node.block = root;
        ArrayList<Node> splitNodes = new ArrayList<>();
        splitNodes.add(node);
        for (int i = 0; i < numOfSplits; i++) {
            int siz = splitNodes.size();
            for (int j = 0; j < siz; j++) {
                for (int k = 0; k < 2; k++) {
                    Node n = new Node();
                    n.block = new int[blockH][blockW];
                    for (int y = 0; y < blockH; y++) {
                        for (int x = 0; x < blockW; x++) {
                            if (k == 0) {
                                n.block[y][x] = splitNodes.get(0).block[y][x] - 1;
                            } else {
                                n.block[y][x] = splitNodes.get(0).block[y][x] + 1;
                            }
                        }
                    }
                    splitNodes.add(n);
                }
                splitNodes.remove(0);
                euclidean(splitNodes, blockW, blockH);
                for (int splitMover = 0; splitMover < splitNodes.size(); splitMover++) {
                    Node nn = new Node();
                    nn.block = getAvgBlock(splitNodes.get(0).blocksOwns, blockW, blockH);
                    splitNodes.add(nn);
                    splitNodes.remove(0);
                }
            }
        }
        euclidean(splitNodes, blockW, blockH);
        return splitNodes;
    }

    private static int[][] getAvgBlock(ArrayList<int[][]> blocks, int blockW, int blockH) {
        int[][] block = new int[blockH][blockW];
        for (int i = 0; i < blocks.size(); i++) {
            for (int y = 0; y < blockH; y++) {
                for (int x = 0; x < blockW; x++) {
                    block[y][x] += blocks.get(i)[y][x];
                }
            }
        }
        for (int y = 0; y < blockH; y++) {
            for (int x = 0; x < blockW; x++) {
                //block[y][x] = (int) Math.round(block[y][x] / (double) blocks.size());
                block[y][x]=block[y][x]/blocks.size();
            }
        }
        return block;
    }

    private static void convertImgToBlocks(int bookW, int bookH) {
        handleImgWithBookSize(bookW, bookH);
        int imgWidth = img.matrix[0].length;
        int imgHeight = img.matrix.length;
        for (int imgY = 0; imgY < imgHeight; imgY += bookH) {
            for (int imgX = 0; imgX < imgWidth; imgX += bookW) {
                int[][] block = new int[bookH][bookW];
                int y = 0;
                int x = 0;

                for (int bookY = imgY; bookY < imgY + bookH; bookY++) {
                    for (int bookX = imgX; bookX < imgX + bookW; bookX++) {
                        block[y][x] = img.matrix[bookY][bookX];
                        x++;
                    }
                    x = 0;
                    y++;
                }
                img.imgBlocks.add(block);
            }
        }
        System.out.println(" ***** "+img.imgBlocks.size());
    }

    private static void handleImgWithBookSize(int bookW, int bookH) {
        int imgWidth = img.matrix[0].length;
        int imgHeight = img.matrix.length;
        if (imgWidth % bookW != 0) imgWidth = ((imgWidth / bookW) + 1) * bookW;
        if (imgHeight % bookH != 0) imgHeight = ((imgHeight / bookH) + 1) * bookH;
        int newMatrix[][] = new int[imgHeight][imgWidth];
        for (int y = 0; y < imgHeight; y++) {
            for (int x = 0; x < imgWidth; x++) {
                newMatrix[y][x] = img.matrix[y][x];
            }
        }
        img.matrix = newMatrix;
        System.out.println(" ;; "+img.matrix.length+" "+img.matrix[0].length);
    }

    private static void convertImageToMatrix(String filePath) {
        File f = new File(filePath); //image file path
        int[][] imageMAtrix = null;
        try {
            BufferedImage img = ImageIO.read(f);
            int oldW = img.getWidth();
            int oldH = img.getHeight();
            imageMAtrix = new int[oldH][oldW];
            for (int y = 0; y < oldH; y++) {
                for (int x = 0; x < oldW; x++) {
                    int p = img.getRGB(x, y);
                    int a = (p >> 24) & 0xff;
                    int r = (p >> 16) & 0xff;
                    int g = (p >> 8) & 0xff;
                    int b = p & 0xff;
                    //because in gray image r=g=b  we will select r
                    imageMAtrix[y][x] = r;

                    //set new RGB value
                    p = (a << 24) | (r << 16) | (g << 8) | b;
                    img.setRGB(x, y, p);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        img.matrix = imageMAtrix;
    }
    public static void writeImage(int[][] imagePixels, String outPath) {
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
