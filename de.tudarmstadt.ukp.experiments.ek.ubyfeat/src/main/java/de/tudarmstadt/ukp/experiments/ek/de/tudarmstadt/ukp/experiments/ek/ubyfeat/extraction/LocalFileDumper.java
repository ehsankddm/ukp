package de.tudarmstadt.ukp.experiments.ek.de.tudarmstadt.ukp.experiments.ek.ubyfeat.extraction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class LocalFileDumper
    extends Dumper
{
    private final File dumpeeFile;
    private final File externalRefFile;
    private PrintWriter writer;
    private PrintWriter writerExternalRef;

    public LocalFileDumper(String path, String fileName)
    {
        super(fileName);
        this.dumpeeFile = new File(new File(path), this.resourceName);
        this.externalRefFile = new File(new File(path), this.resourceName + "_externalRef");
        try {
            this.writer = new PrintWriter(this.dumpeeFile);
            this.writerExternalRef = new PrintWriter(this.externalRefFile);
        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // TODO Auto-generated constructor stub
    }

    @Override
    public void dumpTriplets(Triplet triplet)
    {
        // TODO Auto-generated method stub
        this.writer.println(triplet);
        this.writer.flush();

        this.writerExternalRef.println(triplet.getLeftHandSide().getUbyID() + "\t"
                + triplet.getLeftHandSide().getExternalRefID());
        this.writerExternalRef.println(triplet.getRightHandSide().getUbyID() + "\t"
                + triplet.getRightHandSide().getExternalRefID());
        this.writerExternalRef.flush();
    }

    @Override
    public void dumpTriplets(List<Triplet> triplets)
    {
        // TODO Auto-generated method stub
        for (Triplet triplet : triplets) {
            this.writer.println(triplet);
            this.writer.flush();

            this.writerExternalRef.println(triplet.getLeftHandSide().getUbyID() + "\t"
                    + triplet.getLeftHandSide().getExternalRefID());
            this.writerExternalRef.println(triplet.getRightHandSide().getUbyID() + "\t"
                    + triplet.getRightHandSide().getExternalRefID());
            this.writerExternalRef.flush();
        }

    }

    @Override
    public void close()
    {
        // TODO Auto-generated method stub
        this.writer.close();
        this.writerExternalRef.close();

    }

    public static void main(String[] args)
    {

        String path = System.getProperty("java.io.tmpdir");
        String resourceName = "test.test.test";
        Dumper dumper = new LocalFileDumper(path, resourceName);
        // dumper.addTriplets(new Triple());

    }

}
