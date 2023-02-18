import java.util.*;
import java.lang.*;

public class CantusFirmus extends NoteSequence{
	private List<Integer> notes = new ArrayList<Integer>();
    private int tonic;
    private String mode;
    private int lowestNote  = 127; // G8,  highest midi note
    private int highestNote = 0;   // C-2, lowest midi note

	public CantusFirmus(List<Integer> theNotes, double theNoteLength) {
		super(theNotes, theNoteLength);
	}
}