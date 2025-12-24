package model;

import enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private int id;
    private Customer customer;
    private Date date;
    private double totalPrice;
    private Status status = Status.PENDING;

}
