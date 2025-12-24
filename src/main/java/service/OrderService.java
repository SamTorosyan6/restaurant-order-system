package service;

import db.DBConnectionProvider;
import model.Order;
import model.OrderItem;

import java.sql.*;
import java.util.List;

public class OrderService {

    private Connection connection = DBConnectionProvider.getInstance().getConnection();

    public void addOrder(Order order, List<OrderItem> orderItemList) {

        double total = 0;
        for (OrderItem item : orderItemList) {
            total += item.getPrice() * item.getQuantity();
        }
        order.setTotalPrice(total);

        String sql = "INSERT INTO order(customer_id, total_price, status) VALUES (?, ?, ?)";

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

    private void addOrderItems(int orderId, List<OrderItem> items) throws SQLException {
        String sql = "INSERT INTO order_item(order_id, dish_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (OrderItem item : items) {
                statement.setInt(1, orderId);
                statement.setInt(2, item.getDish().getId());
                statement.setInt(3, item.getQuantity());
                statement.setDouble(4, item.getPrice());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }
}

