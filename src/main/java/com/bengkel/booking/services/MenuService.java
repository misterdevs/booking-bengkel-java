package com.bengkel.booking.services;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.repositories.CustomerRepository;
import com.bengkel.booking.repositories.ItemServiceRepository;
import com.bengkel.booking.utilities.UtilityInput;
import com.bengkel.booking.utilities.UtilityMenu;

public class MenuService {
	private static List<Customer> listAllCustomers = CustomerRepository.getAllCustomer();
	private static List<ItemService> listAllItemService = ItemServiceRepository.getAllItemService();
	private static UtilityMenu menu = new UtilityMenu();
	private static UtilityInput input = new UtilityInput();
	private static String appName = "Booking Bengkel by MRDevs";

	private static boolean isLogin = false;
	private static int numbOfAttempts = 1;
	private static int suspendTime = 10; // in second
	private static List<Object[]> suspendedCustomers = new ArrayList<Object[]>();

	private static LocalTime suspendUntil = LocalTime.now().plusSeconds(-suspendTime);

	public static void run() {
		UtilityMenu menu = new UtilityMenu();
		String[] loginMenuArr = { "Login" };

		menu.createMenu(chosenMenu -> {
			AuthService.removeExpiredSuspend(suspendedCustomers);
			login(chosenMenu);
			if (isLogin) {
				mainMenu();
				isLogin = false;
			}

		}, appName, loginMenuArr, 0, "Exit");

		System.out.println("==========================================");
		System.out.println("APPLICATION HAS BEEN CLOSED");
		System.out.println("==========================================");
		input.close();
	}

	public static void login(String chosenMenu) {
		menu.resetDisplay();
		System.out.println(menu.displayBorder);
		System.out.println("Login - " + appName);
		System.out.println(menu.displayBorder);

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
									.isValidAuth(UserService.getCustomerById(listAllCustomers, customerId), pass);
							if (Validation.isReachedLimitAttemptsAuth(numbOfAttempts) && !isValid) {
								isLogin = false;
								return true;
							}
							numbOfAttempts++;
							return isValid;
						});

				if (Validation.isReachedLimitAttemptsAuth(numbOfAttempts) && !isLogin) {
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
		menu.createMenu(a -> handleMainMenu(a), appName, mainMenuArr, 0, "Logout");
	}

	public static void handleMainMenu(String chosenMenu) {
		menu.resetDisplay();
		switch (Integer.valueOf(chosenMenu)) {
			case 1:
				// panggil fitur Informasi Customer
				break;
			case 2:
				// panggil fitur Booking Bengkel
				break;
			case 3:
				// panggil fitur Top Up Saldo Coin
				break;
			case 4:
				// panggil fitur Informasi Booking Order
				break;
		}
	}

	// Silahkan tambahkan kodingan untuk keperluan Menu Aplikasi
}
