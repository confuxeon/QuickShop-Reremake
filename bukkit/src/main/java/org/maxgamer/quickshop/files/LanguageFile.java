package org.maxgamer.quickshop.files;

import io.github.portlek.bukkititembuilder.util.ColorUtil;
import io.github.portlek.configs.BukkitLinkedManaged;
import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.LinkedConfig;
import io.github.portlek.configs.annotations.LinkedFile;
import io.github.portlek.configs.annotations.Value;
import io.github.portlek.configs.util.FileType;
import io.github.portlek.configs.util.MapEntry;
import io.github.portlek.configs.util.Replaceable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

@LinkedConfig(files = {
    @LinkedFile(
        key = "tr-TR",
        config = @Config(
            name = "messages",
            type = FileType.JSON,
            location = "lang/tr-TR"
        )
    ),
    @LinkedFile(
        key = "en-US",
        config = @Config(
            name = "messages",
            type = FileType.JSON,
            location = "lang/en-US"
        )
    ),
})
public final class LanguageFile extends BukkitLinkedManaged {

    /**
     * Default values from lang-original/messages.json
     * If there is no value at section path.
     */
    @Value
    public Replaceable<String> translation_author = Replaceable.of("Translator: Ghost_chu, Andre_601");

    @Value
    public Replaceable<String> unknown_player = Replaceable.of("&cTarget, called %player_name%, player doesn't exist, please check the username you typed.")
        .replace(this.getPrefix())
        .replaces("%player_name%")
        .map(ColorUtil::colored);

    public LanguageFile(@NotNull final ConfigFile configFile) {
        super(() -> configFile.plugin_language, MapEntry.from("config", configFile));
    }

    @NotNull
    public ConfigFile getConfigFile() {
        return (ConfigFile) this.pull("config").orElseThrow(() ->
            new IllegalStateException("LanguageFile class couldn't load correctly."));
    }

    @NotNull
    public Map<String, Supplier<String>> getPrefix() {
        final Map<String, Supplier<String>> prefix = new HashMap<>();
        prefix.put("%prefix%", () -> this.getConfigFile().plugin_language);
        return prefix;
    }

}
