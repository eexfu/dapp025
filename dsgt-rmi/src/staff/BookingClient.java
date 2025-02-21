package staff;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.util.Set;

import hotel.BookingDetail;
import hotel.IBookingManager;
import hotel.BookingSession;
import hotel.IBookingSession;

public class BookingClient extends AbstractScriptedSimpleTest {

	private IBookingManager stub = null;

	public static void main(String[] args) throws Exception {
		String host = (args.length < 1) ? null : args[0];
		BookingClient client = new BookingClient(host);
		client.run();
		client.runMultiThreadTest();
//		client.runSessionTest();
	}

	/***************
	 * CONSTRUCTOR *
	 ***************/
	public BookingClient(String host) {
		try {
			//Look up the registered remote instance
			Registry registry = LocateRegistry.getRegistry(host);
			stub = (IBookingManager) registry.lookup("BookingManager");
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	@Override
	public boolean isRoomAvailable(Integer roomNumber, LocalDate date) throws RemoteException {
		//Implement this method
		boolean flag = stub.isRoomAvailable(roomNumber, date);
		return flag;
	}

	@Override
	public void addBooking(BookingDetail bookingDetail) throws RemoteException {
		//Implement this method
		stub.addBooking(bookingDetail);
	}

	@Override
	public Set<Integer> getAvailableRooms(LocalDate date) throws RemoteException {
		//Implement this method
        return stub.getAvailableRooms(date);
	}

	@Override
	public Set<Integer> getAllRooms() throws RemoteException {
		return stub.getAllRooms();
	}

	/******************
	 * MULTITHREADING *
	 ******************/

	public void runMultiThreadTest(){
		LocalDate bookingDate = LocalDate.now();
		int numberOfthread = 10;
		Thread[] threads = new Thread[numberOfthread];

		for(int i = 0; i < numberOfthread; i++){
			String clientName = "Client" + i;
			int roomNumber = i > 5 ? 101 : 102;
			threads[i] = new Thread(new BookingTask(stub, clientName, roomNumber, bookingDate));
			threads[i].start();
		}

		for(Thread thread : threads){
			try{
				thread.join();
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	static class BookingTask implements Runnable{
		private final IBookingManager stub;
		private final String clientName;
		private final int roomNumber;
		private final LocalDate date;

        BookingTask(IBookingManager stub, String clientName, int roomNumber, LocalDate date) {
            this.stub = stub;
            this.clientName = clientName;
            this.roomNumber = roomNumber;
            this.date = date;
        }

        @Override
		public void run(){
			try{
				System.out.println("Start booking the room!");
				stub.addBooking(new BookingDetail(clientName, roomNumber, date));
				System.out.println("Booking the room " + roomNumber + " successfully?");
			}
			catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/******************
	 * BOOKING SESSION *
	 ******************/
	public void runSessionTest() throws RemoteException {
		try{
			IBookingSession session = stub.getBookingSession();

			session.addBookingDetail(new BookingDetail("Alice", 101, LocalDate.of(2025, 3, 15)));
			session.addBookingDetail(new BookingDetail("Bob", 102, LocalDate.of(2025, 3, 15)));

			boolean res = session.bookAll();
			String result = res ? "succeed" : "failed";
			System.out.println("Finish session test! Result: " + result);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
