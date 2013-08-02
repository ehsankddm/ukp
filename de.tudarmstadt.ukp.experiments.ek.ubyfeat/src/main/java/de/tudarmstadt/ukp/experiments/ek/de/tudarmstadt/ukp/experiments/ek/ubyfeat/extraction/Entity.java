/**
 *
 */
package de.tudarmstadt.ukp.experiments.ek.de.tudarmstadt.ukp.experiments.ek.ubyfeat.extraction;

import com.mongodb.BasicDBObject;

import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;

/**
 * @author khoddam
 *
 */
public class Entity
{

    /**
     * @param args
     */

    private String ubyID; // Uby ID
    private String externalRefID;  //sourceLexicon ID
    private EntityTypes type;
    private EPartOfSpeech pos;
    private String sourceLexicon;
    private String lemma;
    private String resource;
    private String pureId;
    private BasicDBObject mongoObject=null;

    public Entity(String ubyID, String externalRefID, EntityTypes type, String lemma, EPartOfSpeech pos,
            String sourceLexicon)
    {
        super();
        this.ubyID = ubyID.replaceAll("\\s+", "##");
        this.externalRefID = externalRefID;
        this.type = type;
        this.pos = pos;
        this.sourceLexicon = sourceLexicon;
        this.lemma = lemma.replaceAll("\\s+", "##");
        String[] tokens = this.ubyID.split("_");
        if (tokens.length == 3) {
            this.resource = tokens[0].replaceAll("\\s+", "##");
            this.pureId = tokens[1] + "_" + tokens[2];
            this.pureId = this.pureId.replaceAll("\\s+", "##");
        } else if (tokens.length == 4){
            this.resource= tokens[0]+"_"+tokens[1];
            this.resource = this.resource.replaceAll("\\s+", "##");
            this.pureId = tokens[2] + "_" + tokens[3];
            this.pureId= this.pureId.replaceAll("\\s+", "##");
        }
        else {
            System.out.println(ubyID);
            for (String token:tokens){
                System.out.println(token);
            }

            throw new RuntimeException("Error tokenizing synset id");
        }

    }

    // May consider faster way of doing this but saving it in a field and just read from it
    public String getGeneralLevelName(int level)
    {
        switch (level) {
        case 0:
            return this.resource + "_" + this.lemma + "_" + pureId + "_" + pos;
        case 1:
            return this.resource + "_" + this.lemma + "_" + pos;
        case 2:
            return this.resource + "_" + this.lemma;

        default: return this.resource + "_" + this.lemma + "_" + pureId + "_" + pos;

        }

    }

    /**
     * @return the id
     */
    public String getUbyID()
    {
        return ubyID;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setUbyID(String ubyID)
    {
        this.ubyID = ubyID.replaceAll("\\s+", "##");
    }
    /**
     * @return the externalRefId
     */
    public String getExternalRefID()
    {
        return externalRefID;
    }

    /**
     * @param ubyID
     *            the externalRefId to set
     */
    public void setExternalRefID(String externalRefID)
    {
        this.externalRefID = externalRefID;
    }

    /**
     * @return the type
     */
    public EntityTypes getType()
    {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(EntityTypes type)
    {
        this.type = type;
    }

    /**
     * @return the pos
     */
    public EPartOfSpeech getPos()
    {
        return pos;
    }

    /**
     * @param pos
     *            the pos to set
     */
    public void setPos(EPartOfSpeech pos)
    {
        this.pos = pos;
    }

    /**
     * @return the source
     */
    public String getSourceLexicon()
    {
        return sourceLexicon;
    }

    /**
     * @param source
     *            the source to set
     */
    public void setSourceLexicon(String sourceLexicon)
    {
        this.sourceLexicon = sourceLexicon;
    }

    /**
     * @return the lemma
     */
    public String getLemma()
    {
        return lemma;
    }

    /**
     * @param lemma
     *            the lemma to set
     */
    public void setLemma(String lemma)
    {
        this.lemma = lemma.replaceAll("\\s+", "##");
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ubyID == null) ? 0 : ubyID.hashCode());
        result = prime * result + ((pos == null) ? 0 : pos.hashCode());
        result = prime * result + ((sourceLexicon == null) ? 0 : sourceLexicon.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     *
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
        if (!(obj instanceof Entity)) {
            return false;
        }
        Entity other = (Entity) obj;
        if (ubyID == null) {
            if (other.ubyID != null) {
                return false;
            }
        }
        else if (!ubyID.equals(other.ubyID)) {
            return false;
        }
        if (pos != other.pos) {
            return false;
        }
        if (sourceLexicon == null) {
            if (other.sourceLexicon != null) {
                return false;
            }
        }
        else if (!sourceLexicon.equals(other.sourceLexicon)) {
            return false;
        }
        if (type != other.type) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        // sourceLexicon+"_"+type+":"+id+"_"+pos;
        return  this.resource + "_" + this.lemma + "_" + pureId + "_" + pos;
    }
    public BasicDBObject getMongoObject(){
        if (this.mongoObject==null){
            this.mongoObject = new BasicDBObject("ubyId",this.ubyID)
                              .append("externalRefId",this.externalRefID)
                              .append("type", this.type.toString())
                              .append("lemma",this.lemma)
                              .append("pos", this.pos.toString())
                              .append("sourceLexicon",this.sourceLexicon)
                              ;
            return this.mongoObject;
        }else{
            return mongoObject;
        }
    }
    public static void main(String[] args)
    {
        // TODO Auto-generated method stub

    }

}
