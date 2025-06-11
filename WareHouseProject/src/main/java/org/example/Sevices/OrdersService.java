package org.example.Sevices;

import org.example.Models.Order;
import org.example.Models.Person.Buyer;
import org.example.Models.Product;
import org.example.Models.storage.Cell;
import org.example.Models.storage.SalePoint;
import org.example.dataBase.Repository.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class OrdersService {

    private static final Scanner scanner = new Scanner(System.in);

    static SalePointRepository salePointRepository = new SalePointRepository();
    static OrderRepository orderRepository = new OrderRepository();
    static ProductRepository productRepository = new ProductRepository();
    static BuyerRepository buyerRepository = new BuyerRepository();
    static CellRepository cellRepository = new CellRepository();

    public static void makeOrder() throws SQLException {
        Order order = new Order();
        System.out.println("Выберите точку продаж");
        if(SalePointsService.noSalePoints()){
            System.out.println("Точки продаж отсутствуют");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        }
        SalePointsService.printSalePoints();

        int salePointId = Integer.parseInt(scanner.nextLine());
        SalePoint salePoint = salePointRepository.getById(salePointId);
        order.setSalePointId(salePointId);

        System.out.println("Выберите товар для продажи");
        ArrayList<Cell> cells =cellRepository.getAll("SalePoint_id = " + salePointId);
        ArrayList<Product> products = new ArrayList<>();
        for (Cell cell : cells) {
            Product product = productRepository.getById(cell.productId);
            if(product.status.equals("Available")){
                products.add(product);
            }
        }

        if(products.isEmpty()){
            System.out.println("Нет доступных продуктов");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        } else{
            for(Product product : products){
                int amount = cellRepository.getAmountOfProductInSalePoint(product.id, salePointId);
                System.out.println(product + ", количество: " + amount);
            }
        }
        int productId = Integer.parseInt(scanner.nextLine());
        Product product = productRepository.getById(productId);
        order.setProductId(productId);

        Cell cell = cellRepository.getById(cellRepository.getId("Product_id = " + productId + " AND SalePoint_id = " + salePointId));
        System.out.println("Доступно " + cell.productAmount);
        System.out.println("Введите количество");
        int amount = Integer.parseInt(scanner.nextLine());
        while(amount > cell.productAmount){
            System.out.println("Невалидное количество, попробуйте еще раз");
            amount = Integer.parseInt(scanner.nextLine());
        }
        order.setAmount(amount);
        cell.setProductAmount(cell.productAmount - amount);
        if(cell.productAmount == 0){
            cellRepository.delete(cell.id);
        }
        cellRepository.update(cell);

        double total = product.sellPrice * amount;
        order.setTotalPrice(total);
        salePoint.increaseRevenue(total);

        Buyer buyer = new Buyer();
        System.out.println("Введите имя покупателя");
        String firstName = scanner.nextLine();
        System.out.println("Введите фамилию покупателя");
        String lastName = scanner.nextLine();
        System.out.println("Введите телефон покупателя");
        String phoneNumber = scanner.nextLine();

        buyer.setFirstName(firstName);
        buyer.setLastName(lastName);
        buyer.setPhoneNumber(phoneNumber);
        buyerRepository.add(buyer);
        buyer.setId(buyerRepository.getId("First_name = '" + buyer.firstName + "' AND Last_name = '" + buyer.lastName +"'"));
        order.setBuyerId(buyer.id);

        order.setDate(LocalDateTime.now());
        order.setStatus("Received");
        orderRepository.add(order);

        productRepository.update(product);
        salePointRepository.update(salePoint);
    }

    public static void makeRefund() throws SQLException {
        System.out.println("Выберите заказ для оформления возврата");
        if(noOrders("Status = 'Received'")){
            System.out.println("Нет подходящих заказов");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        }
        printOrders("Status = 'Received'");

        int orderId = Integer.parseInt(scanner.nextLine());
        Order order = orderRepository.getById(orderId);
        Product product = productRepository.getById(order.productId);
        product.setStatus("Available");

        SalePoint salePoint = salePointRepository.getById(order.salePointId);
        salePoint.reduceRevenue(order.totalPrice);

        Cell newCell = cellRepository.getById(cellRepository.getId("SalePoint_id = " + order.salePointId + " AND Product_id = " + product.id));
        if (newCell != null) {
            newCell.setProductAmount(newCell.productAmount + order.amount);
            cellRepository.update(newCell);
        } else{
            newCell = new Cell(order.salePointId);
            newCell.setProductAmount(order.amount);
            newCell.setProductId(product.id);
            cellRepository.addToSalePoint(newCell);
        }
        order.setStatus("Refunded");
        orderRepository.update(order);

        productRepository.update(product);
        salePointRepository.update(salePoint);
    }

    public static void printOrdersByProduct() throws SQLException {
        System.out.println("Выберите товар");
        ArrayList<Product> products = productRepository.getAll();
        if(products.isEmpty()) {
            System.out.println("Доступные продукты отсутствуют");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        } else{
            for(Product product : products){
                System.out.println(product);
            }
        }
        int productId = Integer.parseInt(scanner.nextLine());

        ArrayList<Order> orders = orderRepository.getOrdersByProduct(productId);
        if(orders.isEmpty()){
            System.out.println("Подходящие заказы отсутствуют");
        } else{
            for(Order order : orders){
                System.out.println(order);
            }
        }

        System.out.println("Введите любой символ для выхода");
        String ch = scanner.nextLine();
    }

    public static void printOrdersByBuyer() throws SQLException {
        System.out.println("Выберите покупателя");
        ArrayList<Buyer> buyers = buyerRepository.getAll();
        if(buyers.isEmpty()) {
            System.out.println("Покупатели отсутствуют");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        } else{
            for(Buyer buyer : buyers){
                System.out.println(buyer);
            }
        }
        int buyerId = Integer.parseInt(scanner.nextLine());

        ArrayList<Order> orders = orderRepository.getOrdersByBuyer(buyerId);
        if(orders.isEmpty()) {
            System.out.println("Подходящие заказы отсутствуют");
        } else{
            for(Order order : orders){
                System.out.println(order);
            }
        }

        System.out.println("Введите любой символ для выхода");
        String ch = scanner.nextLine();
    }


    public static boolean noOrders(String condition) throws SQLException{
        ArrayList<Order> orders = orderRepository.getAll(condition);
        return orders.isEmpty();
    }


    public static void printOrders(String condition) throws SQLException{
        ArrayList<Order> orders = orderRepository.getAll(condition);
        for(Order order : orders){
            System.out.println(order);
        }
    }
}
