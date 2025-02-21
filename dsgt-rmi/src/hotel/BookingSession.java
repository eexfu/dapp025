package hotel;

import java.awt.print.Book;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.*;

public class BookingSession extends UnicastRemoteObject implements IBookingSession{
    private BookingManager stub;
    private Set<BookingDetail> cart;

    public BookingSession(BookingManager stub) throws RemoteException {
        super();
        this.stub = stub;
        this.cart = new HashSet<BookingDetail>();
    }

    @Override
    public boolean addBookingDetail(BookingDetail bookingDetail) throws RemoteException {
        return cart.add(bookingDetail);
    }

    @Override
    public boolean bookAll() throws RemoteException {
        boolean flag = true;

        for(BookingDetail detail : cart){
            if(!stub.isRoomAvailable(detail.getRoomNumber(), detail.getDate())){
                cart.clear();
                flag = false;
            }
        }

        for(BookingDetail detail : cart){
            stub.addBooking(detail);
        }

        return flag;
    }
}
