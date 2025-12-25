package service;

import db.DBConnectionProvider;
import model.OrderItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemService {
    private Connection connection = DBConnectionProvider.getInstance().getConnection();
    private DishService dishService = new DishService();

    public List<OrderItem> getItemsByOrderId(int orderId) {
        String sql = "SELECT * FROM order_item WHERE order_id = ?";
        List<OrderItem> items = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                OrderItem item = new OrderItem();
                item.setId(resultSet.getInt("id"));
                item.setQuantity(resultSet.getInt("quantity"));
                item.setPrice(resultSet.getDouble("price"));
                item.setDish(dishService.getDishById(resultSet.getInt("dish_id")));
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
}