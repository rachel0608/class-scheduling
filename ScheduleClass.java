import java.io.FileNotFoundException;

public class ScheduleClass {

    public static Class[][] scheduleClasses(ScheduleInput input) {
        int numRooms = input.getRooms().length - 1;
        int numTimeSlots = input.getNumTimeslots();
        Class[][] schedule = new Class[numRooms][numTimeSlots];
        Class[] classes = input.getClasses();
        int current = 1; 

        // Iterate over each room
        for (int i = 0; i < numRooms; i++) {
            //boolean scheduled = false; // Track if the class is scheduled
            
            // Iterate over each timeslot in the room
            for (int j = 0; j < numTimeSlots && current < classes.length; j++) {
                boolean conflict = false;
                
                Class mostPopularClass = classes[current++];
                for (int k = 0; k < numRooms; k++) {
                    // Check teacher constraint with classes in the same timeslot across other rooms
                    if (schedule[k][j] != null &&
                        checkTeacherConstraint(schedule[k][j], mostPopularClass)) {
                        conflict = true;
                        break;
                    }
                }

                if (!conflict && schedule[i][j] == null) {
                    mostPopularClass.setRoomId(i);  // Update roomId in class object
                    mostPopularClass.setTime(j);     // Update timeslot in class object
                    schedule[i][j] = mostPopularClass;
                }
            }
        }


        printSchedule(schedule);
        return schedule;
    }

    // Checks if there’s a conflict between two classes based on teacher
    private static boolean checkTeacherConstraint(Class class1, Class class2) {
        return class1.isSameTeacher(class2);
    }

    // Print the schedule for debugging or verification
    private static void printSchedule(Class[][] schedule) {
        for (int i = 0; i < schedule.length; i++) {
            System.out.println("Room " + i + ": ");
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
            // Parse input files using ProcessInput
            ScheduleInput input = new ScheduleInput();
            ProcessInput.parseConstraintFile(input, args[0]);
            ProcessInput.parseStudentPrefFile(input, args[1]);

            // Schedule classes with parsed data
            scheduleClasses(input);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }*/
}
