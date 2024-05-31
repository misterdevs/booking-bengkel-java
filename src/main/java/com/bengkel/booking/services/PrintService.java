package com.bengkel.booking.services;

import java.util.ArrayList;
import java.util.List;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Car;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.MemberCustomer;
import com.bengkel.booking.models.Motorcycle;
import com.bengkel.booking.models.Vehicle;
import com.bengkel.booking.utilities.UtilityInput;
import com.bengkel.booking.utilities.UtilityMenu;

import de.vandermeer.asciithemes.TA_GridThemes;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

public class PrintService {
	private static UtilityMenu menu = new UtilityMenu();
	private static UtilityInput input = new UtilityInput();

	public static String printServices(List<ItemService> services) {
		String result = "";
		for (int i = 0; i < services.size(); i++) {
			if (i > 0) {
				result += ", ";
			}
			result += services.get(i).getServiceName();
		}
		return result;
	}

	public static void showCustomerProfile(Customer customer) {
		menu.printTitle("Customer Profile");
		menu.createTable(table -> {
			table.addRow("Customer ID", ": " + customer.getCustomerId());
			table.addRow("Customer Status", Validation.isMember(customer) ? ": Member" : ": Non-Member");
			table.addRow("Nama", ": " + customer.getName());
			table.addRow("Alamat", ": " + customer.getAddress());
			if (Validation.isMember(customer))
				table.addRow("Saldo Koin",
						": " + menu.currencyFormatter((int) ((MemberCustomer) customer).getSaldoCoin()));
			table.addRow("List Kendaraan", ": ");
			table.getContext().setGridTheme(TA_GridThemes.NONE);
			System.out.println(table.render());
		});

		printTableVehiclesByCustomer(customer);
	}

	public static void showBooking(List<BookingOrder> bookingOrders, List<ItemService> services, Customer customer) {
		List<ItemService> chosenServices = new ArrayList<ItemService>();

		menu.printTitle("Booking Bengkel");
		String vehicleId = input.validate("Masukkan Vehicle ID", "Vechile ID tidak ditemukan!",
				s -> CustomerService.findVehicleCustomerByVehicleId(customer.getVehicles(), s) != null);

		// initialize
		Vehicle vehicle = CustomerService.findVehicleCustomerByVehicleId(customer.getVehicles(), vehicleId);
		List<ItemService> servicesByType = ItemServiceService.getServiceByVehicleType(services,
				vehicle.getVehicleType());

		menu.resetDisplay();
		menu.printTitle("Booking Bengkel");
		menu.createTable(table -> {
			table.addRow("Vehicle ID", ": " + vehicle.getVehiclesId());
			table.addRow("Tipe Kendaraan", ": " + vehicle.getVehicleType());
			table.addRow("Merk", ": " + vehicle.getBrand());
			table.addRow("Tahun", ": " + vehicle.getYearRelease());
			table.addRow("List Service", ": ");
			table.getContext().setGridTheme(TA_GridThemes.NONE);
			System.out.println(table.render());
		});
		printTableService(servicesByType);

		int limitChosenService = BookingOrderService.getLimitChosenService(customer);
		boolean isClosed = false;
		do {
			ItemService chosenService = ItemServiceService.findServiceById(servicesByType,
					input.validate("Masukkan Service ID", "Service ID tidak ditemukan!",
							s -> ItemServiceService.findServiceById(servicesByType, s) != null));

			chosenServices.add(chosenService);
			if (chosenServices.size() == limitChosenService) {
				isClosed = true;
			} else {
				isClosed = !menu.confirmation("Ingin menambahkan Service lainnya?");
			}

		} while (isClosed != true);

		System.out.println("Metode Pembayaran");
		System.out.println("(1) Cash");
		if (Validation.isMember(customer))
			System.out.println("(2) Saldo Koin");
		int chosenPaymentMethod = Integer
				.valueOf(input.validate("Pilih Metode Pembayaran", "Hanya masukkan angka yang tersedia.",
						s -> menu.isNumber(s) && Integer.valueOf(s) == 1 || (Validation.isMember(customer)
								? Integer.valueOf(s) == 2
								: false)));

		String paymentMethod = BookingOrderService.getPaymentMethodById(chosenPaymentMethod);
		BookingOrder order = BookingOrderService.createBooking(bookingOrders, customer, chosenServices,
				paymentMethod);

		System.out.println("Booking Berhasil!");
		menu.createTable(table -> {
			table.addRow("Total Harga Service",
					": " + menu.rupiahFormatter((int) order.getTotalServicePrice()));
			table.addRow("Diskon", ": " + menu.rupiahFormatter((int) order.getDiscountCuts()));
			table.addRow("Total Pembayaran", ": " + menu.rupiahFormatter((int) order.getTotalPayment()));
			table.getContext().setGridTheme(TA_GridThemes.NONE);
			System.out.println(table.render());
		});

	}

