// Room class to store room information

public class Room {
    private int roomId;
    private int size;

    public Room(int roomId, int size) {
        this.roomId = roomId;
        this.size = size;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getSize() {
        return size;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String toString() {
        return "Room: " + roomId + ", Size: " + size;
    }
}
