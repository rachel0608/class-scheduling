// Student class that represents a student with a student ID and a list of preferences

public class Student {
    private int studentId;
    private int[] preferences; // each student has a list of 4 classes they want to take

    public Student(int studentId, int[] preferences) {
        this.studentId = studentId;
        this.preferences = preferences;
    }

    public int getStudentId() {
        return studentId;
    }

    public int[] getPreferences() {
        return preferences;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setPreferences(int[] preferences) {
        this.preferences = preferences;
    }

    public String toString() {
        return "Student: " + studentId + ", Preferences: " + preferences[0] + " " + preferences[1] + " " + preferences[2] + " " + preferences[3];
    }
}
