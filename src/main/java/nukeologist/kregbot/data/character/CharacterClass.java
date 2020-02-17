package nukeologist.kregbot.data.character;

import nukeologist.kregbot.util.Tuple;

import java.util.Locale;

public enum CharacterClass {

    BARBARIAN("Barbarian", 12, CharacterAbility.STRENGTH, CharacterAbility.CONSTITUTION),
    BARD("Bard", 8, CharacterAbility.DEXTERITY, CharacterAbility.CHARISMA),
    CLERIC("Cleric", 8, CharacterAbility.WISDOM, CharacterAbility.CHARISMA),
    DRUID("Druid", 8, CharacterAbility.INTELLIGENCE, CharacterAbility.WISDOM),
    FIGHTER("Fighter", 10, CharacterAbility.STRENGTH, CharacterAbility.CONSTITUTION),
    MONK("Monk", 8, CharacterAbility.STRENGTH, CharacterAbility.DEXTERITY),
    PALADIN("Paladin", 10, CharacterAbility.WISDOM, CharacterAbility.CHARISMA),
    RANGER("Ranger", 10, CharacterAbility.STRENGTH, CharacterAbility.DEXTERITY),
    ROGUE("Rogue", 8, CharacterAbility.DEXTERITY, CharacterAbility.INTELLIGENCE),
    SORCERER("Sorcerer", 6, CharacterAbility.CONSTITUTION, CharacterAbility.CHARISMA),
    WARLOCK("Warlock", 8, CharacterAbility.WISDOM, CharacterAbility.CHARISMA),
    WIZARD("Wizard", 6, CharacterAbility.INTELLIGENCE, CharacterAbility.WISDOM);

    private final String name;
    private final int hitDie;
    private final Tuple<CharacterAbility, CharacterAbility> savingThrowProficiency;

    CharacterClass(String name, int hitDie, CharacterAbility ab1, CharacterAbility ab2) {
        this.name = name;
        this.hitDie = hitDie;
        this.savingThrowProficiency = Tuple.of(ab1, ab2);
    }

    public int getHitDice() {
        return this.hitDie;
    }

    public Tuple<CharacterAbility, CharacterAbility> getSavingThrowProficiency() {
        return savingThrowProficiency;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static CharacterClass fromName(final String clazz) {
        return CharacterClass.valueOf(clazz.toUpperCase(Locale.ROOT));
    }
}
