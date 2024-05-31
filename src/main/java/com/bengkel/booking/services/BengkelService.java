package com.bengkel.booking.services;

import java.util.List;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.MemberCustomer;
import com.bengkel.booking.utilities.Utility;

public class BengkelService {
	private static Utility utility = new Utility();
	private static int bookingIdNumber = 1;

	public static BookingOrder createBooking(List<BookingOrder> listAllBookingOrder, Customer customer,
			List<ItemService> services, String paymentMethod) {
		String bookingIdPattern = "Book-'" + customer.getCustomerId() + "'-000";
		BookingOrder booking = new BookingOrder(utility.createIdPattern(bookingIdPattern, bookingIdNumber++), customer,
				services, paymentMethod, calculateTotalService(services));
		listAllBookingOrder.add(booking);
		if (Validation.isMember(customer) && paymentMethod == getPaymentMethodById(2)) {
			recalculateBalanceMember((MemberCustomer) customer, booking.getTotalPayment());
		}

		return booking;
	}

	public static String getPaymentMethodById(int id) {
		return id == 2 ? "Saldo Koin" : "Cash";
	}

	public static void recalculateBalanceMember(MemberCustomer customer, double totalPayment) {
		customer.setSaldoCoin(customer.getSaldoCoin() - totalPayment);
	}

	public static double calculateTotalService(List<ItemService> services) {
		return services.stream().reduce(0.0, (a, b) -> a + b.getPrice(), Double::sum);
	}

	public static int getLimitChosenService(Customer customer) {
		return customer.getMaxNumberOfService();
	}

	// Silahkan tambahkan fitur-fitur utama aplikasi disini

	// Login

	// Info Customer

	// Booking atau Reservation

	// Top Up Saldo Coin Untuk Member Customer

	// Logout

}
