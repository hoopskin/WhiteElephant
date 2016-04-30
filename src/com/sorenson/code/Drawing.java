package com.sorenson.code;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class Drawing {
	/**
	* @param participants list of individuals participating in the draw.
	* @return the list of individuals paired with the participants where participants[0] is matched
	* with assignments[0], participants[1] is matched with assignments[1], and so on. This will return
	* null if there is an error (like repeated individuals or not enough participants)
	*/
	public String[] generateAssignments(final String[] participants) {
		String[] assignments = new String[participants.length];
		
		//Check for length > 1
		if(participants.length <= 1) {
			System.out.println("2 or more individuals are required. Please try again with more individuals.");
			return null;
		}
		
		//Check for repeats
		if (hasRepeats(participants)) {
			System.out.println("All individuals must be uniquely named. Please try again with unique names.");
			return null;
		}
		
		//Randomize assignments (Fisher-Yates shuffle)
		assignments = randomizeAssignments(participants);
		
		//Check for self-assignment, re-randomize until there are no self-assignments
		//While this could technically be an infinite loop (if the shuffle keeps producing the same results,
		//it is so rare that it's not worth breaking out after some number of times
		boolean reRandomize = selfAssignmentExists(assignments, participants);
		while(reRandomize) {
			assignments = randomizeAssignments(participants);
			reRandomize = selfAssignmentExists(assignments, participants);
		}
		
		return assignments;
	}

	/**
	 * Determines if someone has been assigned to themselves.
	 * @param assignments The potential assignments that have been generated.
	 * @param participants The passed list of participants.
	 * @return true if there are self assignments, otherwise false.
	 */
	private boolean selfAssignmentExists(String[] assignments,
			String[] participants) {
		for (int i = 0 ; i < assignments.length ; i++) {
			if (assignments[i] == participants[i]) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Randomly assigns individuals using the Fisher-Yates shuffle.
	 * @param participants The list of participants to shuffle.
	 * @return A shuffled list of participants.
	 */
	private String[] randomizeAssignments(final String[] participants) {
		String[] candidateAssignments = participants.clone();
		Random rnd = new Random();
		for (int i = candidateAssignments.length-1 ; i>0 ; i--) {
			//i+1 because nextInt's upper bound is exclusive
			int idx = rnd.nextInt(i+1);
			
			//Swap individuals at idx and i
			String person = candidateAssignments[idx];
			candidateAssignments[idx] = candidateAssignments[i];
			candidateAssignments[i] = person;
		}
		
		return candidateAssignments;
	}

	/**
	 * Determines if there are repeated participants in the passed list.
	 * @param participants The list to check if there are repeats.
	 * @return true if there are any repeats, otherwise false.
	 */
	public boolean hasRepeats(final String[] participants) {
		//Sets remove repeats so if there are any repeats, the sizes will be different
		Set<String> mySet = new HashSet<String>(Arrays.asList(participants));
		if (mySet.size() != participants.length) {
			return true;
		}
		
		return false;
	}
}
