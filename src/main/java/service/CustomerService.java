package service;

import db.DBConnectionProvider;
import model.Customer;

import java.sql.Connection;

public class CustomerService {

    private Connection connection = DBConnectionProvider.getInstance().getConnection();

    public void addCustomer(Customer customer) {



    }

}
