package com.sorenson.test;

import java.util.Arrays;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeSuite;

import com.sorenson.code.*;

public class DrawingTest {
  Drawing drawing;
  
  @BeforeSuite(alwaysRun = true)
  public void beforeSuite() {
	  drawing = new Drawing();
  }
  
  @Test(groups = "valid")
  public void checkChangingResults() {
	  //Initialize variables
	  String[] persons = fillArray(10);
	  boolean equals = true;

	  //There is a small chance that the same results will be given
	  //If we got matching results with 5 different pairs, fail the test
	  for (int i = 0 ; i <= 5 ; i++) {
		  equals = Arrays.equals(drawing.generateAssignments(persons), drawing.generateAssignments(persons));
		  if (!equals) {
			  break;
		  }
	  }
	  Assert.assertFalse(equals);
  }
  
  @Test(groups = "valid")
  public void checkAllPossibilities() {
	  //Initialize variables
	  String[] persons = {"1", "2", "3", "4"};
	  String[][] allPossibilities = {
			  {"2","3","4","1"},
			  {"3","4","1","2"},
			  {"4","1","2","3"}
	  };
	  boolean[] createdPossibility = {false,false,false};
	  boolean foundAll = false;
	  
	  //If it can't get all 3 possibilites after 100 attempts, fail
	  for (int i = 0 ; i <= 100 ; i++) {
		  //Create result
		  String[] result = drawing.generateAssignments(persons);
		  
		  //See if the result matches any remaining possibilities we haven't seen
		  for (int j = 0 ; j < allPossibilities.length ; j++) {
			  
			  //We're only going to check against possibilities we haven't see yet
			  if(!createdPossibility[j]) {
				  
				  //If we found that combination, note it
				  if (Arrays.equals(result, allPossibilities[j])) {
					  createdPossibility[j] = true;
				  }
			  }
		  }
		  
		  //Check to see if we've found all possibilities
		  if(createdPossibility[0] && createdPossibility[1] && createdPossibility[2]) {
			  foundAll = true;
			  break;
		  }
	  }
	  Assert.assertTrue(foundAll, "After 100 attempts, all possibilities were not found.");
  }
  
  @Test(groups = "valid")
  public void checkTwoPersonArray() {
	  String[] persons = fillArray(2);
	  
	  //Should equal since there's only one possibility
	  Assert.assertTrue(Arrays.equals(drawing.generateAssignments(persons), drawing.generateAssignments(persons)));
  }

  @Test(groups = "invalid")
  public void invalidEmptyArray() {
	  String[] persons = {};
	  Assert.assertNull(drawing.generateAssignments(persons));
  }
  @Test(groups = "invalid")
  public void invalidOnePersonArray() {
	  String[] persons = fillArray(1);
	  Assert.assertNull(drawing.generateAssignments(persons));
  }
  
  @Test(groups = "invalid")
  public void invalidRepeats() {
	  //Fill the array
	  String[] persons = fillArray(10);
	  
	  //Get two random numbers
	  Random rnd = new Random();
	  int i = rnd.nextInt(10);
	  int j = rnd.nextInt(10);
	  while (i == j) {
		  i = rnd.nextInt(10);
		  j = rnd.nextInt(10);
	  }
	  
	  //Overwrite i's element with j's to cause a repeat
	  persons[i] = persons[j];
	  Assert.assertNull(drawing.generateAssignments(persons));
  }
  
  
  @Test(groups = "performance")
  public void check10000ResultsSpeed() {
	  String[] persons = fillArray(10000);
	  
	  //Get elapsed system time while generating assignments
	  long startTime = System.currentTimeMillis();
	  String[] assignments = drawing.generateAssignments(persons);
	  long endTime = System.currentTimeMillis();
	  long elapsedTime = endTime-startTime;
	  
	  //Check that we actually received something
	  Assert.assertNotNull(assignments);
	  
	  //Check that it took less than 50 milliseconds
	  Assert.assertTrue(elapsedTime < 50, 
			  "Epected the elapsed time of generating assignments to " +
			  "take less that 50 milliseconds");
  }
  
  @Test(groups = "performance")
  public void check100000ResultsSpeed() {
	  String[] persons = fillArray(100000);
	  
	  //Get elapsed system time while generating assignments
	  long startTime = System.currentTimeMillis();
	  String[] assignments = drawing.generateAssignments(persons);
	  long endTime = System.currentTimeMillis();
	  long elapsedTime = endTime-startTime;
	  
	  //Check that we actually received something
	  Assert.assertNotNull(assignments);
	  
	  //Check that it took less than 500 milliseconds
	  Assert.assertTrue(elapsedTime < 500, 
			  "Expected the elapsed time of generating assignments to" +
			  "take less than 500 milliseconds");
  }
  
  @Test(groups = "performance")
  public void check1000000ResultsSpeed() {
	  String[] persons = fillArray(1000000);
	  
	  //Get elapsed system time while generating assignments
	  long startTime = System.currentTimeMillis();
	  String[] assignments = drawing.generateAssignments(persons);
	  long endTime = System.currentTimeMillis();
	  long elapsedTime = endTime-startTime;
	  
	  //Check that we actually received something
	  Assert.assertNotNull(assignments);
	  
	  //Check that it took less than 1000 milliseconds
	  Assert.assertTrue(elapsedTime < 1000, 
			  "Expected the elapsed time of generating assignments to " +
			  "take less than 1000 milliseconds");
  }
  
  public String[] fillArray(int size) {
	  String[] persons = new String[size];
	  boolean hasRepeats = true;
	  while (hasRepeats) {
		  for (int i = 0 ; i < size ; i++) {
			  persons[i] = RandomStringUtils.randomAlphabetic(10);
		  }
		  hasRepeats = drawing.hasRepeats(persons);
	  }
	  return persons;
  }
}
