package de.tudarmstadt.ukp.experiments.ek.de.tudarmstadt.ukp.experiments.ek.ubyfeat.extraction;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tudarmstadt.ukp.lmf.exceptions.UbyInvalidArgumentException;
import de.tudarmstadt.ukp.lmf.model.core.LexicalEntry;
import de.tudarmstadt.ukp.lmf.model.core.Lexicon;
import de.tudarmstadt.ukp.lmf.model.core.Sense;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;
import de.tudarmstadt.ukp.lmf.model.semantics.Synset;
import de.tudarmstadt.ukp.lmf.model.semantics.SynsetRelation;
import de.tudarmstadt.ukp.lmf.transform.DBConfig;

public class WordNetSynsetRelationExtraction
    extends UbyRelationExtraction
{
    private final Log log = LogFactory.getLog(WordNetSynsetRelationExtraction.class);

    public WordNetSynsetRelationExtraction(DBConfig db) throws UbyInvalidArgumentException

    {
        super(db);
        super.statisticsExtractor = new StatisticsExtractor("WN_Sense_Feat.stat");

    }

    @Override
    public void dumpRelations(Dumper dumper)
    {
        String lexiconName = "WordNet";
        Lexicon lex;
        List<Triplet> triplets;
        try {
            lex = uby.getLexiconByName(lexiconName);
            for (EPartOfSpeech pos : POSList) {
                Iterator<LexicalEntry> lexicalEntryIterator = uby.getLexicalEntryIterator(pos, lex);
                LexicalEntry lexicalEntry;
                while (lexicalEntryIterator.hasNext()) {
                    lexicalEntry = lexicalEntryIterator.next();

                    for (Synset synset : lexicalEntry.getSynsets()) {
                        triplets = getWordNetSenseRelations(synset, lexiconName);
                        dumper.dumpTriplets(triplets);

                    }
                }
            }

        }
        catch (UbyInvalidArgumentException e) {

            e.printStackTrace();
        }

    }

    @Override
    public void dumpRelationsStatistics(PrintWriter writer)
    {

    }

    private List<Triplet> getSynsetRelations(Synset synset, String lemma, EPartOfSpeech pos,
            String lexName)
    {
        List<Triplet> srList = new ArrayList<Triplet>();
        for (SynsetRelation synsetRelation : synset.getSynsetRelations()) {
            synsetRelation.getFrequencies();
            if (synsetRelation.getSource() == null || synsetRelation.getSource().getId() == null
                    || synsetRelation.getTarget() == null
                    || synsetRelation.getTarget().getId() == null) {
                continue;
            }

            // String targetID = synsetRelation.getTarget().getId();
            String sourceExternalRefID = synsetRelation.getSource().getMonolingualExternalRefs()
                    .get(0).getExternalReference();

            Entity leftHandSide = new Entity(synsetRelation.getSource().getId(),
                    sourceExternalRefID, EntityTypes.SYNSET, lemma, pos, lexName);

            String targetLemma = null;
            // Be careful about this POS
            if (synsetRelation.getTarget().getSenses().size() > 0) {
                targetLemma = synsetRelation.getTarget().getSenses().get(0).getLexicalEntry()
                        .getLemmaForm();

            }
            System.out.println(synsetRelation.getRelType().name() + ":"
                    + synsetRelation.getRelName());
            String targetExternalRefID = synsetRelation.getTarget().getMonolingualExternalRefs()
                    .get(0).getExternalReference();

            Entity rightHandSide = new Entity(synsetRelation.getTarget().getId(),
                    targetExternalRefID, EntityTypes.SYNSET, targetLemma, pos, lexName);

            Relation relation = new Relation(synsetRelation.getRelType().name(),
                    synsetRelation.getRelName());

            Triplet temp = new Triplet(leftHandSide, rightHandSide, relation);

            // log.info(temp);
            srList.add(temp);

        }

        return srList;

    }

    private List<Triplet> getWordNetSenseRelations(Synset synset, String lexiconName)
    {
        List<Triplet> srList = new ArrayList<Triplet>();
        for (SynsetRelation synsetRelation : synset.getSynsetRelations()) {
            synsetRelation.getFrequencies();
            if (synsetRelation.getSource() == null || synsetRelation.getSource().getId() == null
                    || synsetRelation.getTarget() == null
                    || synsetRelation.getTarget().getId() == null) {
                log.info("something is null, be careful about this sense: " + synset
                        + "\n source: " + synsetRelation.getSource() + "\n target:"
                        + synsetRelation.getTarget());
                continue;
            }

            // String targetID = synsetRelation.getTarget().getId();

            Triplet temp;
            for (Sense sourceSense : synsetRelation.getSource().getSenses()) {
                for (Sense targetSense : synsetRelation.getTarget().getSenses()) {

                    String sourceLemma = sourceSense.getLexicalEntry().getLemmaForm();
                    String targetLemma = targetSense.getLexicalEntry().getLemmaForm();
                    EPartOfSpeech sourcePos = sourceSense.getLexicalEntry().getPartOfSpeech();
                    EPartOfSpeech targetPos = targetSense.getLexicalEntry().getPartOfSpeech();
                    // System.out.println(synsetRelation.getRelType().name()+":"+synsetRelation.getRelName());

                    String sourceExternalRefID = sourceSense.getMonolingualExternalRefs().get(0)
                            .getExternalReference();
                    String targetExternalRefID = targetSense.getMonolingualExternalRefs().get(0)
                            .getExternalReference();

                    Entity leftHandSide = new Entity(sourceSense.getId(), sourceExternalRefID,
                            EntityTypes.SENSE, sourceLemma, sourcePos, lexiconName);

                    Entity rightHandSide = new Entity(targetSense.getId(), targetExternalRefID,
                            EntityTypes.SENSE, targetLemma, targetPos, lexiconName);

                    Relation relation = new Relation(synsetRelation.getRelType().name(),
                            synsetRelation.getRelName());

                    temp = new Triplet(leftHandSide, rightHandSide, relation);

                    log.info(temp);
                    srList.add(temp);

                }

            }

        }

        return srList;

    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args)
        throws Exception
    {
        DBConfig db = new DBConfig("localhost/uby_academic", "com.mysql.jdbc.Driver", "mysql",
                "root", "ehsanukp", false);
        WordNetSynsetRelationExtraction relExtractor = new WordNetSynsetRelationExtraction(db);

        Dumper dumper = new LocalFileDumper(".", "WN_Sense_Feat.output");

        // writer = new PrintWriter("./WN_Sense_Feat.output");Saucy Salamander
        relExtractor.dumpRelations(dumper);
        dumper.close();

    }
}
