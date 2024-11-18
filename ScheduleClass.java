import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class ScheduleClass {
    private static Set<Integer> scheduledClassIds = new HashSet<>();
    public static Class[][] scheduleClasses(ScheduleInput input) {
        int numRooms = input.getRooms().length - 1;
        int numTimeSlots = input.getNumTimeslots();
        Class[][] schedule = new Class[numRooms][numTimeSlots];
        Class[] classes = input.getClasses();
        int current = 1; 

        boolean scheduled = true; // Track if the class is scheduled
        Class unscheduledClass = null;

        int[] emptyRooms = new int[classes.length];
        int[] emptyTime = new int[classes.length];
        int indexEmpty = 0; 


        // Iterate over each room
        // for (int i = 0; i < numRooms; i++) {
            
        //     // Iterate over each timeslot in the room
        //     for (int j = 0; j < numTimeSlots && current < classes.length; j++) {
        //         boolean conflict = false;
        //         Class mostPopularClass;

        //         //If the previous class is not scheduled yet, we don't move onto the next popular class
        //         if (unscheduledClass != null) {
        //             mostPopularClass = unscheduledClass;
        //         } else {
        //             mostPopularClass = classes[current++];
        //         }
        //         scheduled = false;                

        //         // Check teacher constraint with classes in the same timeslot across other rooms
        //         for (int k = 0; k < numRooms; k++) {
        //             if (schedule[k][j] != null &&
        //                 checkTeacherConstraint(schedule[k][j], mostPopularClass)) {
        //                 conflict = true;
        //                 break;
        //             }
        //         }

        //         //If it's not conflicted, we schedule the room
        //         if (!conflict && schedule[i][j] == null) {
        //             mostPopularClass.setRoomId(i+1);  // Update roomId in class object
        //             mostPopularClass.setTime(j);     // Update timeslot in class object
        //             schedule[i][j] = mostPopularClass;
        //             scheduled = true;
        //             scheduledClassIds.add(mostPopularClass.getClassId()); // Track scheduled classes
        //             unscheduledClass = null; // Reset after attempting to schedule

        //         //If conflict, we add the empty time and room to the arrays
        //         } else if (conflict) {
        //             emptyRooms[indexEmpty] = i;
        //             emptyTime[indexEmpty] = j;
        //             indexEmpty++;
        //         }

        //         //If the class is not scheduled, track if so that we move onto the next room
        //         if (scheduled == false) {
        //             unscheduledClass = mostPopularClass;
        //         }
        //     }
        // }

        //Storing all unassigned classes into a stack
        Queue<Class> unassignedClass = new ArrayDeque<>();

        //add every class to the queue to keep track of assigned or unassigned class
        for (int i = 1; i < classes.length; i++) {
            unassignedClass.add(classes[i]);
        }

        // for (int i = current; i < classes.length; i++) {
        //     unassignedClass.add(classes[i]);
        //     // System.out.println("iD: " + classes[i].getClassId());
        // }

        //Loop until all the class is scheduled 
        int countClassAssigned = 0;
        System.out.println("Num Rooms " + numRooms);
        while (!unassignedClass.isEmpty()) {
            Class classBeingAssigned = unassignedClass.remove();
            scheduled = false;

            //for every class, we loop through each time slot of each room
            outerloop:
            for (int i = 0; i < numRooms; i++) {
                for (int j = 0; j < numTimeSlots; j++) {
                    boolean conflict = false;
                
                
                    // Check teacher constraint with classes in the same timeslot across other rooms
                    for (int k = 0; k < numRooms; k++) {
                        if (schedule[k][j] != null &&
                            checkTeacherConstraint(schedule[k][j], classBeingAssigned)) {
                            conflict = true;
                            break;
                        }
                    }

                    //If not conflicted, we assign the class, and move onto the next class 
                    if (!conflict && schedule[i][j] == null) {
                        classBeingAssigned.setRoomId(i + 1);  // Update roomId in class object
                        // if (classBeingAssigned.getClassId() == 205) {
                        //     System.out.println("Class 205 got " + classBeingAssigned.getRoomId());
                        // }
                        classBeingAssigned.setTime(j);     // Update timeslot in class object
                        // System.out.println("Class " + classBeingAssigned.getClassId() + " being assigned to " + (i + 1) + " at time " + j);
                        schedule[i][j] = classBeingAssigned;
                        scheduled = true;
                        scheduledClassIds.add(classBeingAssigned.getClassId()); // Track scheduled classes
                        countClassAssigned += 1;
                        break outerloop;
                    }
                }
            }

            // if (scheduled == false) {
            //     System.out.println("Class " + classBeingAssigned.getClassId() + " cannot be scheduled");
            // }
        }
        // printSchedule(schedule);
        return schedule;
    }

    // Checks if there’s a conflict between two classes based on teacher
    private static boolean checkTeacherConstraint(Class class1, Class class2) {
        return class1.isSameTeacher(class2);
    }
    public static boolean isClassScheduled(int classId) {
        return scheduledClassIds.contains(classId);
    }
    // Print the schedule for debugging or verification
    private static void printSchedule(Class[][] schedule) {
        for (int i = 0; i < schedule.length; i++) {
            System.out.println("Room " + (i+1) + ": ");
            for (int j = 0; j < schedule[i].length; j++) {
                System.out.print("  Timeslot " + j + ": ");
                if (schedule[i][j] != null) {
                    System.out.println(schedule[i][j]);
                } else {
                    System.out.println("Empty");
                }
            }
        }
    }


    /*public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java ScheduleClass <constraint file> <pref file>");
            System.exit(1);
        }

        try {
            // Create a new instance of ScheduleInput to hold all scheduling data
            ScheduleInput input = new ScheduleInput();

            // Parse input files using ProcessInput
            ProcessInput.parseConstraintFile(input, args[0]);
            ProcessInput.parseStudentPrefFile(input, args[1]);

            // Schedule classes with the parsed data
            Class[][] scheduledClasses = scheduleClasses(input);

            // Assign students to classes based on their preferences and the created schedule
            assignStudentsToClasses(input);

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
*/
}
