package de.tudarmstadt.ukp.experiments.ek.de.tudarmstadt.ukp.experiments.ek.ubyfeat.extraction;

public enum EntityTypes
{
    SENSE("sense"), SYNSET("synset"), LEXICAL_ENTRY("lexical_entry");

    private String label;

    private EntityTypes(String lablel)
    {
        this.label = lablel;
    }

    public String getLabel()
    {
        return this.label;
    }
}
