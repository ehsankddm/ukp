package de.tudarmstadt.ukp.experiments.ek.de.tudarmstadt.ukp.experiments.ek.ubyfeat.extraction;

import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;

import de.tudarmstadt.ukp.lmf.exceptions.UbyInvalidArgumentException;
import de.tudarmstadt.ukp.lmf.transform.DBConfig;

public class MongoDbDumper
    extends Dumper
    implements DataBaseDumper
{

    private final DB db;

    public MongoDbDumper(DB db, String collectionName)
    {
        super(collectionName);
        this.db = db;
        DBObject obj = new BasicDBObject("ubyId", 1);
        // obj.put("relation", 1);

        try{
            this.db.getCollection(this.resourceName + "_externalRef").ensureIndex(obj, null, true);
        }catch (MongoException e){
            System.out.println("already ensured index ***\n***");
        }

    }

    @Override
    public void dumpTriplets(Triplet triplet)
    {
        DBCollection coll = db.getCollection(this.resourceName);

        coll.insert(triplet.getMongoObject(), WriteConcern.NORMAL); // check for optimization

    }

    @Override
    public void dumpTriplets(List<Triplet> triplets)
    {
        // TODO Auto-generated method stub
        DBCollection dumpeeCollection = db.getCollection(this.resourceName);
        DBCollection externalRefCollection = db.getCollection(this.resourceName + "_externalRef");
        BasicDBObject[] objects = new BasicDBObject[triplets.size()];

        for (int i = 0; i < objects.length; ++i) {
            objects[i] = triplets.get(i).getMongoObject();

            BasicDBObject leftHandSide = new BasicDBObject();
            BasicDBObject rightHandSide = new BasicDBObject();
            leftHandSide.put("ubyId", triplets.get(i).getLeftHandSide().getUbyID());
            leftHandSide.put("externalRefId", triplets.get(i).getLeftHandSide().getExternalRefID());
            leftHandSide.put("sourceLexicon", triplets.get(i).getLeftHandSide().getSourceLexicon());
            rightHandSide.put("ubyId", triplets.get(i).getRightHandSide().getUbyID());
            rightHandSide.put("externalRefId", triplets.get(i).getRightHandSide().getExternalRefID());
            rightHandSide.put("sourceLexicon", triplets.get(i).getRightHandSide().getSourceLexicon());
            try{
                externalRefCollection.insert(leftHandSide, WriteConcern.NORMAL);
            } catch (MongoException e){
                System.out.println("already added ***\n***");
            }

            externalRefCollection.insert(rightHandSide, WriteConcern.NORMAL);


        }

        dumpeeCollection.insert(objects, WriteConcern.NORMAL); // check for optimization

    }

    public List<Triplet> findTripletByEntity(Entity entity)
    {
        // TODO Auto-generated method stub
        DBCollection coll = db.getCollection(this.resourceName);
        return null;
    }

    public List<Triplet> findTripletByRelation(Relation relation)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Triplet> findTripletByRelationType(String type)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Entity> findEntityByRelation(Relation relation)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void close()
    {
        // TODO Auto-generated method stub
        return;

    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        // TODO Auto-generated method stub
        Mongo mongo;
        try {
            DBConfig db = new DBConfig("localhost/uby_wn_gn", "com.mysql.jdbc.Driver", "mysql",
                    "root", "ehsanukp", false);
            mongo = new Mongo();
            DB dumpDB = mongo.getDB("test");
            Dumper dumper = new MongoDbDumper(dumpDB, "Features_GermaNet_WordNet");

            SenseRelationExtraction relExtractorGermaNet =
                    new SenseRelationExtraction(db, "GermaNet");

            SenseRelationExtraction relExtractorWordNet =
                    new SenseRelationExtraction(db, "WordNet");
            WordNetSynsetRelationExtraction wnRelExtractor =
                    new WordNetSynsetRelationExtraction(db);

            SenseAxisRelationExtraction senseAxisRelExtractor = new SenseAxisRelationExtraction(db,
                    "WordNet","GermaNet");

            relExtractorGermaNet.dumpRelations(dumper);
            relExtractorWordNet.dumpRelations(dumper);
            wnRelExtractor.dumpRelations(dumper);
            senseAxisRelExtractor.dumpRelations(dumper);

            dumper.close();

        }
        catch (UbyInvalidArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
