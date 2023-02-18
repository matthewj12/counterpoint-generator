import java.util.*;

public class Counterpoint extends NoteSequence{
	private List<Integer> notes = new ArrayList<Integer>();
	private List<Integer> scale = new ArrayList<Integer>();
    private int lowestNote  = 127; // highest possible midi note (G8)
    private int highestNote = 0;   // lowest possible midi note (C-2)

	private CantusFirmus CF;
	private List<Integer> cfNotes;
	private boolean isAboveCF;
	private boolean restOnFirstBeat;
	private int species;

	public Counterpoint(CantusFirmus theCF, double theNoteLength, boolean isAboveCF){
		super(new ArrayList<Integer>(), theNoteLength);
		super.setMode();

		CF = theCF;
		cfNotes = CF.getNotes();

		// Fill notes with a placeholder value
		for (int note : cfNotes){
			notes.add(-1);
		}

		isAboveCF = isAboveCF;

		for (int i = 0; i < 7; i++){
			scale.add( CF.getTonic() + MODE_INTVLS.get(CF.getMode())[i] );
		}
	}

	public List<Integer> getNotes(){
        return notes;
    }

    public int getSpecies(){
        return species;
    }

	public void generateFirstSpec(){
		species = 1;

		int sixth;
		int third;
		int fifth;
		int octave;
		int distToSixth;
		int distToThird;

		setFinalCadence(1);

		// starts writing at the end (before cadence) and moves backwards
		for (int i = (cfNotes.size() - 3); i > 0; i--){
			int cpBlwMltplr = 1;

			if (!isAboveCF){
				cpBlwMltplr = -1;
			}

			// Above/below the current CF note
			sixth  = cfNotes.get(i) + (MIN_SIXTH * cpBlwMltplr);
			third  = cfNotes.get(i) + (MIN_THIRD * cpBlwMltplr);
			fifth  = cfNotes.get(i) + (PER_FIFTH * cpBlwMltplr);
			octave = cfNotes.get(i) + (OCTAVE    * cpBlwMltplr);
			distToSixth = Math.abs(notes.get(i) - sixth);
			distToThird = Math.abs(notes.get(i) - third);

			// If the minor third/sixth is out of the key
			// 		change it to major so it's in the key
			if (!isInKey(sixth)){
				sixth = cfNotes.get(i) + (MAJ_SIXTH * cpBlwMltplr);
			}
			if (!isInKey(third)){
				third = cfNotes.get(i) + (MAJ_THIRD * cpBlwMltplr);
			}

			// If CF ascends
			if (cfNotes.get(i) < cfNotes.get(i+1)){
				// If sixth makes contrary motion
				if (notes.get(i+1) < sixth){
					notes.set(i, sixth);
				}
				// If third makes contrary motion
				else if (notes.get(i+1) < third){
					notes.set(i, third);
				}
				// If sixth makes direct motion
				else if (notes.get(i+1) > sixth){
					notes.set(i, sixth);
				}
				// If third makes direct motion
				else if (notes.get(i+1) > third){
					notes.set(i, third);
				}
				// If fifth is in key and makes contrary motion
				else if (isInKey(fifth) && fifth > notes.get(i+1)){
					notes.set(i, fifth);
				}
				// If octave makes contrary motion
				else if (octave > notes.get(i+1)){
					notes.set(i, octave);
				}
				
				// If all else fails, set to third/sixth (whichever is closer)
				else{
					if (distToThird <= distToSixth){
						notes.set(i, third);
					}
					else if (distToThird > distToSixth){
						notes.set(i, sixth);
					}
				}
			}

			// If CF descends
			else{
				// If sixth makes contrary motion
				if (notes.get(i+1) > sixth){
					notes.set(i, sixth);
				}
				// If third makes contrary motion
				else if (notes.get(i+1) > third){
					notes.set(i, third);
				}
				// If sixth makes direct motion
				else if (notes.get(i+1) < sixth){
					notes.set(i, sixth);
				}
				// If third makes direct motion
				else if (notes.get(i+1) < third){
					notes.set(i, third);
				}
				// If fifth is in key and makes contrary motion
				else if (isInKey(fifth) && fifth < notes.get(i+1)){
					notes.set(i, fifth);
				}
				// If octave makes contrary motion
				else if (octave < notes.get(i+1)){
					notes.set(i, octave);
				}
				
				// If all else fails, set to third/sixth (whichever is closer)
				else{
					if (distToThird <= distToSixth){
						notes.set(i, third);
					}
					else if (distToThird > distToSixth){
						notes.set(i, sixth);
					}
				}
			}
		}

		setFirstNote();
	}

	public void generateSecondSpec(){
		generateFirstSpec();

		species = 2;

		int second;
		int third;
		int fourth;
		int fifth;
		int sixth;
		int seventh;
		int eighth;
		int ninth;
		int tenth;

		int downBeatsDist;

		List<Integer> newNotes = new ArrayList<Integer>();

		// Adds a placeholder note in between each note (last note stays the same)
		for (int i = 0; i < notes.size(); i++){
			newNotes.add(notes.get(i));
			newNotes.add(-1);
		}

		newNotes.add(notes.get(notes.size()-1));

		notes = new ArrayList<Integer>();
		for (int note : newNotes){
			notes.add(note);
		}

		// Changes the next-to-last measure
		setFinalCadence(2);

		for (int i = 0; i < newNotes.size()-1; i += 2){
			downBeatsDist = newNotes.get(i+2) - newNotes.get(i);
		}


	}

	public boolean isInKey(int note){
		// Acount for different octaves
		if (note >= CF.getTonic() + OCTAVE){
			note -= OCTAVE;
		}
		else if (note < CF.getTonic()){
			note += OCTAVE;
		}

		return scale.contains(note);
	}

	// Sets first note of CP to an octave or fifth above CF (whichever's closer) if CP is above CF
	// Sets first note of CP to an octave          below CF                       if CP is below CF
	public void setFirstNote(){
		if (isAboveCF){
			int octave = cfNotes.get(0) + OCTAVE;
			int fifth  = cfNotes.get(0) + PER_FIFTH;

			// If the octave is closer than the fifth
			if (notes.get(1) - octave < notes.get(1) - fifth){
				notes.set(0, octave);
			}
			else{
				notes.set(0, fifth);
			}
		}
		else{
			notes.set(0, cfNotes.get(0) - OCTAVE);
		}
	}

	public void setFinalCadence(int species){
		int scndToLastNote = cfNotes.get(cfNotes.size()-2);
		int lastNote = cfNotes.get(cfNotes.size()-1);

		if (species == 1){
			if (isAboveCF){
				notes.set(notes.size()-2, scndToLastNote + MAJ_SIXTH);
				notes.set(notes.size()-1, lastNote + OCTAVE);
			}
			else{
				notes.set(notes.size()-2, scndToLastNote - MIN_THIRD);
				notes.set(notes.size()-1, lastNote + UNISON);
			}
		}

		else if (species == 2){
			if (isAboveCF){
				notes.set(notes.size() - 3, cfNotes.get(cfNotes.size()-2) + PER_FIFTH);
				notes.set(notes.size() - 2, cfNotes.get(cfNotes.size()-2) + MAJ_SIXTH);
			}
			else if (!isAboveCF){
				notes.set(notes.size() - 3, cfNotes.get(cfNotes.size()-2) - PER_FIFTH);
				notes.set(notes.size() - 2, cfNotes.get(cfNotes.size()-2) - MIN_THIRD);
			}
		}
	}

	public String toString(){
        String toReturn = "";

        if (species == 1){
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
	    }

        return toReturn;
    }
}