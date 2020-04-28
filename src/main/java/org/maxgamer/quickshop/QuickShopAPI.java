package org.maxgamer.quickshop;

import org.jetbrains.annotations.NotNull;
import org.maxgamer.quickshop.files.ConfigFile;
import org.maxgamer.quickshop.files.LanguageFile;

public final class QuickShopAPI {

    @NotNull
    public final ConfigFile configFile;

    @NotNull
    public final LanguageFile languageFile;

    @NotNull
    private final QuickShop plugin;

    public QuickShopAPI(@NotNull final QuickShop plugin) {
        this.plugin = plugin;
        this.configFile = new ConfigFile();
        languageFile = new LanguageFile(configFile);
    }

    public void reload(final boolean first) {
        this.configFile.load();
        this.languageFile.load();
        if (first) {
            // Register listeners...
        }
    }

    public void disable() {

    }

}
