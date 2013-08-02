package de.tudarmstadt.ukp.experiments.ek.de.tudarmstadt.ukp.experiments.ek.ubyfeat.extraction;

import java.util.List;

public interface DataBaseDumper
{

    public List<Triplet> findTripletByEntity(Entity entity);
    public List<Triplet> findTripletByRelation(Relation relation);
    public List<Triplet> findTripletByRelationType(String type);
    public List<Entity> findEntityByRelation (Relation relation);




}
