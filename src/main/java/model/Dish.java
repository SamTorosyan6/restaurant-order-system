package model;

import enums.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dish {

    private int id;
    private String name;
    private Category category;
    private double price;
    private boolean available;

}
