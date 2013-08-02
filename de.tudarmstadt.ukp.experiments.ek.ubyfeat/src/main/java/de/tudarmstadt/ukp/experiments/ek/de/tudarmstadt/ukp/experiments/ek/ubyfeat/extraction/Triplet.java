package de.tudarmstadt.ukp.experiments.ek.de.tudarmstadt.ukp.experiments.ek.ubyfeat.extraction;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;


public class Triplet
{
    private Entity leftHandSide;
    private Entity rightHandSide;
    private Relation relation;
    private BasicDBObject mongoObject;
    public Triplet(Entity leftHandSide, Entity rightHandSide, Relation relation)
    {
        super();
        this.leftHandSide = leftHandSide;
        this.rightHandSide = rightHandSide;
        this.relation = relation;
    }


    /**
     *
     */
    public List<String> generalizeTriplet(){
        List<String> generalizedTriplets = new ArrayList<String>();
        for (int LHSlevel=0; LHSlevel<1 ; LHSlevel++ ){
            for (int RHSlevel=0; RHSlevel<1 ; RHSlevel++){
                generalizedTriplets.add(this.leftHandSide.getGeneralLevelName(LHSlevel)+"\t"+
                                       this.relation+"\t"+
                                        this.rightHandSide.getGeneralLevelName(RHSlevel));


            }
        }
        return generalizedTriplets;

    }
    /**
     * @return the leftHandSide
     */
    public Entity getLeftHandSide()
    {
        return leftHandSide;
    }



    /**
     * @param leftHandSide the leftHandSide to set
     */
    public void setLeftHandSide(Entity leftHandSide)
    {
        this.leftHandSide = leftHandSide;
    }



    /**
     * @return the rightHandSide
     */
    public Entity getRightHandSide()
    {
        return rightHandSide;
    }



    /**
     * @param rightHandSide the rightHandSide to set
     */
    public void setRightHandSide(Entity rightHandSide)
    {
        this.rightHandSide = rightHandSide;
    }



    /**
     * @return the relation
     */
    public Relation getRelation()
    {
        return relation;
    }



    /**
     * @param relation the relation to set
     */
    public void setRelation(Relation relation)
    {
        this.relation = relation;
    }



    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((leftHandSide == null) ? 0 : leftHandSide.hashCode());
        result = prime * result + ((relation == null) ? 0 : relation.hashCode());
        result = prime * result + ((rightHandSide == null) ? 0 : rightHandSide.hashCode());
        return result;
    }



    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Triplet)) {
            return false;
        }
        Triplet other = (Triplet) obj;
        if (leftHandSide == null) {
            if (other.leftHandSide != null) {
                return false;
            }
        }
        else if (!leftHandSide.equals(other.leftHandSide)) {
            return false;
        }
        if (relation == null) {
            if (other.relation != null) {
                return false;
            }
        }
        else if (!relation.equals(other.relation)) {
            return false;
        }
        if (rightHandSide == null) {
            if (other.rightHandSide != null) {
                return false;
            }
        }
        else if (!rightHandSide.equals(other.rightHandSide)) {
            return false;
        }
        return true;
    }

    public BasicDBObject getMongoObject(){
        if (this.mongoObject==null){
            this.mongoObject = new BasicDBObject("leftHandSide",this.leftHandSide.getMongoObject())
                              .append("rightHandSide",this.rightHandSide.getMongoObject())
                              .append("relation", this.relation.getMongoObject())
                              ;
            return this.mongoObject;
        }else{
            return mongoObject;
        }
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return leftHandSide+"\t"+relation+"\t"+rightHandSide;
    }



    public static void main(String[] args)
    {

    }

}
