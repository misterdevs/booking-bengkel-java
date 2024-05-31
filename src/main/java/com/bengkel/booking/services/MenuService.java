package com.bengkel.booking.services;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.Vehicle;
import com.bengkel.booking.repositories.CustomerRepository;
import com.bengkel.booking.repositories.ItemServiceRepository;
import com.bengkel.booking.utilities.UtilityInput;
import com.bengkel.booking.utilities.UtilityMenu;

public class MenuService {
	private static List<Customer> listAllCustomers = CustomerRepository.getAllCustomer();
	private static List<ItemService> listAllItemService = ItemServiceRepository.getAllItemService();
	private static List<BookingOrder> listAllBookingOrder = new ArrayList<BookingOrder>();
	private static UtilityMenu menu = new UtilityMenu();
	private static UtilityInput input = new UtilityInput();
	private static String appName = "Booking Bengkel";
	private static String appSubName = "by MRDevs";

	private static Customer loggedInCustomer = null;
	private static boolean isLogin = false;
	private static int numbOfAttempts = 1;
	private static int suspendTime = 10; // in second
	private static List<Object[]> suspendedCustomers = new ArrayList<Object[]>();

	private static LocalTime suspendUntil = LocalTime.now().plusSeconds(-suspendTime);

	public static void run() {
		UtilityMenu menu = new UtilityMenu();
		String[] loginMenuArr = { "Login" };

		// FOR DEVELOPMENT ONLY
		// isLogin = true;
		// loggedInCustomer = listAllCustomers.get(2);
		// mainMenu();
		// FOR DEVELOPMENT ONLY

		menu.createMenu(chosenMenu -> {
			AuthService.removeExpiredSuspend(suspendedCustomers);
			login(chosenMenu);
			if (isLogin) {
				mainMenu();
				isLogin = false;
				loggedInCustomer = null;
			}

		}, appSubName, appName, loginMenuArr, 0, "Exit");

		System.out.println("==========================================");
		System.out.println("APPLICATION HAS BEEN CLOSED");
		System.out.println("==========================================");
		input.close();
	}

	public static void login(String chosenMenu) {
		menu.resetDisplay();
		menu.printTitleCustom("Login", 1, 0);
		menu.printTitleCustom("Booking Bengkel", 0, 1);
		if (Integer.valueOf(chosenMenu) != 0) {
			numbOfAttempts = 1;
			isLogin = true;

			String customerId = input.validate("Masukkan Customer ID", "Customer ID tidak terdaftar!",
					id -> Validation.isValidUser(listAllCustomers, id));

			Object[] suspendedCustomer = AuthService.getSuspendedUserById(suspendedCustomers, customerId);
			if (Validation.isSuspendedCustomer(suspendedCustomer)) {
				System.out.println("Customer ID ini tersuspend. Coba lagi dalam "
						+ AuthService.getLeftTimeSuspend((LocalTime) suspendedCustomer[1])
						+ " detik");
				isLogin = false;
				menu.enterToContinue();

			} else {
				input.validate("Masukkan Password", "Password salah!",
						pass -> {
							boolean isValid = Validation
									.isValidAuth(CustomerService.getCustomerById(listAllCustomers, customerId), pass);
							if (Validation.isReachedLimitAttemptsAuth(numbOfAttempts) && !isValid) {
								isLogin = false;
								return true;
							}
							numbOfAttempts++;
							return isValid;
						});

				if (isLogin) {
					loggedInCustomer = CustomerService.getCustomerById(listAllCustomers, customerId);
				} else if (Validation.isReachedLimitAttemptsAuth(numbOfAttempts) && !isLogin) {
					suspendUntil = LocalTime.now().plusSeconds(suspendTime);
					suspendedCustomers.add(new Object[] { customerId, suspendUntil });
					System.out.println("Percobaan sudah 3 kali. Coba lagi dalam " + suspendTime + " detik.");
					menu.enterToContinue();

				}
			}

		}
	}

	public static void mainMenu() {
		String[] mainMenuArr = { "Informasi Customer", "Booking Bengkel", "Top Up Bengkel Coin", "Informasi Booking" };
		menu.createMenu(a -> handleMainMenu(a), appSubName, appName, mainMenuArr, 0, "Logout");
	}

	public static void handleMainMenu(String chosenMenu) {

		menu.resetDisplay();
		switch (Integer.valueOf(chosenMenu)) {
			case 1:
				// panggil fitur Informasi Customer

				PrintService.showCustomerProfile(loggedInCustomer);
				menu.enterToContinue();
				break;
			case 2:
				// panggil fitur Booking Bengkel
				PrintService.showBooking(listAllBookingOrder, listAllItemService, loggedInCustomer);
				menu.enterToContinue();
				break;
			case 3:
				// panggil fitur Top Up Saldo Coin
				PrintService.showTopupSaldo(loggedInCustomer);
				break;
			case 4:
				// panggil fitur Informasi Booking Order
				PrintService.showBookingOrder(listAllBookingOrder);
				menu.enterToContinue();
				break;
		}
	}

	// Silahkan tambahkan kodingan untuk keperluan Menu Aplikasi
}
