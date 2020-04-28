package org.maxgamer.quickshop.files;

import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.LinkedConfig;
import io.github.portlek.configs.util.FileType;

@LinkedConfig(configs = {
    @Config(
        name = "tr",
        type = FileType.JSON,
        location = "%basedir%/QuickShop-Era",
        copyDefault = true,
        resourcePath = ""
    ),
    @Config(
        name = "en",
        type = FileType.JSON,
        location = "%basedir%/QuickShop-Era",
        copyDefault = true,
        resourcePath = ""
    )
})
public final class LanguageFile {

}
