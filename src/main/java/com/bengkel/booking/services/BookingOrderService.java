package com.bengkel.booking.services;

import java.util.List;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.MemberCustomer;
import com.bengkel.booking.utilities.Utility;

public class BookingOrderService {
	private static Utility utility = new Utility();
	private static int bookingIdNumber = 1;

	public static BookingOrder createBooking(List<BookingOrder> listAllBookingOrder, Customer customer,
			List<ItemService> services, String paymentMethod) {
		// create bookingID pattern
		String bookingIdPattern = "Book-'" + customer.getCustomerId() + "'-000";

		// create booking order
		BookingOrder booking = new BookingOrder(utility.createIdPattern(bookingIdPattern, bookingIdNumber++), customer,
				services, paymentMethod, calculateTotalService(services));
		listAllBookingOrder.add(booking);

		// decrease balance if payment method using saldo koin
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

}
