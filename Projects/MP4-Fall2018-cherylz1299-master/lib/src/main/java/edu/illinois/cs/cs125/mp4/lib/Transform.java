package edu.illinois.cs.cs125.mp4.lib;

/**
 * 
 */
public class Transform {

    public Transform() {

    }

    public static RGBAPixel[][] shiftLeft(RGBAPixel[][] originalImage,
                                           int amount) {
        return shift(originalImage, amount, ShiftDirection.LEFT);
    }

    public static RGBAPixel[][] shiftRight(RGBAPixel[][] originalImage,
                                            int amount) {
        return shift(originalImage, amount, ShiftDirection.RIGHT);
    }

    public static RGBAPixel[][] shiftUp(RGBAPixel[][] originalImage,
                                         int amount) {
        return shift(originalImage, amount, ShiftDirection.UP);
    }

    public static RGBAPixel[][] shiftDown(RGBAPixel[][] originalImage,
                                           int amount) {
        return shift(originalImage, amount, ShiftDirection.DOWN);
    }

    private enum ShiftDirection {
        /** Shift left. */
        LEFT,

        /** Shift right. */
        RIGHT,

        /** Shift up. */
        UP,

        /** Shift down. */
        DOWN
    }

    private static RGBAPixel[][] shift(RGBAPixel[][] originalImage,
                                       int amount, ShiftDirection direction) {
        int rows = originalImage.length;
        int cols = originalImage[0].length;
        RGBAPixel[][] newImage = new RGBAPixel[rows][cols];
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                switch (direction) {
                    case LEFT:
                        if (i + amount < rows) {
                            newImage[i][j] = originalImage[i + amount][j];
                        } else {
                            newImage[i][j] = RGBAPixel.getFillValue();
                        }
                        break;
                    case RIGHT:
                        if (i - amount >= 0) {
                            newImage[i][j] = originalImage[i - amount][j];
                        } else {
                            newImage[i][j] = RGBAPixel.getFillValue();
                        }
                        break;
                    case UP:
                        if (j + amount < cols) {
                            newImage[i][j] = originalImage[i][j + amount];
                        } else {
                            newImage[i][j] = RGBAPixel.getFillValue();
                        }
                        break;
                    case DOWN:
                        if (j - amount >= 0) {
                            newImage[i][j] = originalImage[i][j - amount];
                        } else {
                            newImage[i][j] = RGBAPixel.getFillValue();
                        }
                        break;
                }
            }
        }
        return newImage;
    }

    public static RGBAPixel[][] rotateLeft(RGBAPixel[][] originalImage) {
        return originalImage;
    }

    public static RGBAPixel[][] rotateRight(RGBAPixel[][] originalImage) {
        return originalImage;
    }

    public static RGBAPixel[][] flipVertical(RGBAPixel[][] originalImage) {
        return originalImage;
    }

    public static RGBAPixel[][] flipHorizontal(RGBAPixel[][] originalImage) {
        return  originalImage;
    }

    public static RGBAPixel[][] shrinkVertical(RGBAPixel[][] originalImage,
                                                int amount) {
        return originalImage;
    }

    public static RGBAPixel[][] expandVertical(RGBAPixel[][] originalImage,
                                                int amount) {
        return originalImage;
    }

    public static RGBAPixel[][] shrinkHorizontal(RGBAPixel[][] originalImage,
                                                  int amount) {
        return originalImage;
    }

    public static RGBAPixel[][] expandHorizontal(RGBAPixel[][] originalImage,
                                                  int amount) {
        return originalImage;
    }

    public static RGBAPixel[][] greenScreen(RGBAPixel[][] originalImage) {
        return originalImage;
    }
}
