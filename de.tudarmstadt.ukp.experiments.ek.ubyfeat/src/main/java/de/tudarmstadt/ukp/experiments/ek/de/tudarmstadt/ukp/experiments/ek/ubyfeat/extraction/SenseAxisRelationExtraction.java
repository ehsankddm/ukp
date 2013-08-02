/**
 *
 */
package de.tudarmstadt.ukp.experiments.ek.de.tudarmstadt.ukp.experiments.ek.ubyfeat.extraction;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tudarmstadt.ukp.lmf.exceptions.UbyInvalidArgumentException;
import de.tudarmstadt.ukp.lmf.model.core.Lexicon;
import de.tudarmstadt.ukp.lmf.model.core.Sense;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;
import de.tudarmstadt.ukp.lmf.model.multilingual.SenseAxis;
import de.tudarmstadt.ukp.lmf.model.semantics.Synset;
import de.tudarmstadt.ukp.lmf.transform.DBConfig;

/**
 * @author khoddam
 *
 */
public class SenseAxisRelationExtraction
    extends UbyRelationExtraction
{

    private final Log log = LogFactory.getLog(SenseAxisRelationExtraction.class);
    private String sourceLexiconName;
    private String targetLexiconName;

    /**
     * @param db
     * @throws UbyInvalidArgumentException
     */
    private SenseAxisRelationExtraction(DBConfig db)
        throws UbyInvalidArgumentException
    {
        super(db);
        // super.statisticsExtractor = new StatisticsExtractor("WN_SenseAxis.stat");
    }

    public SenseAxisRelationExtraction(DBConfig db, String sourceLexiconName)
        throws UbyInvalidArgumentException
    {
        this(db);
        this.sourceLexiconName = sourceLexiconName;

    }

    public SenseAxisRelationExtraction(DBConfig db, String sourceLexiconName,
            String targetLexiconName)
        throws UbyInvalidArgumentException
    {
        this(db, sourceLexiconName);
        this.targetLexiconName = targetLexiconName;
        // super.statisticsExtractor = new StatisticsExtractor("WN_SenseAxis.stat");
    }

    /*
     * (non-Javadoc)
     *
     * @see de.tudarmstadt.ukp.experiments.ek.de.tudarmstadt.ukp.experiments.ek.ubyfeat.extraction.
     * UbyRelationExtraction#dumpRelations(java.io.PrintWriter)
     */
    @Override
    public void dumpRelations(Dumper dumper)
    {

        Lexicon lex;
        List<Triplet> triplets;
        try {
            lex = uby.getLexiconByName(this.sourceLexiconName);

            System.out.println("*******************************************************");
            if ((this.targetLexiconName.equals("GermaNet") || this.targetLexiconName
                    .equals("WordNet"))
                    && (this.sourceLexiconName.equals("GermaNet") || this.sourceLexiconName
                            .equals("WordNet"))) {

                Iterator<SenseAxis> senseAxisIterator = uby.getSenseAxisIterator();
                SenseAxis senseAxis;
                while (senseAxisIterator.hasNext()) {
                    senseAxis = senseAxisIterator.next();
                    triplets = getSenseAxisRelationsWordNetGermaNet(senseAxis);
                    dumper.dumpTriplets(triplets);
                    triplets.clear();
                }

            }
            else {

                Iterator<Sense> senseIterator = uby.getSenseIterator(lex);
                Sense sense;
                while (senseIterator.hasNext()) {

                    sense = senseIterator.next();
                    log.info(sense);

                    String lemma = sense.getLexicalEntry().getLemmaForm();

                    triplets = getSenseAxisRelations(sense);
                    triplets.clear();

                }
            }

            // this.statisticsExtractor.addData(triples);

        }
        catch (UbyInvalidArgumentException e) {

            e.printStackTrace();
        }

    }

    private List<Triplet> getSenseAxisRelationsWordNetGermaNet(SenseAxis senseAxis)
    {
        List<Triplet> saxList = new ArrayList<Triplet>();

        Triplet temp;
        if (senseAxis.getSynsetOne() == null || senseAxis.getSynsetOne().getId() == null
                || senseAxis.getSynsetTwo() == null || senseAxis.getSynsetTwo().getId() == null) {
            System.out.println("pashmmmmmmmmmmmmmmm");
            return saxList;

        }
        else {

            Synset synsetOne = senseAxis.getSynsetOne();
            Synset synsetTwo = senseAxis.getSynsetTwo();
            for (Sense senseOne : synsetOne.getSenses()) {
                for (Sense senseTwo : synsetTwo.getSenses()) {
                    String sourceLexicon = senseOne.getLexicalEntry().getLexicon().getName();
                    String targetLexicon = senseTwo.getLexicalEntry().getLexicon().getName();
                    EPartOfSpeech senseOnePos = senseOne.getLexicalEntry().getPartOfSpeech();
                    EPartOfSpeech senseTwoPos = senseTwo.getLexicalEntry().getPartOfSpeech();
                    if (this.targetLexiconName != null) {
                        if (!this.targetLexiconName.equals(sourceLexicon)
                                && !this.targetLexiconName.equals(targetLexicon))

                        {
                            return saxList;

                        }
                    }
                    String sourceExternalRefID = senseOne.getMonolingualExternalRefs().get(0)
                            .getExternalReference();
                    String targetExternalRefID = senseTwo.getMonolingualExternalRefs().get(0)
                            .getExternalReference();

                    String sourceLemma = senseOne.getLexicalEntry().getLemmaForm();
                    String targetLemma = senseTwo.getLexicalEntry().getLemmaForm();

                    Entity leftHandSide = new Entity(senseOne.getId(), sourceExternalRefID,
                            EntityTypes.SENSE, sourceLemma, senseOnePos, sourceLexiconName);

                    // Be careful about POS
                    Entity rightHandSide = new Entity(senseTwo.getId(), targetExternalRefID,
                            EntityTypes.SENSE, targetLemma, senseTwoPos, targetLexiconName);

                    Relation relation = new Relation(senseAxis.getSenseAxisType().name(),
                            "SenseAxis");

                    temp = new Triplet(leftHandSide, rightHandSide, relation);

                    // System.out.println(temp);
                    log.info(temp);
                    saxList.add(temp);
                }
            }

        }

        return saxList;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.tudarmstadt.ukp.experiments.ek.de.tudarmstadt.ukp.experiments.ek.ubyfeat.extraction.
     * UbyRelationExtraction#dumpRelationsStatistics(java.io.PrintWriter)
     */
    @Override
    public void dumpRelationsStatistics(PrintWriter writer)
    {

    }

    private List<Triplet> getSenseAxisRelations(Sense sense)
    {
        // TODO Auto-generated method stub
        List<Triplet> saxList = new ArrayList<Triplet>();

        for (SenseAxis senseAxis : this.uby.getSenseAxisBySense(sense)) {
            Triplet temp;
            if (senseAxis.getSenseOne() == null || senseAxis.getSenseOne().getId() == null
                    || senseAxis.getSenseTwo() == null || senseAxis.getSenseTwo().getId() == null) {
                System.out.println("pashmmmmmmmmmmmmmmm");
                continue;

            }
            else {
                Sense senseOne = senseAxis.getSenseOne();
                Sense senseTwo = senseAxis.getSenseTwo();
                String sourceLexicon = senseOne.getLexicalEntry().getLexicon().getName();
                String targetLexicon = senseTwo.getLexicalEntry().getLexicon().getName();

                EPartOfSpeech senseOnePos = senseOne.getLexicalEntry().getPartOfSpeech();
                EPartOfSpeech senseTwoPos = senseTwo.getLexicalEntry().getPartOfSpeech();
                if (this.targetLexiconName != null) {
                    if (!this.targetLexiconName.equals(sourceLexicon)
                            && !this.targetLexiconName.equals(targetLexicon))

                    {
                        continue;

                    }
                }

                String sourceExternalRefID = senseOne.getMonolingualExternalRefs().get(0)
                        .getExternalReference();
                String targetExternalRefID = senseTwo.getMonolingualExternalRefs().get(0)
                        .getExternalReference();

                String sourceLemma = senseOne.getLexicalEntry().getLemmaForm();
                String targetLemma = senseTwo.getLexicalEntry().getLemmaForm();

                Entity leftHandSide = new Entity(senseOne.getId(), sourceExternalRefID,
                        EntityTypes.SENSE, sourceLemma, senseOnePos, sourceLexiconName);

                Entity rightHandSide = new Entity(senseTwo.getId(), targetExternalRefID,
                        EntityTypes.SENSE, targetLemma, senseTwoPos, targetLexiconName);

                Relation relation = new Relation(senseAxis.getSenseAxisType().name(), "SenseAxis");

                temp = new Triplet(leftHandSide, rightHandSide, relation);

                // System.out.println(temp);
                log.info(temp);
                saxList.add(temp);

            }
        }

        return saxList;
    }

    /**
     * @param args
     * @throws UbyInvalidArgumentException
     */
    public static void main(String[] args)
        throws UbyInvalidArgumentException
    {
        // TODO Auto-generated method stub
        DBConfig db = new DBConfig("localhost/uby_wn_gn", "com.mysql.jdbc.Driver", "mysql", "root",
                "ehsanukp", false);
        SenseAxisRelationExtraction relExtractor = new SenseAxisRelationExtraction(db, "WordNet",
                "GermaNet");
        Dumper dumper = new LocalFileDumper(".", "WN_GN_SenseAxis");
        relExtractor.dumpRelations(dumper);
        dumper.close();
    }

}
