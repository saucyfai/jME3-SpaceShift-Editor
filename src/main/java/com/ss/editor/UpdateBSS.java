package com.ss.editor;

import com.sun.javafx.css.Stylesheet;

import java.io.File;
import java.io.IOException;

/**
 * The class to update bss files.
 */
public class UpdateBSS {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {
        Stylesheet.convertToBinary(new File("./src/main/resources/ui/css/base.css"), new File("./src/main/resources/ui/css/base.bss"));
        Stylesheet.convertToBinary(new File("./src/main/resources/ui/css/shadow-color.css"), new File("./src/main/resources/ui/css/shadow-color.bss"));
        Stylesheet.convertToBinary(new File("./src/main/resources/ui/css/dark-color.css"), new File("./src/main/resources/ui/css/dark-color.bss"));
        Stylesheet.convertToBinary(new File("./src/main/resources/ui/css/light-color.css"), new File("./src/main/resources/ui/css/light-color.bss"));
        Stylesheet.convertToBinary(new File("./src/main/resources/ui/css/external.css"), new File("./src/main/resources/ui/css/external.bss"));
        Stylesheet.convertToBinary(new File("./src/main/resources/ui/css/custom_ids.css"), new File("./src/main/resources/ui/css/custom_ids.bss"));
        Stylesheet.convertToBinary(new File("./src/main/resources/ui/css/custom_classes.css"), new File("./src/main/resources/ui/css/custom_classes.bss"));
    }
}
