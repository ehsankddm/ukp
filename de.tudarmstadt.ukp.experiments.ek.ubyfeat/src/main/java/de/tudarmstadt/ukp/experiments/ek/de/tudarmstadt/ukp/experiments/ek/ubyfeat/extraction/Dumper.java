package de.tudarmstadt.ukp.experiments.ek.de.tudarmstadt.ukp.experiments.ek.ubyfeat.extraction;

import java.util.List;

public abstract class Dumper
{
    protected String resourceName;

    protected Dumper(String resourceName)
    {
        this.resourceName = resourceName;
    }

    public String getResourceName()
    {
        return this.resourceName;
    }

    public void setResourceName(String resourceName)
    {
        this.resourceName = resourceName;
    }

    public abstract void dumpTriplets(Triplet triplet);

    public abstract void dumpTriplets(List<Triplet> triplets);

    public abstract void close();

}
