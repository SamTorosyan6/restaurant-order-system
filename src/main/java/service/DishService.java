package service;

import db.DBConnectionProvider;
import enums.Category;
import model.Dish;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DishService {

    private Connection connection = DBConnectionProvider.getInstance().getConnection();

    public void addDish(Dish dish) {

        String sql = "INSERT INTO dish(name, category, price, available) VALUES (?, ?, ?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, dish.getName());
            statement.setString(2, dish.getCategory().name().toUpperCase());
            statement.setDouble(3, dish.getPrice());
            statement.setBoolean(4, dish.isAvailable());
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
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Dish with ID:" + id + " has been removed successfully");
    }

    public void updateDish(Dish dish) {

        String sql = "UPDATE dish SET name = ?, category = ?, price = ?, available = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, dish.getName());
            statement.setString(2, String.valueOf(dish.getCategory()).toUpperCase());
            statement.setString(3, String.valueOf(dish.getPrice()));
            statement.setBoolean(4, dish.isAvailable());
            statement.setInt(5, dish.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Dish: " + dish.getName() + " was successfully changed!");
    }

    public List<Dish> getAllDishes() {
        String sql = "SELECT * FROM dish";
        List<Dish> dishes = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Dish dish = new Dish();
                dish.setId(resultSet.getInt("id"));
                dish.setName(resultSet.getString("name"));
                dish.setCategory(Category.valueOf(resultSet.getString("category")));
                dish.setPrice(resultSet.getDouble("price"));
                dish.setAvailable(resultSet.getBoolean("available"));
                dishes.add(dish);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dishes;
    }

    public Dish getDishById(int id) {
        String sql = "SELECT * FROM dish WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Dish dish = new Dish();
                dish.setId(resultSet.getInt("id"));
                dish.setName(resultSet.getString("name"));
                dish.setCategory(Category.valueOf(resultSet.getString("category")));
                dish.setPrice(resultSet.getDouble("price"));
                return dish;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
