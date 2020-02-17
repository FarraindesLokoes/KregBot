package nukeologist.kregbot.data.character;

import java.util.Locale;

public enum CharacterAbility {

    STRENGTH("Strength"),
    DEXTERITY("Dexterity"),
    CONSTITUTION("Constitution"),
    INTELLIGENCE("Intelligence"),
    WISDOM("Wisdom"),
    CHARISMA("Charisma");

    private final String name;

    CharacterAbility(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static CharacterAbility fromName(final String ability) {
        return CharacterAbility.valueOf(ability.toUpperCase(Locale.ROOT));
    }
}
