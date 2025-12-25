import commands.Commands;
import enums.Category;
import enums.Status;
import model.Customer;
import model.Dish;
import model.Order;
import model.OrderItem;
import service.CustomerService;
import service.DishService;
import service.OrderItemService;
import service.OrderService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class RestaurantDemo implements Commands {

    private static Scanner scanner = new Scanner(System.in);
    private static CustomerService customerService = new CustomerService();
    private static DishService dishService = new DishService();
    private static OrderItemService orderItemService = new OrderItemService();
    private static OrderService orderService = new OrderService();

    public static void main(String[] args) {

        boolean isRun = true;
        while (isRun) {
            Commands.printCommands();
            String command = scanner.nextLine();
            switch (command) {
                case EXIT:
                    isRun = false;
                    break;
                case ADD_DISH:
                    addDish();
                    break;
                case DELETE_DISH:
                    deleteDish();
                    break;
                case UPDATE_DISH:
                    updateDish();
                    break;
                case ADD_CUSTOMER:
                    addCustomer();
                    break;
                case PRINT_ALL_CUSTOMERS:
                    printCustomers();
                    break;
                case ADD_ORDER:
                    addOrder();
                    break;
                case ADD_DISHES_TO_ORDER:
                    addDishesToOrder();
                    break;
                case PRINT_ALL_ORDERS:
                    printAllOrders();
                    break;
                case PRINT_CUSTOMER_ORDER_HISTORY:
                    printCustomerOrderHistory();
                    break;
                case PRINT_ORDER_DETAILS:
                    printOrderDetails();
                    break;
                case CHANGE_ORDER_STATUS:
                    changeOrderStatus();
                    break;
                default:
                    System.err.println("Invalid command!");

            }

        }
    }

    private static void addDishesToOrder() {

        printAllOrders();
        System.out.println("Please enter Order ID:");
        int orderId = Integer.parseInt(scanner.nextLine());

        getDishes();
        System.out.println("Please enter Dish ID to add:");
        int dishId = Integer.parseInt(scanner.nextLine());
        Dish dish = dishService.getDishById(dishId);

        if (dish == null) {
            System.out.println("Dish not found!");
            return;
        }

        System.out.println("Please enter quantity:");
        int quantity = Integer.parseInt(scanner.nextLine());

        OrderItem orderItem = new OrderItem();
        orderItem.setDish(dish);
        orderItem.setQuantity(quantity);
        orderItem.setPrice(dish.getPrice());

        orderService.addDishToOrder(orderId, orderItem);
    }

    private static void changeOrderStatus() {
        System.out.println("All Orders:");
        List<Order> orders = orderService.getAllOrders();
        for (Order order : orders) {
            System.out.println("ID: " + order.getId() + " | Current Status: " + order.getStatus());
        }

        System.out.println("Please enter Order ID to change status:");
        int orderId = Integer.parseInt(scanner.nextLine());

        System.out.println("Choose new status:");
        Status[] statuses = Status.values();
        for (int i = 0; i < statuses.length; i++) {
            System.out.println(i + ". " + statuses[i]);
        }

        int statusIndex = Integer.parseInt(scanner.nextLine());
        if (statusIndex >= 0 && statusIndex < statuses.length) {
            orderService.updateOrderStatus(orderId, statuses[statusIndex]);
        } else {
            System.out.println("Invalid status selection!");
        }
    }

    private static void printCustomerOrderHistory() {
        printCustomers();
        System.out.println("Please enter customer ID to see history:");
        try {
            int customerId = Integer.parseInt(scanner.nextLine());
            Customer customer = customerService.getCustomerById(customerId);
            if (customer == null) {
                System.out.println("Customer not found!");
                return;
            }

            List<Order> orders = orderService.getOrdersByCustomerId(customerId);
            if (orders.isEmpty()) {
                System.out.println("No orders found for this customer.");
            } else {
                for (Order order : orders) {
                    System.out.println(order);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    private static void printOrderDetails() {
        System.out.println("All Orders:");
        List<Order> orders = orderService.getAllOrders();
        for (Order order : orders) {
            System.out.println(order);
        }

        System.out.println("Please enter Order ID to see details:");
        try {
            int orderId = Integer.parseInt(scanner.nextLine());
            List<OrderItem> items = orderItemService.getItemsByOrderId(orderId);

            if (items.isEmpty()) {
                System.out.println("No items found for this order.");
            } else {
                System.out.println("Order Items for ID: " + orderId);
                for (OrderItem item : items) {
                    double itemTotal = item.getQuantity() * item.getPrice();

                    System.out.println("Dish: " + item.getDish().getName() +
                            " | Quantity: " + item.getQuantity() +
                            " | Price: " + item.getPrice() +
                            " | Total: " + itemTotal);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    private static void printAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        for (Order order : orders) {
            System.out.println(order);
        }
    }

    private static void addOrder() {

        printCustomers();
        System.out.println("Please enter customer ID");
        String id = scanner.nextLine();
        Customer customer = customerService.getCustomerById(Integer.parseInt(id));
        if (customer == null) {
            System.out.println("Customer not found");
            return;
        }
        Order order = new Order();
        order.setCustomer(customer);
        order.setDate(new Date());
        order.setTotalPrice(0);
        order.setStatus(Status.PENDING);
        List<OrderItem> orderItems = new ArrayList<>();

        boolean addMore = true;
        while (addMore) {

            dishService.getAllDishes();
            System.out.println("Please enter dish ID:");
            int dishId = Integer.parseInt(scanner.nextLine());

            Dish dish = dishService.getDishById(dishId);
            if (dish == null) {
                System.out.println("Dish not found");
                continue;
            }

            System.out.println("Please enter quantity:");
            int quantity = Integer.parseInt(scanner.nextLine());

            OrderItem orderItem = new OrderItem();
            orderItem.setDish(dish);
            orderItem.setQuantity(quantity);
            orderItem.setPrice(dish.getPrice());

            orderItems.add(orderItem);

            System.out.println("Add more dishes? (yes/no)");
            String answer = scanner.nextLine();
            if (!answer.equalsIgnoreCase("yes")) {
                addMore = false;
            }
        }

        if (orderItems.isEmpty()) {
            System.out.println("Order must contain at least one dish");
            return;
        }
        orderService.addOrder(order, orderItems);
    }

    private static void printCustomers() {

        List<Customer> allCustomers = customerService.getAllCustomers();

        for (Customer customer : allCustomers) {
            System.out.println(customer);
        }

    }

    private static void addCustomer() {

        System.out.println("Please input customer's name");
        String name = scanner.nextLine();
        System.out.println("Please input customer's phone");
        String phone = scanner.nextLine();
        System.out.println("Please input customer's email");
        String email = scanner.nextLine();
        Customer customer = new Customer();
        customer.setName(name);
        customer.setPhone(phone);
        customer.setEmail(email);
        customerService.addCustomer(customer);

    }

    private static void updateDish() {

        getDishes();
        System.out.println("Please input the dish ID would you like to change");
        String dishId = scanner.nextLine();
        Dish dishById = dishService.getDishById(Integer.parseInt(dishId));
        if (dishById == null) {
            System.out.println("Dish with ID " + dishId + " not found!");
            return;
        }
        System.out.println("Please input the new name for update dish's name");
        String newName = scanner.nextLine();
        dishById.setName(newName);
        System.out.println("Please input the new category for update dish's category");
        Category[] categories = Category.values();
        for (Category category : categories) {
            System.out.print(category + " ");
        }
        String newCategory = scanner.nextLine();
        dishById.setCategory(Category.valueOf(newCategory.toUpperCase()));
        System.out.println("Please input the new price for update dish's price");
        String price = scanner.nextLine();
        dishById.setPrice(Double.parseDouble(price));
        System.out.println("Please input the new availability for update dish's availability (True or False)");
        String available = scanner.nextLine();
        dishById.setAvailable(Boolean.parseBoolean(available));
        dishService.updateDish(dishById);

    }

    private static void deleteDish() {

        getDishes();
        System.out.println("Please input the dish ID would you like to remove");
        String dishId = scanner.nextLine();
        Dish dishById = dishService.getDishById(Integer.parseInt(dishId));
        if (dishById == null) {
            System.out.println("Dish not found");
            return;
        }
        dishService.deleteDish(Integer.parseInt(dishId));
    }

    private static void getDishes() {

        List<Dish> dishes = dishService.getAllDishes();
        for (Dish dish : dishes) {
            System.out.println(dish);
        }
    }

    private static void addDish() {
        System.out.println("Please input name");
        Dish dish = new Dish();
        String name = scanner.nextLine();
        dish.setName(name);
        System.out.println("Please input category");
        Category[] categories = Category.values();
        for (Category category : categories) {
            System.out.print(category + " ");
        }
        String category = scanner.nextLine();
        dish.setCategory(Category.valueOf(category.toUpperCase()));
        System.out.println("Please input price");
        String price = scanner.nextLine();
        dish.setPrice(Double.parseDouble(price));
        System.out.println("Please input availability (True or False)");
        String available = scanner.nextLine();
        dish.setAvailable(Boolean.parseBoolean(available));
        dishService.addDish(dish);
    }
}