	public static void showBookingOrder(List<BookingOrder> bookingOrders) {
		menu.printTitle("Booking Orders");
		printTableBookingOrder(bookingOrders);
	}

	public static void showTopupSaldo(Customer customer) {

		if (Validation.isMember(customer)) {
			menu.printTitle("Topup Saldo Koin");
			String value = input.validate("Masukkan nilai Topup", "Topup saldo mulai dari 1000",
					s -> menu.isNumber(s) && Integer.valueOf(s) >= 1000);

			// topup balance
			CustomerService.topupSaldo((MemberCustomer) customer, Double.valueOf(value));

			menu.resetDisplay();
			menu.printTitleCustom(
					"Topup saldo senilai " + menu.currencyFormatter(Integer.valueOf(value)) + " berhasil!", 2, 2);
			menu.enterToContinue();
		} else {
			menu.printTitleCustom("Daftar menjadi member untuk dapat menggunakan fitur ini", 2, 2);
			menu.enterToContinue();
		}
	}

	public static void printTableVehiclesByCustomer(Customer customer) {
		Object[] headers = { "No", "Vehicle ID", "Merk", "Warna", "Kapasitas Mesin", "Jumlah Pintu",
				"Transimisi", "Tipe", "Tahun" };
		List<Vehicle> vehicles = customer.getVehicles();

		menu.printTable(headers, table -> {
			int num = 1;
			for (Vehicle vehicle : vehicles) {
				table.addRow(num++, vehicle.getVehiclesId(), vehicle.getBrand(), vehicle.getColor(),
						vehicle.getVehicleType() == "Motorcycle" ? ((Motorcycle) vehicle).getEngineCapacity() + "cc"
								: "-",
						vehicle.getVehicleType() == "Car" ? ((Car) vehicle).getNumberOfDoor() : "-",
						vehicle.getTransmisionType(),
						vehicle.getVehicleType(),
						vehicle.getYearRelease());
				table.addRule();
			}
			table.setTextAlignment(TextAlignment.CENTER);
		});

	}

	public static void printTableBookingOrder(List<BookingOrder> bookingOrders) {
		Object[] headers = { "No", "Booking ID", "Nama Customer", "Metode Pembayaran", "Total Service", "Diskon",
				"Total Pembayaran", "List Service" };
		menu.printTable(headers, table -> {
			int num = 1;
			for (BookingOrder booking : bookingOrders) {
				table.addRow(num++, booking.getBookingId(), booking.getCustomer().getName(), booking.getPaymentMethod(),
						menu.rupiahFormatter((int) booking.getTotalServicePrice()),
						menu.rupiahFormatter((int) booking.getDiscountCuts()),
						menu.rupiahFormatter((int) booking.getTotalPayment()),
						printServices(booking.getServices()));
				table.addRule();
			}
			table.setTextAlignment(TextAlignment.CENTER);
		});
	}

	public static void printTableService(List<ItemService> services) {
		Object[] headers = { "No", "Service ID", "Nama Service", "Tipe Kendaraan", "Harga" };
		menu.printTable(headers, table -> {
			int num = 1;
			for (ItemService service : services) {
				table.addRow(num++, service.getServiceId(), service.getServiceName(), service.getVehicleType(),
						menu.rupiahFormatter((int) service.getPrice()));
				table.addRule();
			}
			table.setTextAlignment(TextAlignment.CENTER);
		});

	}

}
