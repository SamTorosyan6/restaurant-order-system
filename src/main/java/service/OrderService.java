package service;

import db.DBConnectionProvider;
import enums.Status;
import model.Customer;
import model.Order;
import model.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private Connection connection = DBConnectionProvider.getInstance().getConnection();
    private CustomerService customerService = new CustomerService();

    public void addOrder(Order order, List<OrderItem> orderItemList) {

        double total = 0;
        for (OrderItem item : orderItemList) {
            total += item.getPrice() * item.getQuantity();
        }
        order.setTotalPrice(total);

        String sql = "INSERT INTO `order`(customer_id, total_price, status) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, order.getCustomer().getId());
            statement.setDouble(2, order.getTotalPrice());
            statement.setString(3, order.getStatus().name().toUpperCase());
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                int generatedOrderId = resultSet.getInt(1);
                order.setId(generatedOrderId);

                String sqlOrder = "INSERT INTO order_item(order_id, dish_id, quantity, price) VALUES (?, ?, ?, ?)";

                for (OrderItem item : orderItemList) {
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sqlOrder)) {
                        preparedStatement.setInt(1, generatedOrderId);
                        preparedStatement.setInt(2, item.getDish().getId());
                        preparedStatement.setInt(3, item.getQuantity());
                        preparedStatement.setDouble(4, item.getPrice());
                        preparedStatement.executeUpdate();
                    }
                }
            }

            System.out.println("Order added successfully with ID: " + order.getId());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addDishToOrder(int orderId, OrderItem item) {
        String sqlItem = "INSERT INTO order_item(order_id, dish_id, quantity, price) VALUES (?, ?, ?, ?)";
        String sqlUpdateTotal = "UPDATE `order` SET total_price = total_price + ? WHERE id = ?";

        try {
            try (PreparedStatement statement = connection.prepareStatement(sqlItem)) {
                statement.setInt(1, orderId);
                statement.setInt(2, item.getDish().getId());
                statement.setInt(3, item.getQuantity());
                statement.setDouble(4, item.getPrice());
                statement.executeUpdate();
            }
            try (PreparedStatement statement = connection.prepareStatement(sqlUpdateTotal)) {
                double itemTotal = item.getPrice() * item.getQuantity();
                statement.setDouble(1, itemTotal);
                statement.setInt(2, orderId);
                statement.executeUpdate();
            }

            System.out.println("Dish added to order successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Order> getAllOrders() {
        String sql = "SELECT * FROM `order`";
        List<Order> orders = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Order order = new Order();

                order.setId(resultSet.getInt("id"));

                int customerId = resultSet.getInt("customer_id");
                Customer customer = customerService.getCustomerById(customerId);
                order.setCustomer(customer);

                order.setDate(resultSet.getTimestamp("order_date"));
                order.setTotalPrice(resultSet.getDouble("total_price"));
                order.setStatus(Status.valueOf(resultSet.getString("status")));

                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public void updateOrderStatus(int orderId, Status newStatus) {
        String sql = "UPDATE `order` SET status = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newStatus.name());
            statement.setInt(2, orderId);
            statement.executeUpdate();
            System.out.println("Order status updated to: " + newStatus);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Order> getOrdersByCustomerId(int customerId) {
        String sql = "SELECT * FROM `order` WHERE customer_id = ?";
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getInt("id"));
                order.setCustomer(customerService.getCustomerById(customerId));
                order.setDate(resultSet.getTimestamp("order_date"));
                order.setTotalPrice(resultSet.getDouble("total_price"));
                order.setStatus(Status.valueOf(resultSet.getString("status")));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

}

