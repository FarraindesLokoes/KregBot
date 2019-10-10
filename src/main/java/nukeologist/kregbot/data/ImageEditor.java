package nukeologist.kregbot.data;

import nukeologist.kregbot.api.Context;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageEditor {

    private BufferedImage bufferedImage;
    private Graphics2D graphics2D;
    private static File file = new File("tempImage.jpg");


    //region draw
    public ImageEditor drawPoint(int x, int y, int color) {
        bufferedImage.setRGB(x, y, color);
        return this;
    }

    public ImageEditor drawLine(int x1, int y1, int x2, int y2, Color color) {
        graphics2D.setColor(color != null ? color : Color.BLACK);
        graphics2D.drawLine(x1, y1, x2, y2);
        return this;
    }

    public ImageEditor drawRect(int x1, int y1, int x2, int y2, Color color) {
        graphics2D.setColor(color);
        graphics2D.fillRect(x1, y1, x2, y2);
        return this;
    }

    public ImageEditor drawEllipse(int x1, int y1, int x2, int y2, Color color) {
        graphics2D.setColor(color);
        graphics2D.fillOval(x1, y1, x2, y2);
        return this;
    }
    //endregion

    public ImageEditor clear() {
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        return this;
    }
    public ImageEditor display(Context context) throws IOException {
        graphics2D.dispose();
        ImageIO.write(bufferedImage, "jpg", file);
        context.getChannel().sendFile(file).queue();
        graphics2D = bufferedImage.createGraphics();
        return this;
    }

    private ImageEditor(int x, int y) {
        bufferedImage = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
        graphics2D = bufferedImage.createGraphics();
        clear();

    }

    public static ImageEditor createImage(int width, int height) {
        return new ImageEditor(width, height);
    }


}
