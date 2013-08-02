/**
 *
 */
package de.tudarmstadt.ukp.experiments.ek.de.tudarmstadt.ukp.experiments.ek.ubyfeat.extraction;

import com.mongodb.BasicDBObject;

/**
 * @author khoddam
 *
 */
public class Relation
{

    /**
     * @param args
     */
    private String type;
    private String name;
    private BasicDBObject mongoObject=null;


    public Relation(String type, String name)
    {

        this.type = type;
        this.name = name;
    }


    /**
     * @return the type
     */
    public String getType()
    {
        return type;
    }


    /**
     * @param type the type to set
     */
    public void setType(String type)
    {
        this.type = type;
    }


    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }


    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }



    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        if (!(obj instanceof Relation)) {
            return false;
        }
        Relation other = (Relation) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        }
        else if (!name.equals(other.name)) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        }
        else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }

    public BasicDBObject getMongoObject(){
        if (this.mongoObject==null){
            this.mongoObject = new BasicDBObject("name",this.name)
                              .append("type", this.type.toString())
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
        return type + ":" + name;
    }



    public static void main(String[] args)
    {
        // TODO Auto-generated method stub

    }

}
