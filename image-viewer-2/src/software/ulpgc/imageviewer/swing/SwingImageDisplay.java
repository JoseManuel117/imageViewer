package software.ulpgc.imageviewer.swing;

import software.ulpgc.imageviewer.ImageDisplay;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwingImageDisplay extends JPanel implements ImageDisplay {
    private Shift shift = Shift.Null;
    private Released released = Released.Null;
    private int initShift;
    private List<Paint> paints = new ArrayList<>();

    private static final Map<String, BufferedImage> images = new HashMap<>();

    public SwingImageDisplay() {
        loadImages();
        this.addMouseListener(mouseListener());
        this.addMouseMotionListener(mouseMotionListener());
    }

    private MouseListener mouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                initShift = e.getX();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                released.offset(e.getX() - initShift);
            }

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) { }
        };
    }

    private MouseMotionListener mouseMotionListener() {
        return new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                shift.offset(e.getX() - initShift);
            }

            @Override
            public void mouseMoved(MouseEvent e) {}
        };
    }

    @Override
    public void paint(String id, int offset) {
        paints.add(new Paint(id, offset));
        repaint();
    }

    @Override
    public void clear() {
        paints.clear();
    }

    private static final Map<String,Color> colors = Map.of(
            "red", Color.RED,
            "green", Color.GREEN,
            "blue", Color.BLUE
    );
    @Override
    public void paint(Graphics g) {
        for (Paint paint : paints) {
            BufferedImage image = images.get(paint.id);
            if (image != null) {
                g.drawImage(image, paint.offset, 0, this);
            }
        }
    }
    private static void loadImages() {
        String[] imagePaths = new String[]{"image-viewer-2/src/software/ulpgc/imageviewer/images/1.jpg","image-viewer-2/src/software/ulpgc/imageviewer/images/2.jpg","image-viewer-2/src/software/ulpgc/imageviewer/images/3.jpg"};;

        for (String path : imagePaths) {
            try {
                BufferedImage originalImage = ImageIO.read(new File(path));
                BufferedImage resizedImage = resizeImage(originalImage, 1200, 800);
                images.put(path, resizedImage);
            } catch (IOException e) {
                System.err.println("Error al cargar la imagen: " + path);
                e.printStackTrace();
            }
        }
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();

        return outputImage;
    }
    @Override
    public void on(Shift shift) {
        this.shift = shift != null ? shift : Shift.Null;
    }

    @Override
    public void on(Released released) {
        this.released = released != null ? released : Released.Null;
    }

    private record Paint(String id, int offset) {
    }
}
