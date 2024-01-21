package software.ulpgc.imageviewer.mocks;

import software.ulpgc.imageviewer.Image;
import software.ulpgc.imageviewer.ImageLoader;

public class MockImageLoader implements ImageLoader {
    private final String[] ids = new String[] {"image-viewer-2/src/software/ulpgc/imageviewer/images/1.jpg","image-viewer-2/src/software/ulpgc/imageviewer/images/2.jpg","image-viewer-2/src/software/ulpgc/imageviewer/images/3.jpg"};
    @Override
    public Image load() {
        return imageAt(0);
    }

    private Image imageAt(int i) {
        return new Image() {
            @Override
            public String id() {
                return ids[i];
            }

            @Override
            public Image next() {
                return imageAt((i + 1) % ids.length);
            }

            @Override
            public Image prev() {
                return imageAt(i > 0 ? i -1 : ids.length-1);
            }
        };
    }
}
