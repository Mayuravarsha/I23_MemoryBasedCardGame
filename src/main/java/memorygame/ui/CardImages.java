package memorygame.ui;

import java.awt.Image;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

/** Loads card pictures from the classpath and caches them per size. */
final class CardImages {
    private final Map<String, ImageIcon> cache = new HashMap<>();

    ImageIcon face(int face, int size) {
        return load("card" + face + ".jpg", size);
    }

    ImageIcon back(int size) {
        return load("back.jpg", size);
    }

    private ImageIcon load(String name, int size) {
        return cache.computeIfAbsent(name + "@" + size, k -> {
            URL url = CardImages.class.getResource("/memorygame/cards/" + name);
            if (url == null) {
                throw new IllegalStateException("missing card image " + name);
            }
            Image img = new ImageIcon(url).getImage();
            return new ImageIcon(img.getScaledInstance(size, size, Image.SCALE_SMOOTH));
        });
    }
}
