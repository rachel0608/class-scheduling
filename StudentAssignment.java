import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class StudentAssignment {

    public static void assignStudentsToClasses(ScheduleInput input, String outputFile) {
        int numStudents = input.getNumStudents();
        int numTimeSlots = input.getNumTimeslots();
        Student[] students = input.getStudents();
        Class[] classes = new Class[input.getClasses().length + 1];
        Class[][] schedule = ScheduleClass.scheduleClasses(input);
        int total = 0; //total fit value 
        int count = 0; //count current fit value 

        for (int i = 1; i < input.getClasses().length; i++) {
            classes[input.getClasses()[i].getClassId()] = input.getClasses()[i];
        }

        boolean[][] studentSchedule = new boolean[numStudents + 1][input.getNumTimeslots()];
        int[][] studentAssignments = new int[numStudents + 1][classes.length];

        // Initialize room capacities
        int numRooms = input.getRooms().length - 1; // rooms are 1-indexed
        int[][] roomTimeslotCapacities = new int[numRooms + 1][numTimeSlots + 1];

        //adding each room size (for each timeslot) into a 2D array
        for (int i = 1; i < schedule.length; i++) {
            Room currentRoom = input.getRooms()[i];
            int roomId = currentRoom.getRoomId();
            for (int j = 0; j < schedule[i].length; j++) {
                roomTimeslotCapacities[roomId][j] = currentRoom.getSize();
            }
        }

        // Assign students to classes based on their preferences
        for (int studentId = 1; studentId <= numStudents; studentId++) {
            Student student = students[studentId];

            // Loop through student's preferred list 
            for (int preferenceIndex = 0; preferenceIndex < student.getPreferences().length; preferenceIndex++) {
                total += 1;
                int preferredClassId = student.getPreferences()[preferenceIndex];
                Class cls = classes[preferredClassId];

                // if a course has not been assigned in the schedule, skip that course
                if (!ScheduleClass.isClassScheduled(preferredClassId))
                    continue; 

                int roomId = cls.getRoomId();
                int timeslot = cls.getTimeslot();

                // Check if room has capacity and student is free at that timeslot
                if (roomTimeslotCapacities[roomId][timeslot] > 0 && !studentSchedule[studentId][timeslot]) {
                    count++;
                    studentAssignments[studentId][preferredClassId] = 1; // Mark the class as assigned to the student
                    studentSchedule[studentId][timeslot] = true; // Mark this timeslot as occupied for the student
                    roomTimeslotCapacities[roomId][timeslot]--; // Decrease room capacity
                }
            }
        }

        // Checks how many percent of the total fit value we have 
        System.out.println("Assigned " + count + " students out of " + total + ", which is " + (((double)count/total) * 100)+ " percent");

        // Output the assignments for verification
        printStudentAssignments(input, studentAssignments, outputFile);
    }

    // Write to file function
    private static void printStudentAssignments(ScheduleInput input, int[][] studentAssignments, String outputFile) {
        try (PrintWriter writer = new PrintWriter(outputFile, "UTF-8")) {
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
        if (args.length != 3) { 
            System.out.println("Usage: java StudentAssignment <constraint file> <pref file> <output file>");
            System.exit(1);
        }

        try {
            ScheduleInput input = new ScheduleInput();

            // Assuming ProcessInput class has static methods to parse files
            ProcessInput.parseConstraintFile(input, args[0]);
            ProcessInput.parseStudentPrefFile(input, args[1]);

            Class[][] scheduledClasses = ScheduleClass.scheduleClasses(input);

            // Directly assign students to classes
            assignStudentsToClasses(input, args[2]);

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
