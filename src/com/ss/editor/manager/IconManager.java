package com.ss.editor.manager;

import com.ss.editor.util.EditorUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;

import static java.lang.String.valueOf;

/**
 * Менеджер по работе с иконками.
 *
 * @author Ronn
 */
public class IconManager {

    private static final Logger LOGGER = LoggerManager.getLogger(IconManager.class);

    public static final int DEFAULT_FILE_ICON_SIZE = 16;

    private static IconManager instance;

    public static IconManager getInstance() {

        if(instance == null) {
            instance = new IconManager();
        }

        return instance;
    }

    /**
     * Кеш для хранения загруженных иконок.
     */
    private final Map<String, Image> imageCache;

    public IconManager() {
        this.imageCache = new HashMap<>();
    }

    /**
     * Получение иконки для указанного файла.
     *
     * @param path файл для которого надо получить иконку.
     * @param size размер иконки.
     * @return найденная иконка.
     */
    public Image getIcon(final Path path, int size) {

        String contentType = null;

        try {
            contentType = Files.probeContentType(path);
        } catch (IOException e) {
            LOGGER.warning(e);
        }

        if(contentType != null) {
            contentType = contentType.replace("/", "-");
        }

        if(contentType == null) {
            contentType = "none";
        } else if("inode-directory".equals(contentType)) {
            contentType = "folder";
        }

        final Path mimeTypes = Paths.get("/ui/icons/faenza/mimetypes");

        Path iconPath = mimeTypes.resolve(valueOf(size)).resolve(contentType + ".png");
        String url = iconPath.toString();

        if(!EditorUtil.checkExists(url)) {
            iconPath = mimeTypes.resolve(valueOf(size)).resolve("none.png");
            url = iconPath.toString();
        }

        return getImage(url);
    }

    public Image getImage(final String url) {

        Image image = imageCache.get(url);

        if(image == null) {
            image = new Image(url);
            imageCache.put(url, image);
        }

        return image;
    }
}
