import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.IOException;

public class StudentAssignment {

    public static void assignStudentsToClasses(ScheduleInput input) {
        int numStudents = input.getNumStudents();
        Student[] students = input.getStudents();
        Room[] rooms = input.getRooms();
        Class[] classes = input.getClasses();

        boolean[][] studentSchedule = new boolean[numStudents + 1][input.getNumTimeslots()];
        int[][] studentAssignments = new int[numStudents + 1][classes.length];

        // Initialize room capacities
        int[] roomCapacities = new int[rooms.length];
        for (int i = 1; i < rooms.length; i++) {
            roomCapacities[i] = rooms[i].getSize();
        }

        // Assign students to classes based on their preferences
        for (int studentId = 1; studentId <= numStudents; studentId++) {
            Student student = students[studentId];
            for (int preference : student.getPreferences()) {
                Class cls = classes[preference];
                int roomId = cls.getRoomId();
                int timeslot = cls.getTimeslot();

                // Check if room has capacity and student is free at that timeslot
                if (roomCapacities[roomId] > 0 && !studentSchedule[studentId][timeslot]) {
                    studentAssignments[studentId][preference] = 1; // Assign the student to the class
                    studentSchedule[studentId][timeslot] = true; // Mark this timeslot as occupied
                    roomCapacities[roomId]--; // Decrement room capacity
                }
            }
        }

        // Output the assignments for verification
        printStudentAssignments(studentAssignments, students);
    }

    private static void printStudentAssignments(int[][] studentAssignments, Student[] students) {
        try (PrintWriter writer = new PrintWriter("studentAssignments.txt", "UTF-8")) {
            for (int studentId = 1; studentId < studentAssignments.length; studentId++) {
                writer.print("Student " + studentId + " assigned to classes: ");
                for (int classId = 1; classId < studentAssignments[studentId].length; classId++) {
                    if (studentAssignments[studentId][classId] == 1) {
                        writer.print(classId + " ");
                    }
                }
                writer.println(); // Move to the next line after printing all classes for a student
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

            // Assuming ScheduleClass has a static method scheduleClasses
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
