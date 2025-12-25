package commands;

public interface Commands {

        String EXIT = "0";
        String ADD_DISH = "1";
        String DELETE_DISH = "2";
        String UPDATE_DISH = "3";
        String ADD_CUSTOMER = "4";
        String PRINT_ALL_CUSTOMERS = "5";
        String ADD_ORDER = "6";
        String ADD_DISHES_TO_ORDER = "7";
        String PRINT_ALL_ORDERS = "8";
        String PRINT_CUSTOMER_ORDER_HISTORY = "9";
        String PRINT_ORDER_DETAILS = "10";
        String CHANGE_ORDER_STATUS = "11";

        static void printCommands(){

            System.out.println("Please input " + EXIT + " for EXIT");
            System.out.println("Please input " + ADD_DISH + " for ADD DISH");
            System.out.println("Please input " + DELETE_DISH + " for DELETE DISH");
            System.out.println("Please input " + UPDATE_DISH + " for UPDATE DISH");
            System.out.println("Please input " + ADD_CUSTOMER + " for ADD CUSTOMER");
            System.out.println("Please input " + PRINT_ALL_CUSTOMERS + " for SEE ALL CUSTOMERS");
            System.out.println("Please input " + ADD_ORDER + " for ADD ORDER");
            System.out.println("Please input " + ADD_DISHES_TO_ORDER + " for ADD DISHES TO ORDER");
            System.out.println("Please input " + PRINT_ALL_ORDERS + " for SEE ALL ORDERS");
            System.out.println("Please input " + PRINT_CUSTOMER_ORDER_HISTORY + " for SEE CUSTOMER ORDER HISTORY");
            System.out.println("Please input " + PRINT_ORDER_DETAILS + " for SEE ORDER DETAILS");
            System.out.println("Please input " + CHANGE_ORDER_STATUS + " for CHANGE ORDER STATUS");
        }
}
