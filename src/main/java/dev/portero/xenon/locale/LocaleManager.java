package dev.portero.xenon.locale;

import dev.portero.xenon.Xenon;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.util.UTF8ResourceBundleControl;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Manages the loading and handling of locale-specific resource bundles for Xenon.
 * This class is responsible for initializing and updating the translation registry with various locales.
 */
public class LocaleManager {

    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH; // Xenon Default Language

    private final Xenon plugin;
    private final Set<Locale> installed = ConcurrentHashMap.newKeySet();
    private TranslationRegistry registry;

    /**
     * Constructs a LocaleManager for managing application locales.
     *
     * @param plugin The main class of the Xenon application.
     */
    public LocaleManager(Xenon plugin) {
        this.plugin = plugin;
    }

    /**
     * Reloads and updates the translation registry with the available locales.
     * It loads the default locale and any custom locales specified.
     */
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

    /**
     * Loads the default locale into the translation registry.
     */
    private void loadDefaultLocale() {
        loadFromResourceBundle(DEFAULT_LOCALE);
    }

    /**
     * Loads custom locales into the translation registry.
     * Currently, this method only explicitly loads the Spanish locale.
     * TODO: Implement a system to automatically detect and load available language resource bundles.
     */
    private void loadCustomLocales() {
        loadFromResourceBundle(new Locale("es"));
        /*
        Future implementation: Automatically discover and load all available locales
        for (Locale locale : discoverAvailableLocales()) {
            loadFromResourceBundle(locale);
        }
        */
    }

    /**
     * Loads a resource bundle for a given locale and registers it with the translation registry.
     *
     * @param locale The locale for which the resource bundle will be loaded.
     */
    private void loadFromResourceBundle(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("locale.xenon", locale, UTF8ResourceBundleControl.get());
        try {
            this.registry.registerAll(locale, bundle, false);
        } catch (IllegalArgumentException e) {
            if (!isAdventureDuplicatesException(e)) {
                this.plugin.getLogger().log(Level.WARNING, "Error loading locale file for " + locale, e);
            }
        }
    }

    /**
     * Checks if an exception is due to duplicate key registration in Adventure's translation registry.
     *
     * @param e The exception to check.
     * @return True if the exception is due to duplicate key registration.
     */
    private static boolean isAdventureDuplicatesException(Exception e) {
        return e instanceof IllegalArgumentException && (e.getMessage().startsWith("Invalid key") || e.getMessage().startsWith("Translation already exists"));
    }
}
