package service;

import db.DBConnectionProvider;
import model.Dish;

import java.sql.Connection;

public class DishService {

    private Connection connection = DBConnectionProvider.getInstance().getConnection();

    public void addDish(Dish dish) {



    }
}
