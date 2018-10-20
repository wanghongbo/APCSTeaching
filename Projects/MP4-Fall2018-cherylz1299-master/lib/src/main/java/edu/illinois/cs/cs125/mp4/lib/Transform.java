package edu.illinois.cs.cs125.mp4.lib;

/**
 * A class that runs implements several simple transformations on 2D image arrays.
 */
public class Transform {

    /**
     * Constructor.
     */
    public Transform() {

    }

    /**
     * Shift left.
     *
     * @param originalImage the image to shift to the left
     * @param amount        the amount to shift the image to the left
     * @return the shifted image
     */
    public static RGBAPixel[][] shiftLeft(final RGBAPixel[][] originalImage,
                                          final int amount) {
        return shift(originalImage, amount, Direction.LEFT);
    }

    /**
     * Shift right.
     *
     * @param originalImage the image to shift to the right
     * @param amount        the amount to shift the image to the right
     * @return the shifted image
     */
    public static RGBAPixel[][] shiftRight(final RGBAPixel[][] originalImage,
                                           final int amount) {
        return shift(originalImage, amount, Direction.RIGHT);
    }

    /**
     * Shift up.
     *
     * @param originalImage the image to shift to the up
     * @param amount        the amount to shift the image to the up
     * @return the shifted image
     */
    public static RGBAPixel[][] shiftUp(final RGBAPixel[][] originalImage,
                                        final int amount) {
        return shift(originalImage, amount, Direction.UP);
    }

    /**
     * Shift down.
     *
     * @param originalImage the image to shift to the down
     * @param amount        the amount to shift the image to the down
     * @return the shifted image
     */
    public static RGBAPixel[][] shiftDown(final RGBAPixel[][] originalImage,
                                          final int amount) {
        return shift(originalImage, amount, Direction.DOWN);
    }

    /**
     * Direction enum.
     */
    private enum Direction {
        /**
         * Left direction.
         */
        LEFT,

        /**
         * Right direction.
         */
        RIGHT,

        /**
         * Up direction.
         */
        UP,

        /**
         * Down direction.
         */
        DOWN
    }

