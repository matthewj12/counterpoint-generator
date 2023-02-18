import java.util.*;
import java.lang.*;

public class NoteSequence{
    private List<Integer> notes = new ArrayList<Integer>();
    private double noteLength;
    private int tonic;
    private String mode;
    private int lowestNote  = 127; // G8,  highest midi note
    private int highestNote = 0;   // C-2, lowest midi note

    public int UNISON      = 0;
    public int MIN_SECOND  = 1;
    public int MAJ_SECOND  = 2;
    public int MIN_THIRD   = 3;
    public int MAJ_THIRD   = 4;
    public int PER_FOURTH  = 5;
    public int TRITONE     = 6;
    public int PER_FIFTH   = 7;
    public int MIN_SIXTH   = 8;
    public int MAJ_SIXTH   = 9;
    public int MIN_SEVENTH = 10;
    public int MAJ_SEVENTH = 11;
    public int OCTAVE      = 12;

    public HashMap<String, int[]> MODE_INTVLS = new HashMap<String, int[]>();

    public NoteSequence(List<Integer> theNotes, double theNoteLength){
        notes = theNotes;
        noteLength = theNoteLength;

        MODE_INTVLS.put( "major",          new int[] {0, 2, 4, 5, 7, 9, 11} );
        MODE_INTVLS.put( "natural minor",  new int[] {0, 2, 3, 5, 7, 8, 10} );
        MODE_INTVLS.put( "harmonic minor", new int[] {0, 2, 3, 5, 7, 8, 11} );
        MODE_INTVLS.put( "dorian",         new int[] {0, 2, 3, 5, 7, 9, 10} );
        MODE_INTVLS.put( "phygarian",      new int[] {0, 1, 3, 5, 7, 8, 10} );
        MODE_INTVLS.put( "lydian",         new int[] {0, 2, 4, 6, 7, 9, 11} );
        MODE_INTVLS.put( "mixolodian",     new int[] {0, 2, 4, 5, 7, 9, 10} );
        MODE_INTVLS.put( "atonal",         new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11} );

        if (notes.size() > 0){
            tonic = notes.get(0);
            setMode();
            setRange();
        }

    }

    public void setRange(){
        for (int note : notes){
            if (note < lowestNote){
                lowestNote = note;
            }
            if (note > highestNote){
                highestNote = note;
            }
        }
    }

    public List<Integer> getNotes(){
        return notes;
    }

    public int getTonic(){
        return tonic;
    }

    public int getHighestNote(){
        return highestNote;
    }

    public int getLowestNote(){
        return lowestNote;
    }

    public String getMode(){
        return mode;
    }

    public void setMode(){
        //List<Integer> intvls = new ArrayList<Integer>();

        //for (int note : notes){
        //    while (note < tonic){
        //        note += 12;
        //    }
        //    while (note > tonic){
        //        note -= 12;
        //    }

        //    intvls.add(note - tonic);
        //}

        //Collections.sort(intvls);

        //int i = 0;
        //while (i < intvls.size()-1){
        //    if (intvls.get(i) == intvls.get(i+1)){
        //        intvls.remove(i);
        //    }
        //    i++;
        //}

        mode = "major";
    }

    public String toString(){
        String toReturn = "";

        if (notes.size() > 0){
            for (int i = 0; i < notes.size()-1; i++){
                if (notes.get(i) < 10){
                    toReturn += notes.get(i) + "  -  ";
                }
                else if (notes.get(i) < 100){
                    toReturn += notes.get(i) + " -  ";
                }
                else{
                    toReturn += notes.get(i) + "-  ";
                }
            }
            toReturn += notes.get(notes.size()-1);
            toReturn += "\n";
        }

        return toReturn;
    }
}