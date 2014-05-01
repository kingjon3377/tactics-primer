package common;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * A class to load images from file, backed by a cache so no image is loaded
 * more than once. This probably belongs in the controller, but we'd rather
 * avoid package-level circular dependencies. And it shouldn't be singleton, but
 * I can't think of a better way of making the cache actually be shared without
 * making it effectively singleton except for lightweight instances to access
 * the singleton cache.
 *
 * @author Jonathan Lovelace
 */
public final class ImageLoader {
	/**
	 * Singleton instance.
	 */
	private static final ImageLoader LOADER = new ImageLoader();

	/**
	 * An icon cache.
	 */
	private final Map<String, Icon> iconCache = new HashMap<>();

	/**
	 * The size of fixture icons.
	 */
	private static final int ICON_SIZE = 20;
	/**
	 * The cache.
	 */
	private final Map<String, Image> cache = new HashMap<>();

	/**
	 * Constructor.
	 */
	private ImageLoader() {
		// Singleton.
	}

	/**
	 * @return the instance.
	 */
	public static ImageLoader getLoader() {
		return LOADER;
	}

	/**
	 * Load an image from the cache, or if not in it, from file.
	 *
	 * @param file
	 *            the name of the file to load
	 * @return the image contained in the file.
	 * @throws IOException
	 *             if the file isn't found, or on other I/O error reading the
	 *             file
	 */
	public Image loadImage(final String file) throws IOException {
		if (!cache.containsKey(file)) {
			try (final ResourceInputStream res = new ResourceInputStream(
					"images/" + file)) {
				cache.put(file, ImageIO.read(res));
			}
		}
		return cache.get(file);
	}

	/**
	 * Load an icon from the cache, or if not in it, from file.
	 *
	 * @param file
	 *            the name of the file to load
	 * @return an icon of image contained in the file.
	 * @throws IOException
	 *             if the file isn't found, or on other I/O error reading the
	 *             file
	 */
	public Icon loadIcon(final String file) throws IOException {
		if (!iconCache.containsKey(file)) {
			iconCache.put(file, new ImageIcon(loadImage(file)
					.getScaledInstance(ICON_SIZE, -1, Image.SCALE_DEFAULT)));
		}
		return iconCache.get(file);
	}

	/**
	 * @return a String representation of the object
	 */
	@Override
	public String toString() {
		return "ImageLoader";
	}
}
