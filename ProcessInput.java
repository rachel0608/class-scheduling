// Process constraint and student pref files and store data in ScheduleInput object

/* Note:
 * - All arrays have a 0 index element that is not used for easier access to elements by ID if needed
 * - Rooms and Classes are sorted in place by size and frequency, respectively. We don't need to 
 * access elements by ID yet, if we do, we can modify by creating a copy of the array and sorting that
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class ProcessInput {
    public static final int KEY_POSITION = 0; // Position of the key (i.e. class time) in the input file
    public static final int VALUE_POSITION = 1; // Position of the value (i.e. 4) in the input file
    public static final int NUM_PREFS = 4; // Number of preferences each student has

    // Parse the constraint file and sort rooms by size
    public static void parseConstraintFile(ScheduleInput input, String filePath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filePath));

        // Parse timeslots
        String line = scanner.nextLine();
        input.setNumTimeslots(Integer.parseInt(line.split("\t")[VALUE_POSITION]));

        // Parse rooms
        line = scanner.nextLine();
        int numRooms = Integer.parseInt(line.split("\t")[VALUE_POSITION]);
        input.setRooms(new Room[numRooms+1]); // Room IDs are 1-indexed in input
        for (int i = 0; i < numRooms; i++) {
            line = scanner.nextLine();
            String[] roomData = line.split("\t"); // A line contains room ID and size
            int roomId = Integer.parseInt(roomData[KEY_POSITION]);  
            int size = Integer.parseInt(roomData[VALUE_POSITION]);
            Room room = new Room(roomId, size);
            input.getRooms()[roomId] = room;
        }

        // Sort rooms by size
        Arrays.sort(input.getRooms(), 1, input.getRooms().length, (a, b) -> b.getSize() - a.getSize());

        // Parse classes and teachers
        line = scanner.nextLine();
        int numClasses = Integer.parseInt(line.split("\t")[VALUE_POSITION]);
        line = scanner.nextLine();
        int numTeachers = Integer.parseInt(line.split("\t")[VALUE_POSITION]);
        if (numTeachers != numClasses/2) {
            System.out.println("Number of teachers must be half the number of classes");
            System.exit(1);
        }
        
        // Parse class-teacher assignments
        input.setClasses(new Class[numClasses+1]); // Class IDs are 1-indexed in input
        for (int i = 0; i < numClasses; i++) {
            line = scanner.nextLine();
            String[] classData = line.split("\t");
            int classId = Integer.parseInt(classData[KEY_POSITION]);  
            int teacherId = Integer.parseInt(classData[VALUE_POSITION]);
            Class newClass = new Class(classId, teacherId, 0, 0, 0);
            input.getClasses()[classId] = newClass;
        }
        
        scanner.close();
    }

    // Parse the student pref file and sort classes by frequency
    public static void parseStudentPrefFile(ScheduleInput input, String filePath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filePath));

        // Parse number of students
        String line = scanner.nextLine();
        input.setNumStudents(Integer.parseInt(line.split("\t")[VALUE_POSITION]));

        // Parse student preferences
        input.setStudents(new Student[input.getNumStudents()+1]); // Student IDs are 1-indexed in input
        for (int i = 1; i <= input.getNumStudents(); i++) {
            line = scanner.nextLine();
            String[] studentData = line.split("\t");
            int studentId = Integer.parseInt(studentData[KEY_POSITION]);
            String[] prefData = studentData[VALUE_POSITION].split(" ");
            int[] preferences = new int[prefData.length];
            if (prefData.length != NUM_PREFS) {
                System.out.println("Each student must have 4 preferences");
                System.out.println(prefData.length);
                System.exit(1);
            }

            for (int j = 0; j < prefData.length; j++) {
                preferences[j] = Integer.parseInt(prefData[j]);
                input.getClasses()[preferences[j]].setFrequency(input.getClasses()[preferences[j]].getFrequency() + 1); // Increment class frequency
            }
            Student student = new Student(studentId, preferences);
            input.getStudents()[studentId] = student;
        }

        // Sort classes by frequency
        Arrays.sort(input.getClasses(), 1, input.getClasses().length, (a, b) -> b.getFrequency() - a.getFrequency());
        scanner.close();
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Scheduler <constraint file> <pref file>");
            System.exit(1);
        }

        try {
            ScheduleInput input = new ScheduleInput();
            parseConstraintFile(input, args[0]);
            parseStudentPrefFile(input, args[1]);
            System.out.println("Number of timeslots: " + input.getNumTimeslots());
            System.out.println("\nNumber of rooms: " + (input.getRooms().length-1)); // Subtract 1 to exclude the 0th element
            for (int i = 1; i < input.getRooms().length; i++) {
                System.out.println("ID: " + input.getRooms()[i].getRoomId() + " - Size: " + input.getRooms()[i].getSize());
            }
            
            System.out.println("\nNumber of classes: " + (input.getClasses().length-1)); // Subtract 1 to exclude the 0th element
            for (int i = 1; i < input.getClasses().length; i++) {
                System.out.println(input.getClasses()[i]);
            }

            System.out.println("\nNumber of students: " + input.getNumStudents());
            for (int i = 1; i < input.getStudents().length; i++) {
                System.out.println(input.getStudents()[i]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + args[0]);
        }
    }
}
