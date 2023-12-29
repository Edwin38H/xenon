package dev.portero.xenon.locale;

import dev.portero.xenon.Xenon;
import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.util.UTF8ResourceBundleControl;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class LocaleManager {

    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    private final Xenon plugin;
    private final Set<Locale> installed = ConcurrentHashMap.newKeySet();
    private TranslationRegistry registry;

    @Getter
    private final Path translationsDirectory;


    public LocaleManager(Xenon plugin) {
        this.plugin = plugin;

        translationsDirectory = plugin.getDataFolder().toPath().resolve("locale");
    }

    public Set<Locale> getInstalledLocales() {
        return Collections.unmodifiableSet(this.installed);
    }

    public void reload() {
        if (this.registry != null) {
            GlobalTranslator.translator().removeSource(this.registry);
            this.installed.clear();
        }

        this.registry = TranslationRegistry.create(Key.key("xenon", "main"));
        this.registry.defaultLocale(DEFAULT_LOCALE);

        loadCustomLocales();
        loadDefaultLocale();

        GlobalTranslator.translator().addSource(this.registry);
    }

    private void loadDefaultLocale() {
        loadFromResourceBundle(DEFAULT_LOCALE);
    }

    private void loadCustomLocales() {
        Locale spanish = new Locale("es");

        loadFromResourceBundle(spanish);
    }

    private void loadFromResourceBundle(Locale spanish) {
        ResourceBundle es = ResourceBundle.getBundle("locale.xenon", spanish, UTF8ResourceBundleControl.get());
        try {
            this.registry.registerAll(spanish, es, false);
        } catch (IllegalArgumentException e) {
            if (!isAdventureDuplicatesException(e)) {
                this.plugin.getLogger().log(Level.WARNING, "Error loading default locale file", e);
            }
        }
    }

    private static boolean isAdventureDuplicatesException(Exception e) {
        return e instanceof IllegalArgumentException && (e.getMessage().startsWith("Invalid key") || e.getMessage().startsWith("Translation already exists"));
    }
}
