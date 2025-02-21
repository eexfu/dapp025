package staff;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.util.Set;

import hotel.BookingDetail;
import hotel.BookingManagerInterface;

public class BookingClient extends AbstractScriptedSimpleTest {

	private BookingManagerInterface stub = null;

	public static void main(String[] args) throws Exception {
		String host = (args.length < 1) ? null : args[0];
		BookingClient client = new BookingClient(host);
		client.run();
	}

	/***************
	 * CONSTRUCTOR *
	 ***************/
	public BookingClient(String host) {
		try {
			//Look up the registered remote instance
			Registry registry = LocateRegistry.getRegistry(host);
			stub = (BookingManagerInterface) registry.lookup("BookingManager");
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
}
