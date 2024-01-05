package dev.portero.xenon.config;

import lombok.Getter;

@Getter
public enum ConfigFileNames {

    ITEMS("items.yml"),
    SKILLS("skills.yml"),
    QUESTS("quests.yml"),
    NPCS("npcs.yml"),
    HOLOGRAMS("holograms.yml"),
    CHESTS("chests.yml");

    private final String fileName;

    ConfigFileNames(String fileName) {
        this.fileName = fileName;
    }
}
