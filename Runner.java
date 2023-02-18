import java.util.*;
import org.jfugue.player.*;
import org.jfugue.pattern.*;

public class Runner{
	public static void main(String[] args){
		Scanner scnr = new Scanner(System.in);
		List<Integer> notes = new ArrayList<Integer>();

		do {
			System.out.print("Enter Sequence of MIDI note values (0-127): ");
			Scanner noteSeqScanner = new Scanner(scnr.nextLine());

			while (noteSeqScanner.hasNextInt()){
				notes.add(noteSeqScanner.nextInt());
			}

		} while (notes.size() < 3);

		//notes.add(60);
		//notes.add(64);
		//notes.add(62);
		//notes.add(64);
		//notes.add(65);
		//notes.add(67);
		//notes.add(64);
		//notes.add(62);
		//notes.add(60);

		System.out.print("Enter Tempo (BPM): ");
		String tempo = scnr.nextLine();

		boolean cpAboveCF;

		System.out.print("Location of generated part (\"a\" for above, \"b\" for below): ");
		if (scnr.nextLine().equals("a")) {
			cpAboveCF = true;
		}
		else {
			cpAboveCF = false;
		}

		System.out.print("\n");

		CantusFirmus CF = new CantusFirmus(notes, 1);
		Counterpoint CP = new Counterpoint(CF, 1, cpAboveCF);
		CP.generateFirstSpec();

		//System.out.println("Lowest note in Cantus Firmus:  "  + CF.getLowestNote());
		//System.out.println("Highest note in Cantus Firmus: " + CF.getHighestNote() + "\n");

		//System.out.println(CF.getTonic() + " " + CF.getMode());

		if (cpAboveCF) {
			System.out.print("Counterpoint:  ");
			System.out.println(CP);

			System.out.print("Cantus Firmus: ");
			System.out.println(CF);
		}
		else {
			System.out.print("Cantus Firmus: ");
			System.out.println(CF);

			System.out.print("Counterpoint:  ");
			System.out.println(CP);
		}

		Player player = new Player();

		String cfString = "T" + tempo + " ";
		String cpString = "T" + tempo + " "; // Tempo = 170 BPM

		// Add each CF note to a jfugue music string as a whole note
		for (int note : CF.getNotes()) {
			cfString += note + "/1.0 ";
		}

		// Add each CP note to a jfugue music string
		for (int note : CP.getNotes()) {
			if (CP.getSpecies() == 1){
				cpString += note + "/1.0 ";
			}
		}

 		Pattern cfPart = new Pattern(cfString).setVoice(0).setInstrument("Piano");
 		Pattern cpPart = new Pattern(cpString).setVoice(1).setInstrument("Piano");

 		player.play(cfPart, cpPart);
	}
}