    /**
     * Shift a image with given amount and direction.
     *
     * @param originalImage the image to shift
     * @param amount        the amount to shift the image
     * @param direction     shift direction
     * @return the shifted image
     */
    private static RGBAPixel[][] shift(final RGBAPixel[][] originalImage,
                                       final int amount, final Direction direction) {
        int rows = originalImage.length;
        int cols = originalImage[0].length;
        RGBAPixel[][] newImage = new RGBAPixel[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
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
                    default:
                        return originalImage;
                }
            }
        }
        return newImage;
    }

    /**
     * Convert between pixel array and image coordinate.
     *
     * @param originalImage the original image to convert
     * @return the converted image
     */
    private static RGBAPixel[][] convert(final RGBAPixel[][] originalImage) {
        int rows = originalImage[0].length;
        int cols = originalImage.length;
        RGBAPixel[][] newImage = new RGBAPixel[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newImage[i][j] = originalImage[j][i];
            }
        }
        return newImage;
    }

    /**
     * Rotate the image left by 90 degrees around its center.
     *
     * @param originalImage the image to rotate left 90 degrees
     * @return the rotated image
     */
    public static RGBAPixel[][] rotateLeft(final RGBAPixel[][] originalImage) {
        //Old method.
        //return rotate(originalImage, Direction.LEFT);

        //New method.
        int rows = originalImage.length;
        int cols = originalImage[0].length;
        RGBAPixel[][] newImage = new RGBAPixel[rows][cols];

        double centerX = (rows - 1) / 2.0;
        double centerY = (cols - 1) / 2.0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = (int) (j - centerY + centerX);
                int y = (int) (centerX - i + centerY);
                if (x < 0 || x > rows - 1 || y < 0 || y > cols - 1) {
                    newImage[i][j] = RGBAPixel.getFillValue();
                } else {
                    int originI = (int) (centerX + centerY - j);
                    int originJ = (int) (i + centerY - centerX);
                    newImage[i][j] = originalImage[originI][originJ];
                }
            }
        }

        return newImage;
    }

    /**
     * Rotate the image right by 90 degrees around its center.
     *
     * @param originalImage the image to rotate right 90 degrees
     * @return the rotated image
     */
    public static RGBAPixel[][] rotateRight(final RGBAPixel[][] originalImage) {
        //Old method.
        //return rotate(originalImage, Direction.RIGHT);

        //New method.
        int rows = originalImage.length;
        int cols = originalImage[0].length;
        RGBAPixel[][] newImage = new RGBAPixel[rows][cols];

        double centerX = (rows - 1) / 2.0;
        double centerY = (cols - 1) / 2.0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = (int) (centerY - j + centerX);
                int y = (int) (i - centerX + centerY);
                if (x < 0 || x > rows - 1 || y < 0 || y > cols - 1) {
                    newImage[i][j] = RGBAPixel.getFillValue();
                } else {
                    int originI = (int) (j + centerX - centerY);
                    int originJ = (int) (centerY + centerX - i);
                    newImage[i][j] = originalImage[originI][originJ];
                }
            }
        }

        return newImage;
    }

    /**
     * Rotate the image left or right by 90 degrees around its center.
     *
     * @param originalImage the image to rotate
     * @param direction     the rotate direction
     * @return the rotated image
     */
    private static RGBAPixel[][] rotate(final RGBAPixel[][] originalImage, final Direction direction) {
        RGBAPixel[][] convertedImage = convert(originalImage);
        int rows = convertedImage.length;
        int cols = convertedImage[0].length;
        RGBAPixel[][] newImage = new RGBAPixel[cols][rows];
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                switch (direction) {
                    case LEFT:
                        newImage[i][j] = convertedImage[j][cols - i - 1];
                        break;
                    case RIGHT:
                        newImage[i][j] = convertedImage[rows - j - 1][i];
                        break;
                    default:
                        return originalImage;
                }
            }
        }
        RGBAPixel[][] rotatedImage = fill(newImage);
        return convert(rotatedImage);
    }

    /**
     * fill the rotated image with FILL_VALUE.
     *
     * @param image the image to fill
     * @return the filled image
     */
    private static RGBAPixel[][] fill(final RGBAPixel[][] image) {
        int width = image.length;
        int height = image[0].length;

        RGBAPixel[][] newImage = new RGBAPixel[height][width];

        if (width < height) {
            int from = (height - width) / 2;
            int to = from + width - 1;
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (j >= from && j <= to) {
                        newImage[j][i] = image[j - from][i + from];
                    } else {
                        newImage[j][i] = RGBAPixel.getFillValue();
                    }
                }
            }
        } else if (width > height) {
            int from = (width - height) / 2;
            int to = from + height - 1;
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (i >= from && i <= to) {
                        newImage[j][i] = image[j + from][i - from];
                    } else {
                        newImage[j][i] = RGBAPixel.getFillValue();
                    }
                }
            }
        } else {
            return image;
        }
        return newImage;
    }

    /**
     * Axis enum.
     */
    private enum Axis {
        /**
         * Vertical axis.
         */
        VERTICAL,

        /**
         * Horizontal axis.
         */
        HORIZONTAL
    }

    /**
     * Flip the image on the vertical axis across its center.
     *
     * @param originalImage the image to flip on its vertical axis
     * @return the flipped image
     */
    public static RGBAPixel[][] flipVertical(final RGBAPixel[][] originalImage) {
        return flip(originalImage, Axis.VERTICAL);
    }

    /**
     * Flip the image on the horizontal axis across its center.
     *
     * @param originalImage the image to flip on its horizontal axis
     * @return the flipped image
     */
    public static RGBAPixel[][] flipHorizontal(final RGBAPixel[][] originalImage) {
        return flip(originalImage, Axis.HORIZONTAL);
    }

    /**
     * Flip the image on the vertical axis or horizontal axis across its center.
     *
     * @param originalImage the image to flip on its vertical axis or horizontal axis
     * @param axis          vertical axis or horizontal axis
     * @return the flipped image
     */
    public static RGBAPixel[][] flip(final RGBAPixel[][] originalImage, final Axis axis) {
        RGBAPixel[][] convertedImage = convert(originalImage);
        int rows = convertedImage.length;
        int cols = convertedImage[0].length;
        RGBAPixel[][] newImage = new RGBAPixel[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                switch (axis) {
                    case VERTICAL:
                        newImage[i][j] = convertedImage[rows - i - 1][j];
                        break;
                    case HORIZONTAL:
                        newImage[i][j] = convertedImage[i][cols - j - 1];
                        break;
                    default:
                        return originalImage;
                }
            }
        }
        return convert(newImage);
    }

    /**
     * Shrink in the vertical axis around the image center.
     *
     * @param originalImage the image to shrink
     * @param amount        the factor by which the image's height is reduced
     * @return the shrunken image
     */
    public static RGBAPixel[][] shrinkVertical(final RGBAPixel[][] originalImage,
                                               final int amount) {
        return originalImage;
    }

    /**
     * Expand in the vertical axis around the image center.
     *
     * @param originalImage the image to expand
     * @param amount        the factor by which the image's height is increased
     * @return the expanded image
     */
    public static RGBAPixel[][] expandVertical(final RGBAPixel[][] originalImage,
                                               final int amount) {
        RGBAPixel[][] convertedImage = convert(originalImage);
        int rows = convertedImage.length * amount;
        int cols = convertedImage[0].length;
        RGBAPixel[][] newImage = new RGBAPixel[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newImage[i][j] = convertedImage[i / amount][j];
            }
        }
        RGBAPixel[][] expandedImage = new RGBAPixel[convertedImage.length][cols];
        for (int i = 0; i < convertedImage.length; i++) {
            for (int j = 0; j < cols; j++) {
                expandedImage[i][j] = newImage[(rows - convertedImage.length) / 2 + i][j];
            }
        }

        return convert(expandedImage);
    }

    /**
     * Shrink in the horizontal axis around the image center.
     *
     * @param originalImage the image to shrink
     * @param amount        the factor by which the image's width is reduced
     * @return the shrunken image
     */
    public static RGBAPixel[][] shrinkHorizontal(final RGBAPixel[][] originalImage,
                                                 final int amount) {
        return originalImage;
    }

    /**
     * Expand in the horizontal axis around the image center.
     *
     * @param originalImage the image to expand
     * @param amount        the factor by which the image's width is increased
     * @return the expanded image
     */
    public static RGBAPixel[][] expandHorizontal(final RGBAPixel[][] originalImage,
                                                 final int amount) {
        RGBAPixel[][] convertedImage = convert(originalImage);
        int rows = convertedImage.length;
        int cols = convertedImage[0].length * amount;
        RGBAPixel[][] newImage = new RGBAPixel[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newImage[i][j] = convertedImage[i][j / amount];
            }
        }
        RGBAPixel[][] expandedImage = new RGBAPixel[rows][convertedImage[0].length];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < convertedImage[0].length; j++) {
                expandedImage[i][j] = newImage[i][(cols - convertedImage[0].length) / 2 + j];
            }
        }

        return convert(expandedImage);
    }

    /**
     * Remove a green screen mask from an image.
     *
     * @param originalImage the image to remove a green screen from
     * @return the image with the green screen removed
     */
    public static RGBAPixel[][] greenScreen(final RGBAPixel[][] originalImage) {
        int rows = originalImage.length;
        int cols = originalImage[0].length;
        RGBAPixel[][] newImage = new RGBAPixel[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                RGBAPixel pixel = originalImage[i][j];
                if (pixel.getGreen() != 0) {
                    newImage[i][j] = RGBAPixel.getFillValue();
                } else {
                    newImage[i][j] = pixel;
                }
            }
        }
        return newImage;
    }
}
