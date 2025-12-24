package service;

import db.DBConnectionProvider;
import model.Dish;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DishService {

    private Connection connection = DBConnectionProvider.getInstance().getConnection();

    public void addDish(Dish dish) {

        String sql = "INSERT INTO dish(name, category, price, available) VALUES (?, ?, ?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, dish.getName());
            statement.setString(2, dish.getCategory().name().toUpperCase());
            statement.setString(3, String.valueOf(dish.getPrice()));
            statement.setString(4, String.valueOf(dish.isAvailable()));
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                dish.setId(resultSet.getInt(1));
            }
            System.out.println("Dish:" + dish.getName() + " added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDish(int id) {

        String sql = "DELETE FROM dish WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Dish with ID:" + "has been removed successfully");
    }

    public void updateDish(Dish dish) {

        String sql = "UPDATE dish SET name = ?, set category = ?, set price = ?, set available = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, dish.getName());
            statement.setString(2, String.valueOf(dish.getCategory()).toUpperCase());
            statement.setString(3, String.valueOf(dish.getPrice()));
            statement.setString(4, String.valueOf(dish.isAvailable()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
