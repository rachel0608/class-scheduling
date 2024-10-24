// Class to represent a class with a class ID, teacher ID, room ID, and time

public class Class {
    private int classId;
    private int teacherId;
    private int roomId;
    private int timeslot;
    private int frequency;

    public Class(int classId, int teacherId, int roomId, int timeslot, int frequency) {
        this.classId = classId;
        this.teacherId = teacherId;
        this.roomId = roomId;
        this.timeslot = timeslot;
        this.frequency = frequency;
    }

    public int getClassId() {
        return classId;
    }
    
    public int getTeacherId() {
        return teacherId;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getTimeslot() {
        return timeslot;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setTime(int timeslot) {
        this.timeslot = timeslot;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public boolean isSameTeacher(Class other) {
        return this.teacherId == other.teacherId;
    }

    public String toString() {
        return "Class: " + classId + ", Teacher: " + teacherId + ", Room: " + 
        roomId + ", Timeslot: " + timeslot + ", Frequency: " + frequency;
    }
}
