/**
 *
 */
package de.tudarmstadt.ukp.experiments.ek.de.tudarmstadt.ukp.experiments.ek.ubyfeat.extraction;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author khoddam
 *
 */
public class StatisticsExtractor
{

    private PrintWriter writer;
    private final Map<String, Integer> counts;

    /**
     *
     */
    public StatisticsExtractor(String fileName)
    {
        // TODO Auto-generated constructor stub
        counts = new HashMap<String, Integer>();
        try {
            writer = new PrintWriter(fileName);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void addData(List<Triplet> triples)
    {
        Integer count;
        for (Triplet triple : triples) {
            if (counts.containsKey(triple.getRelation().toString())) {
                count = counts.get(triple.getRelation().toString());
                counts.put(triple.getRelation().toString(), count + 1);
            }
            else {
                counts.put(triple.getRelation().toString(), 1);
            }
        }
    }

    /**
     *
     */
    public void dumpStatistics(String message)
    {
        writer.println(message);
        for (Entry<String, Integer> entry : counts.entrySet()) {
            writer.println(entry.getKey() + "\t" + entry.getValue());
        }
        writer.flush();
        counts.clear();

    }

    public void close(){
        counts.clear();
        writer.close();
    }

}
