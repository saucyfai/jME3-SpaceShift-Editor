package com.ss.editor.manager;

import static com.ss.rlib.plugin.impl.PluginSystemFactory.newBasePluginSystem;
import com.ss.editor.Editor;
import com.ss.editor.annotation.FXThread;
import com.ss.editor.annotation.JMEThread;
import com.ss.editor.config.Config;
import com.ss.editor.plugin.EditorPlugin;
import com.ss.rlib.logging.Logger;
import com.ss.rlib.logging.LoggerManager;
import com.ss.rlib.manager.InitializeManager;
import com.ss.rlib.plugin.ConfigurablePluginSystem;
import com.ss.rlib.plugin.Plugin;
import com.ss.rlib.plugin.exception.PreloadPluginException;
import com.ss.rlib.util.FileUtils;
import com.ss.rlib.util.Utils;
import com.ss.rlib.util.array.Array;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

/**
 * The manager to work with plugins.
 *
 * @author JavaSaBr
 */
public class PluginManager {

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(PluginManager.class);

    @Nullable
    private static PluginManager instance;

    @NotNull
    public static PluginManager getInstance() {
        if (instance == null) instance = new PluginManager();
        return instance;
    }

    @NotNull
    private final ConfigurablePluginSystem pluginSystem;

    private PluginManager() {
        InitializeManager.valid(getClass());

        this.pluginSystem = newBasePluginSystem(getClass().getClassLoader());
        this.pluginSystem.setAppVersion(Config.APP_VERSION);

        final Path folderInUserHome = Config.getAppFolderInUserHome();
        final String embeddedPath = System.getProperty("editor.embedded.plugins.path");

        if (embeddedPath != null) {
            final Path embeddedPluginPath = Paths.get(embeddedPath);
            LOGGER.debug(this, "embedded plugin path: " + embeddedPluginPath);
            pluginSystem.configureEmbeddedPluginPath(embeddedPluginPath);
        } else {
            final Path rootFolder = Utils.getRootFolderFromClass(Editor.class);
            final Path embeddedPluginPath = rootFolder.resolve("embedded-plugins");
            LOGGER.debug(this, "embedded plugin path: " + embeddedPluginPath);
            if (Files.exists(embeddedPluginPath)) {
                pluginSystem.configureEmbeddedPluginPath(embeddedPluginPath);
            } else {
                LOGGER.warning(this, "The embedded plugin folder doesn't exists.");
            }
        }

        final Path userPluginsFolder = folderInUserHome.resolve("plugins");

        LOGGER.debug(this, "installation plugin path: " + userPluginsFolder);

        if (!Files.exists(userPluginsFolder)) {
            Utils.run(() -> Files.createDirectories(userPluginsFolder));
        }

        pluginSystem.configureInstallationPluginsPath(userPluginsFolder);

        try {
            pluginSystem.preLoad();
        } catch (final PreloadPluginException e) {
            FileUtils.delete(e.getPath());
            throw e;
        }

        pluginSystem.initialize();
    }

    /**
     * Install a new plugin to the system.
     *
     * @param path the path to the plugin.
     */
    public void installPlugin(@NotNull final Path path) {
        pluginSystem.installPlugin(path, false);
    }

    /**
     * Remove a plugin from this editor.
     *
     * @param plugin the plugin.
     */
    public void removePlugin(@NotNull final EditorPlugin plugin) {
        pluginSystem.removePlugin(plugin);
    }

    /**
     * Do some things before when JME context will be created.
     */
    @JMEThread
    public void onBeforeCreateJMEContext() {
        final Array<Plugin> plugins = pluginSystem.getPlugins();
        plugins.stream().filter(EditorPlugin.class::isInstance)
                .map(EditorPlugin.class::cast)
                .forEach(editorPlugin -> editorPlugin.onBeforeCreateJMEContext(pluginSystem));
    }

    /**
     * Do some things after when JME context was created.
     */
    @JMEThread
    public void onAfterCreateJMEContext() {
        final Array<Plugin> plugins = pluginSystem.getPlugins();
        plugins.stream().filter(EditorPlugin.class::isInstance)
                .map(EditorPlugin.class::cast)
                .forEach(editorPlugin -> editorPlugin.onAfterCreateJMEContext(pluginSystem));
    }

    /**
     * Do some things before when JavaFX context will be created.
     */
    @FXThread
    public void onBeforeCreateJavaFXContext() {
        final Array<Plugin> plugins = pluginSystem.getPlugins();
        plugins.stream().filter(EditorPlugin.class::isInstance)
                .map(EditorPlugin.class::cast)
                .forEach(editorPlugin -> editorPlugin.onBeforeCreateJavaFXContext(pluginSystem));
    }

    /**
     * Do some things after when JavaFX context was created.
     */
    @FXThread
    public void onAfterCreateJavaFXContext() {
        final Array<Plugin> plugins = pluginSystem.getPlugins();
        plugins.stream().filter(EditorPlugin.class::isInstance)
                .map(EditorPlugin.class::cast)
                .forEach(editorPlugin -> editorPlugin.onAfterCreateJavaFXContext(pluginSystem));
    }

    /**
     * Do some things before when the editor is ready to work.
     */
    @FXThread
    public void onFinishLoading() {
        final Array<Plugin> plugins = pluginSystem.getPlugins();
        plugins.stream().filter(EditorPlugin.class::isInstance)
                .map(EditorPlugin.class::cast)
                .forEach(editorPlugin -> editorPlugin.onFinishLoading(pluginSystem));
    }

    /**
     * Handle each loaded plugin.
     *
     * @param consumer the consumer.
     */
    public void handlePlugins(@NotNull final Consumer<EditorPlugin> consumer) {
        final Array<Plugin> plugins = pluginSystem.getPlugins();
        plugins.stream().filter(EditorPlugin.class::isInstance)
                .map(EditorPlugin.class::cast)
                .forEach(consumer);
    }
}
