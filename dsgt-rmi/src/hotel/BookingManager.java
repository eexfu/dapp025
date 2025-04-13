package hotel;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.time.LocalDate;
import java.util.*;

public class BookingManager implements IBookingManager {

	private Room[] rooms;

	public static void main(String args[]){
		try{
			System.setProperty("java.rmi.server.hostname", "172.171.215.140");
			BookingManager obj = new BookingManager();
			IBookingManager stub = (IBookingManager) UnicastRemoteObject.exportObject(obj, 5001);

			Registry registry = LocateRegistry.createRegistry(5000);
			registry.rebind("BookingManager", stub);

			System.err.println("Server ready");
		}
		catch(Exception e){
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}

	public BookingManager() {
		this.rooms = initializeRooms();
	}

	@Override
	public synchronized Set<Integer> getAllRooms() {
		Set<Integer> allRooms = new HashSet<Integer>();
		Iterable<Room> roomIterator = Arrays.asList(rooms);
		for (Room room : roomIterator) {
			allRooms.add(room.getRoomNumber());
		}
		return allRooms;
	}

	@Override
	public synchronized boolean isRoomAvailable(Integer roomNumber, LocalDate date) {
		//implement this method
		boolean available = false;
        for (Room room : this.rooms) {
            if (Objects.equals(room.getRoomNumber(), roomNumber)) {
                List<BookingDetail> bookings = room.getBookings();
                available = true;
                for (BookingDetail booking : bookings) {
                    if (booking.getDate().equals(date)) {
                        available = false;
                        break;
                    }
                }
            }
        }
		return available;
	}

	@Override
	public synchronized void addBooking(BookingDetail bookingDetail) {
		//implement this method
		for(Room room : this.rooms){
			List<BookingDetail> bookings = room.getBookings();
			boolean available = true;
			for(BookingDetail booking : bookings){
                if (booking.getDate().equals(bookingDetail.getDate())) {
                    available = false;
                    break;
                }
			}

			try {
				// slow down to test concurrency problem
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if(available){
				if(Objects.equals(room.getRoomNumber(), bookingDetail.getRoomNumber())){
					room.getBookings().add(bookingDetail);
				}
			}
			else{
				System.err.println("There is a booking in the date!");
			}
		}
	}

	@Override
	public synchronized Set<Integer> getAvailableRooms(LocalDate date) {
		//implement this method
		Set<Integer> rooms = new HashSet<>();
		for(Room room : this.rooms){
			boolean available = true;
			List<BookingDetail> bookings = room.getBookings();
			for(BookingDetail Booking : bookings){
                if (Booking.getDate().equals(date)) {
                    available = false;
                    break;
                }
			}
			if(available){
				rooms.add(room.getRoomNumber());
			}
		}
		return rooms;
	}

	@Override
	public synchronized IBookingSession getBookingSession() throws RemoteException {
		return new BookingSession(this);
	}

	private static Room[] initializeRooms() {
		Room[] rooms = new Room[4];
		rooms[0] = new Room(101);
		rooms[1] = new Room(102);
		rooms[2] = new Room(201);
		rooms[3] = new Room(203);
		return rooms;
	}
}
