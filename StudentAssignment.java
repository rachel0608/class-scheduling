import java.io.FileNotFoundException;

public class StudentAssignment {

    public static void assignStudentsToClasses(ScheduleInput input, Class[][] schedule) {
        int numStudents = input.getNumStudents();
        Student[] students = input.getStudents();
        Room[] rooms = input.getRooms();

        boolean[][] studentSchedule = new boolean[numStudents + 1][input.getNumTimeslots()];
        int[][] studentAssignments = new int[numStudents + 1][input.getClasses().length];

        // Initialize room capacities
        int[] roomCapacities = new int[rooms.length];
        for (int i = 1; i < rooms.length; i++) {
            roomCapacities[i] = rooms[i].getSize();
        }

        // Assign students to classes based on their preferences and availability of
        // slots
        for (int studentId = 1; studentId <= numStudents; studentId++) {
            Student student = students[studentId];
            int[] preferences = student.getPreferences();
            int assignedCount = 0;

            for (int prefIndex = 0; prefIndex < preferences.length; prefIndex++) {
                int classId = preferences[prefIndex];
                boolean assigned = false;

                // Check all rooms and timeslots for the preferred class
                for (int roomId = 1; roomId < schedule.length && !assigned; roomId++) {
                    for (int timeslot = 0; timeslot < schedule[roomId].length && !assigned; timeslot++) {
                        if (schedule[roomId][timeslot] != null && schedule[roomId][timeslot].getClassId() == classId) {
                            // Check if student can be assigned to this class at this timeslot
                            if (!studentSchedule[studentId][timeslot] && roomCapacities[roomId] > 0) {
                                studentAssignments[studentId][classId] = 1; // Assign student to class
                                studentSchedule[studentId][timeslot] = true; // Mark timeslot as busy for this student
                                roomCapacities[roomId]--; // Decrease room capacity
                                assigned = true; // Mark as assigned
                                assignedCount++;
                            }
                        }
                    }
                }
            }

            // Optional: Handle cases where students could not be assigned to all their preferred classes
            if (assignedCount < preferences.length) {
                System.out.println("Student " + studentId + " could not be assigned to all preferred classes.");
            }
        }

        // Print the assignments for verification
        for (int studentId = 1; studentId <= numStudents; studentId++) {
            System.out.print("Student " + studentId + " assigned to classes: ");
            for (int classId = 1; classId < studentAssignments[studentId].length; classId++) {
                if (studentAssignments[studentId][classId] == 1) {
                    System.out.print(classId + " ");
                }
            }
            System.out.println();
        }
    }
        public static void main(String[] args) {
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
            Class[][] scheduledClasses = ScheduleClass.scheduleClasses(input);

            // Assign students to classes based on their preferences and the created schedule
            assignStudentsToClasses(input, scheduledClasses);

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        }
}
