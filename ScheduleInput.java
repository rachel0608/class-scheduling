import java.util.Arrays;

public class ScheduleInput {
    private int numTimeslots;
    private int numStudents;
    private Class[] classes; // Array of classes
    private Room[] rooms; // Array of rooms
    private Student[] students; // Array of student IDs and their preferences

    public ScheduleInput() {
    }

    public int getNumTimeslots() {
        return numTimeslots;
    }

    public int getNumStudents() {
        return numStudents;
    }

    public Class[] getClasses() {
        return classes;
    }

    public Room[] getRooms() {
        return rooms;
    }

    public Student[] getStudents() {
        return students;
    }

    public void setNumTimeslots(int numTimeslots) {
        this.numTimeslots = numTimeslots;
    }

    public void setNumStudents(int numStudents) {
        this.numStudents = numStudents;
    }

    public void setClasses(Class[] classes) {
        this.classes = classes;
    }

    public void setRooms(Room[] rooms) {
        this.rooms = rooms;
    }

    public void setStudents(Student[] students) {
        this.students = students;
    }

    public String toString() {
        return "ScheduleInput{" +
                "numTimeslots=" + numTimeslots +
                ", numStudents=" + numStudents +
                ", classes=" + Arrays.toString(classes) +
                ", rooms=" + Arrays.toString(rooms) +
                ", students=" + Arrays.toString(students) +
                '}';
    }
}