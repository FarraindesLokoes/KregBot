package nukeologist.kregbot.data;

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

    /*Getters and Setters of RawStats */
    public int getRawStrength() {
        return RAWSTRENGTH;
    }

    public void setRawStrength(int RAWSTRENGTH) {
        this.RAWSTRENGTH = RAWSTRENGTH;
    }

    public int getRawDexterity() {
        return RAWDEXTERITY;
    }

    public void setRawDexterity(int RAWDEXTERITY) {
        this.RAWDEXTERITY = RAWDEXTERITY;
    }

    public int getRawConstitution() {
        return RAWCONSTITUTION;
    }

    public void setRawConstitution(int RAWCONSTITUTION) {
        this.RAWCONSTITUTION = RAWCONSTITUTION;
    }

    public int getRawIntelligence() {
        return RAWINTELLIGENCE;
    }

    public void setRawIntelligence(int RAWINTELLIGENCE) {
        this.RAWINTELLIGENCE = RAWINTELLIGENCE;
    }

    public int getRawWisdom() {
        return RAWWISDOM;
    }

    public void setRawWisdom(int RAWWISDOM) {
        this.RAWWISDOM = RAWWISDOM;
    }

    public int getRawCharisma() {
        return RAWCHARISMA;
    }

    public void setRawCharisma(int RAWCHARISMA) {
        this.RAWCHARISMA = RAWCHARISMA;
    }
    /*End of rawStats Getters and Setters*/

    private int modifierStat(int stat) {
        return (stat - 10) / 2;
    }

    public int getStrengthModifier() {
        return modifierStat(RAWSTRENGTH);
    }

    public int getDexterityModifier() {
        return modifierStat(RAWDEXTERITY);
    }

    public int getConstitutionModifier() {
        return modifierStat(RAWCONSTITUTION);
    }

    public int getIntelligenceModifier() {
        return modifierStat(RAWINTELLIGENCE);
    }

    public int getWisdomModifier() {
        return modifierStat(RAWWISDOM);
    }

    public int getCharismaModifier() {
        return modifierStat(RAWCHARISMA);
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public long getOwnerName() {
        return OWNERID;
    }
}
