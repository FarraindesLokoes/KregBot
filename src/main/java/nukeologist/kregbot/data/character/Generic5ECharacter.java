package nukeologist.kregbot.data.character;

public abstract class Generic5ECharacter {

    private String NAME;
    private final long OWNERID;

    public Generic5ECharacter(String name, long OWNERID) {
        this.NAME = name;
        this.OWNERID = OWNERID;
    }

    private int RAWSTRENGTH;
    private int RAWDEXTERITY;
    private int RAWCONSTITUTION;
    private int RAWINTELLIGENCE;
    private int RAWWISDOM;
    private int RAWCHARISMA;

    public final void setAbility(final CharacterAbility ability, final int value) {
        //java 11 doesn't have switch expressions yet (it's on 12)
        switch (ability) {
            case STRENGTH:
                this.RAWSTRENGTH = value;
                break;
            case DEXTERITY:
                this.RAWDEXTERITY = value;
                break;
            case CONSTITUTION:
                this.RAWCONSTITUTION = value;
                break;
            case INTELLIGENCE:
                this.RAWINTELLIGENCE = value;
                break;
            case WISDOM:
                this.RAWWISDOM = value;
                break;
            case CHARISMA:
                this.RAWCHARISMA = value;
                break;
            default:
                throw new RuntimeException("UNKNOWN or null ability!");
        }
    }

    public final int getAbilityScore(final CharacterAbility ability) {
        switch (ability) {
            case STRENGTH:
                return this.RAWSTRENGTH;
            case DEXTERITY:
                return this.RAWDEXTERITY;
            case CONSTITUTION:
                return this.RAWCONSTITUTION;
            case INTELLIGENCE:
                return this.RAWINTELLIGENCE;
            case WISDOM:
                return this.RAWWISDOM;
            case CHARISMA:
                return this.RAWCHARISMA;
            default:
                throw new RuntimeException("UNKNOWN or null ability!");
        }
    }

    private int modifierStat(final int stat) {
        return (stat - 10) / 2;
    }

    public final int getModifier(final CharacterAbility ability) {
        switch (ability) {
            case STRENGTH:
                return modifierStat(this.RAWSTRENGTH);
            case DEXTERITY:
                return modifierStat(this.RAWDEXTERITY);
            case CONSTITUTION:
                return modifierStat(this.RAWCONSTITUTION);
            case INTELLIGENCE:
                return modifierStat(this.RAWINTELLIGENCE);
            case WISDOM:
                return modifierStat(this.RAWWISDOM);
            case CHARISMA:
                return modifierStat(this.RAWCHARISMA);
            default:
                throw new RuntimeException("UNKNOWN or null ability!");
        }
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(final String NAME) {
        this.NAME = NAME;
    }

    public final long getOwnerName() {
        return OWNERID;
    }
}
