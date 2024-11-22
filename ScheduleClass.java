import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class ScheduleClass {
    private static Set<Integer> scheduledClassIds = new HashSet<>();
    public static Class[][] scheduleClasses(ScheduleInput input) {
        int numRooms = input.getRooms().length - 1;
        int numTimeSlots = input.getNumTimeslots();
        Class[][] schedule = new Class[numRooms][numTimeSlots];
        Class[] classes = input.getClasses();

        //Storing all unassigned classes into a stack
        Queue<Class> unassignedClass = new ArrayDeque<>();

        //Add every class to the queue to keep track of assigned or unassigned class
        for (int i = 1; i < classes.length; i++) {
            unassignedClass.add(classes[i]);
        }

        //Loop until all the class is scheduled 
        while (!unassignedClass.isEmpty()) {
            Class classBeingAssigned = unassignedClass.remove();

            //for every class, we loop through each time slot of each room
            outerloop:
            for (int i = 0; i < numRooms; i++) {
                for (int j = 0; j < numTimeSlots; j++) {
                    boolean conflict = false;
                
                    //if this slot is already scheduled, move to the next timeslot
                    if (schedule[i][j] != null) {
                        continue;
                    }

                    // Check teacher constraint with classes in the same timeslot across other rooms
                    for (int k = 0; k < numRooms; k++) {
                        if (schedule[k][j] != null &&
                            checkTeacherConstraint(schedule[k][j], classBeingAssigned)) {
                            conflict = true;
                            break;
                        }
                    }

                    //If not conflicted, we assign the class, and move onto the next class 
                    if (!conflict) {
                        classBeingAssigned.setRoomId(i + 1);  // Update roomId in class object
                        classBeingAssigned.setTime(j); // Update timeslot in class object
                        schedule[i][j] = classBeingAssigned;
                        scheduledClassIds.add(classBeingAssigned.getClassId()); // Track scheduled classes
                        break outerloop;
                    }
                }
            }
        }
        return schedule;
    }

    // Checks if there’s a conflict between two classes based on teacher
    private static boolean checkTeacherConstraint(Class class1, Class class2) {
        return class1.isSameTeacher(class2);
    }

    // Checks if the class is already scheduled
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
}
