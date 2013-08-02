package de.tudarmstadt.ukp.experiments.ek.de.tudarmstadt.ukp.experiments.ek.ubyfeat.extraction;

import java.util.Iterator;

import de.tudarmstadt.ukp.lmf.api.Uby;
import de.tudarmstadt.ukp.lmf.exceptions.UbyInvalidArgumentException;
import de.tudarmstadt.ukp.lmf.model.core.LexicalEntry;
import de.tudarmstadt.ukp.lmf.model.core.Lexicon;
import de.tudarmstadt.ukp.lmf.model.core.Sense;
import de.tudarmstadt.ukp.lmf.model.semantics.Synset;
import de.tudarmstadt.ukp.lmf.transform.DBConfig;

public class DocumentGenerator
{

    public DocumentGenerator()
    {
        // TODO Auto-generated constructor stub

    }

    private void generateSynsetDoc (Lexicon lex){
        Iterator<Synset> synsetIterator = lex.getSynsets().iterator();
        Synset  synset = null;
        while (synsetIterator.hasNext()){
            synset = synsetIterator.next();

            System.out.println(synset.getMonolingualExternalRefs().get(0).getExternalReference());
            System.out.println("gloss: "+synset.getGloss());
            System.out.println("def text: "+synset.getDefinitionText());
            System.out.println("size of def: "+synset.getDefinitions().size());
            for (Sense sense:synset.getSenses()){
                System.out.println("Sense:"+sense.getLexicalEntry().getLemmaForm());
                System.out.println("        examples: "+sense.getSenseExamples());

            }
            System.out.println("*******************************************--------********");
        }


    }

    private void generateLexicalEntryDoc(Iterator<LexicalEntry> iterator){
        LexicalEntry lexEntry= null;
        while (iterator.hasNext()){
            lexEntry = iterator.next();
            System.out.println(lexEntry.getLemmaForm());


        }
    }
    private void generateSenseDoc (Iterator<Sense> iterator){
        Sense sense = null;
        while (iterator.hasNext()){
            sense = iterator.next();
            System.out.println("Sense:"+sense.getLexicalEntry().getLemmaForm());
            System.out.println(sense.getMonolingualExternalRefs().get(0).getExternalReference());
            System.out.println("examples: "+sense.getSenseExamples());
            System.out.println("def text: "+sense.getDefinitionText());
            System.out.println("size of def: "+sense.getDefinitions().size());

            System.out.println("*******************************************--------********");
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        // TODO Auto-generated method stub
        DBConfig db = new DBConfig("localhost/uby_wn_gn", "com.mysql.jdbc.Driver", "mysql",
                "root", "ehsanukp", false);
        Uby uby;
        try {
            uby = new Uby(db);
            String lexiconName = "GermaNet";

            Lexicon lexicon = uby.getLexiconByName(lexiconName);
            DocumentGenerator docGen = new DocumentGenerator();
            docGen.generateSynsetDoc(lexicon);
//            docGen.generateLexicalEntryDoc(uby.getLexicalEntryIterator(lexicon));
//            docGen.generateSenseDoc(uby.getSenseIterator(lexicon));

        }
        catch (UbyInvalidArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

}
