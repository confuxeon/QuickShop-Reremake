package org.maxgamer.quickshop.files;

import io.github.portlek.configs.BukkitLinkedManaged;
import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.LinkedConfig;
import io.github.portlek.configs.util.FileType;
import io.github.portlek.configs.util.MapEntry;
import org.jetbrains.annotations.NotNull;

@LinkedConfig(files = {
    @LinkedFile(
        id = "tr-TR",
        config = @Config(
            name = "messages",
            type = FileType.JSON,
            location = "lang/tr-TR"
        )
    ),
    @LinkedFile(
        id = "en-US",
        config = @Config(
            name = "messages",
            type = FileType.JSON,
            location = "lang/en-US"
        )
    ),
})
public final class LanguageFile extends BukkitLinkedManaged {

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

}
