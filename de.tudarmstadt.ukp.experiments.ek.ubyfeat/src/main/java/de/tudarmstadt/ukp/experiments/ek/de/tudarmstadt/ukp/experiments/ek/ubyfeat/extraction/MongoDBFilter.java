package de.tudarmstadt.ukp.experiments.ek.de.tudarmstadt.ukp.experiments.ek.ubyfeat.extraction;

import java.net.UnknownHostException;
import java.util.Iterator;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.Mongo;

public class MongoDBFilter
    extends Filter
{

    private final DB db;
    private final String collectionName;
    private final String field;
    private final String collectionOutputName;
    private final DBCollection collection;
    private final String mapFunc;

    private final String reduceFunc;
    MapReduceCommand cmd;

    private MapReduceOutput out;

    public MongoDBFilter(DB db, String collectionName, String field)
    {
        // TODO Auto-generated constructor stub
        this.db = db;
        this.collectionName = collectionName;
        this.collection = db.getCollection(this.collectionName);
        this.field = field;
        this.collectionOutputName = this.collectionName+"_"+this.field + "_counts";

        this.mapFunc = "function(){" + "emit(this." + this.field + ",{counts: 1}) };";

        this.reduceFunc = "function(key, values)" +
        		" { " +
        		"total = 0; " +
        		"for (var i = 0; i < values.length; ++i) { total += values[i].counts; };" +
        		"return {counts: total}; " +
        		"}" +
        		";";

    }

    /**
     * will delete all the instances in database where contain a relation with frequency less then
     * minCount
     *
     * @param minCount
     */
    public void filterRelations(int minCount)
    {
        // check if outputcollection exists ???

        this.cmd = new MapReduceCommand(this.collection, mapFunc, reduceFunc,
                this.collectionOutputName, MapReduceCommand.OutputType.REDUCE, null);

        // db.relation_counts_2.find( {value: {$lt : {counts:minCount} }} )

        this.collection.mapReduce(this.cmd);
        DBCollection collectionOutput = db.getCollection(this.collectionOutputName);
        // DBObject query = new BasicDBObject("value", " {$lt : {counts:5000} }");
        DBObject query = new BasicDBObject("value", new BasicDBObject("$lt", new BasicDBObject(
                "counts", minCount)));
        DBCursor cursor = collectionOutput.find(query);
        Iterator<DBObject> it = cursor.iterator();
        DBObject result;
        //List<DBObject> results = new ArrayList<DBObject>();

        while (it.hasNext()) {
            result = it.next();
            System.out.println(result.get("_id"));
            //results.add((DBObject) result.get("_id"));
//            this.collection.remove( new BasicDBObject(this.field,result.get("_id")));
        }



    }

    public void filterEntities(int minCount){
        /*
         *
         *
         * entity_counts:
         *      Map: function(){ emit(this.leftHandSide.ubyId, {count:1});
         *                  emit(this.rightHandSide.ubyId, {count:1});
         *                 }
         *      Reduce: function(key, values) { total = 0; for (var i = 0; i < values.length; ++i)
         *          { total += values[i].count; }; return {count: total}; }
         *      output: merge
         *      output collection: entity_counts
         * find entities with frequency smaller than mincut, only names:
         *      cur=db.entity_counts_3.find({"value.count":{$lt:mincut}  }, {"_id": 1} )
         *
         *

         */

        // Map_Reduce should be done beforehand, incomplete implementation of method
        DBCollection collectionOutput = db.getCollection("entity_counts");
        DBCollection collectionExt = db.getCollection(this.collectionName+"_externalRef");
        // DBObject query = new BasicDBObject("value", " {$lt : {counts:5000} }");
        DBObject query = new BasicDBObject("value", new BasicDBObject("$lt", new BasicDBObject(
                "count", minCount)));
        DBCursor cursor = collectionOutput.find(query);

        Iterator<DBObject> it = cursor.iterator();
        DBObject result;
        //List<DBObject> results = new ArrayList<DBObject>();

        while (it.hasNext()) {
            result = it.next();
            System.out.println(result.get("_id"));
            //results.add((DBObject) result.get("_id"));
            String lhs_ubyId = "leftHandSide.ubyId";
            String rhs_ubyId = "rightHandSide.ubyId";
            collectionExt.remove(new BasicDBObject("ubyId",result.get("_id")));
            this.collection.remove( new BasicDBObject(lhs_ubyId,result.get("_id")));
            this.collection.remove( new BasicDBObject(rhs_ubyId,result.get("_id")));
        }
        //System.out.println(cursor.length());
    }
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        // TODO Auto-generated method stub
        Mongo mongo;
        try {
            mongo = new Mongo();
            DB db = mongo.getDB("germanet");
            MongoDBFilter filter = new MongoDBFilter(db, "Features_GermaNet", "relation");
            filter.filterEntities(4);


        }
        catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
