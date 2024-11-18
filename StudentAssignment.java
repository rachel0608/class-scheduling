import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class StudentAssignment {

    public static void assignStudentsToClasses(ScheduleInput input) {
        int numStudents = input.getNumStudents();
        Student[] students = input.getStudents();
        Class[] classes = new Class[input.getClasses().length + 1];
        int count = 0;

        for (int i = 1; i < input.getClasses().length; i++) {
            classes[input.getClasses()[i].getClassId()] = input.getClasses()[i];
        }
        boolean[][] studentSchedule = new boolean[numStudents + 1][input.getNumTimeslots()];
        int[][] studentAssignments = new int[numStudents + 1][classes.length];

        // Initialize room capacities
        int numRooms = input.getRooms().length - 1; // rooms are 1-indexed
        int[] roomCapacities = new int[numRooms + 1];
        for (int i = 1; i < input.getRooms().length; i++) {
            Room currentRoom = input.getRooms()[i];
            int roomId = currentRoom.getRoomId();
            roomCapacities[roomId] = currentRoom.getSize();
        }

        System.out.println("room id: " + 1 + " capacity " +  roomCapacities[1] );
        System.out.println("room id: " + 2 + " capacity " +  roomCapacities[2] );
        System.out.println("room id: " + 3 + " capacity " +  roomCapacities[3] );
        System.out.println("room id: " + 4 + " capacity " +  roomCapacities[4] );


        // Assign students to classes based on their preferences
        for (int studentId = 1; studentId <= numStudents; studentId++) {
            Student student = students[studentId];
            boolean assigned = false;

            for (int preferenceIndex = 0; preferenceIndex < student.getPreferences().length; preferenceIndex++) {
                int preferredClassId = student.getPreferences()[preferenceIndex];
                Class cls = classes[preferredClassId];

                // if a course has not been assigned in the schedule, skip that course
                if (!ScheduleClass.isClassScheduled(preferredClassId))
                    continue; 

                int roomId = cls.getRoomId();
                // System.out.println("room id: " + roomId + " capacity " +  roomCapacities[roomId] );
                int timeslot = cls.getTimeslot();

                // if (studentId < 800) {
                //     System.out.println("Student " + studentId + " pref " + preferredClassId + " Room " + roomId
                //     + " Timeslot " + timeslot + " RoomCap " + roomCapacities[roomId] + " Free: "
                //     + !studentSchedule[studentId][timeslot]);

                // }
                // System.out.println(" RoomCap of " + roomId + " is " + roomCapacities[roomId]);
                // Check if room has capacity and student is free at that timeslot
                if (roomCapacities[roomId] > 0 && !studentSchedule[studentId][timeslot]) {
                    count++;
                    studentAssignments[studentId][preferredClassId] = 1; // Mark the class as assigned to the student
                    studentSchedule[studentId][timeslot] = true; // Mark this timeslot as occupied for the student
                    // System.out.println("Decrementing room capacity for Room " + roomId + 
                //    ". Current capacity: " + roomCapacities[roomId] + " this is count: " + count);

                    roomCapacities[roomId]--; // Decrease room capacity
                    assigned = true;
                }
                // System.out.println("Trying to assign Student " + studentId + " to Class " + preferredClassId
                        // + " at Timeslot " + timeslot + " in Room " + roomId);
            }

        }

        // Output the assignments for verification
        printStudentAssignments(input, studentAssignments);
        // System.out.println("Student schedule:");
        // for (int i = 0; i < studentSchedule.length; i++) {
        //     System.out.println("Student " + i + ": " + Arrays.toString(studentSchedule[i]));
        // }
        // System.out.println("Student Assignments:");
        // for (int i = 1; i < studentAssignments.length; i++) {
        //     System.out.println("Student " + i + ": " + Arrays.toString(studentAssignments[i]));
        // }
    }

    private static void printStudentAssignments(ScheduleInput input, int[][] studentAssignments) {
        try (PrintWriter writer = new PrintWriter("studentAssignment.txt", "UTF-8")) {
            writer.println("Course\tRoom\tTeacher\tTime\tStudents");

            Class[] classes = input.getClasses();

            // Iterate over each class
            for (int classIndex = 1; classIndex < classes.length; classIndex++) {
                Class cls = classes[classIndex];
                int classId = cls.getClassId(); // use classId directly from the Class object

                StringBuilder line = new StringBuilder(classId + "\t" + cls.getRoomId() + "\t"
                        + cls.getTeacherId() + "\t" + cls.getTimeslot() + "\t");

                StringBuilder studentsList = new StringBuilder();

                // students assigned to this specific classId
                for (int studentId = 1; studentId < studentAssignments.length; studentId++) {
                    if (studentAssignments[studentId][classId] == 1) { // Check assignment using classId
                        studentsList.append(studentId).append(" ");
                    }
                }

                // Only print details if there are students assigned to this class
                if (studentsList.length() > 0) {
                    line.append(studentsList.toString().trim()); // Add student IDs to the line
                    writer.println(line); // Write the line to the file
                }
            }
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java StudentAssignment <constraint file> <pref file>");
            System.exit(1);
        }

        try {
            ScheduleInput input = new ScheduleInput();

            // Assuming ProcessInput class has static methods to parse files
            ProcessInput.parseConstraintFile(input, args[0]);
            ProcessInput.parseStudentPrefFile(input, args[1]);

            Class[][] scheduledClasses = ScheduleClass.scheduleClasses(input);
            // Directly assign students to classes
            assignStudentsToClasses(input);

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
