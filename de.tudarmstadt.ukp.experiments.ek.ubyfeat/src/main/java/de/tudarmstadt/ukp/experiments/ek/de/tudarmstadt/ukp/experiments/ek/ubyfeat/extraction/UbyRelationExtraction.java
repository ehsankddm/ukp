package de.tudarmstadt.ukp.experiments.ek.de.tudarmstadt.ukp.experiments.ek.ubyfeat.extraction;

import java.io.PrintWriter;

import de.tudarmstadt.ukp.lmf.api.Uby;
import de.tudarmstadt.ukp.lmf.exceptions.UbyInvalidArgumentException;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;
import de.tudarmstadt.ukp.lmf.transform.DBConfig;

public abstract class UbyRelationExtraction
{
    protected Uby uby;
    protected EPartOfSpeech[] POSList = { EPartOfSpeech.verb, EPartOfSpeech.noun,
            EPartOfSpeech.adjective, EPartOfSpeech.adverb };
    protected StatisticsExtractor statisticsExtractor;
    protected UbyRelationExtraction( DBConfig db) throws UbyInvalidArgumentException{
        uby = new Uby(db);
    }
    public abstract void dumpRelations(Dumper dumper);
    public abstract void dumpRelationsStatistics(PrintWriter writer);
}
