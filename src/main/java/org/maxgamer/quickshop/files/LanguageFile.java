package org.maxgamer.quickshop.files;

import io.github.portlek.configs.BukkitLinkedManaged;
import io.github.portlek.configs.BukkitSection;
import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.LinkedConfig;
import io.github.portlek.configs.annotations.Section;
import io.github.portlek.configs.util.FileType;
import io.github.portlek.configs.util.MapEntry;
import org.jetbrains.annotations.NotNull;

@LinkedConfig(configs = {
    @Config(
        name = "tr-TR",
        type = FileType.JSON,
        location = "%basedir%/QuickShop-Era",
        copyDefault = true,
        resourcePath = "lang"
    ),
    @Config(
        name = "en-US",
        type = FileType.JSON,
        location = "%basedir%/QuickShop-Era",
        copyDefault = true,
        resourcePath = "lang"
    )
})
public final class LanguageFile extends BukkitLinkedManaged {

    @Instance
    public final LanguageFile.Errors errors = new LanguageFile.Errors();

    public LanguageFile(@NotNull final ConfigFile configFile) {
        super(configFile.plugin_language, MapEntry.from("config", configFile));
    }

    @NotNull
    public ConfigFile getConfigFile() {
        return (ConfigFile) this.pull("config").orElseThrow(() ->
            new IllegalStateException("LanguageFile class couldn't load correctly."));
    }

    @NotNull
    public String getPrefix() {
        return this.getConfigFile().plugin_prefix;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onLoad() {

    }

    @Section(path = "errors")
    public final class Errors extends BukkitSection {

    }

}
