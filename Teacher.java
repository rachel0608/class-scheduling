// Teacher class to store teacher ID and list of classes they teach

public class Teacher {
    private int teacherId;
    private int[] classes; 

    public Teacher(int teacherId, int[] classes) {
        this.teacherId = teacherId;
        this.classes = classes;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public int[] getClasses() {
        return classes;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }    
} 