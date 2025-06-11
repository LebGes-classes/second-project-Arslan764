package org.example.Sevices;

import org.example.Models.Producer;
import org.example.Models.Product;
import org.example.Models.storage.Cell;
import org.example.dataBase.Repository.CellRepository;
import org.example.dataBase.Repository.ProducerRepository;
import org.example.dataBase.Repository.ProductRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class ProductService {
    private final static Scanner scanner = new Scanner(System.in);

    static ProducerRepository producerRepository = new ProducerRepository();
    static ProductRepository productRepository = new ProductRepository();
    static CellRepository cellRepository = new CellRepository();

    public static void addProduct() throws SQLException {
        Product product = new Product();

        System.out.println("Введите название продукта");
        String name = scanner.nextLine();

        System.out.println("Введите стоимость товара");
        double price = Double.parseDouble(scanner.nextLine());

        System.out.println("Введите продажную стоимость товара");
        double sellPrice = Double.parseDouble(scanner.nextLine());

        product.setName(name);
        product.setPrice(price);
        product.setSellPrice(sellPrice);

        System.out.println("Выберите склад для добавления товара");
        if(StoragesService.noStorages()){
            System.out.println("Склады отсутствуют");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        }
        StoragesService.printStorages();
        int storageId = Integer.parseInt(scanner.nextLine());

        Cell cell = new Cell(storageId);

        System.out.println("Введите количество товара");
        int amount = Integer.parseInt(scanner.nextLine());
        cell.setProductAmount(amount);
        product.setStatus("Available");

        Producer producer = new Producer();
        System.out.println("Введите название компании-производителя");
        String producerName = scanner.nextLine();
        producer.setName(producerName);
        producerRepository.add(producer);
        producer.setId(producerRepository.getId("Name = '" + producerName + "'"));

        productRepository.add(product);
        cell.setProductId(productRepository.getLastAddedId());
        cellRepository.addToStorage(cell);

    }
    public static void printAvailableProducts() throws SQLException {
        String condition = "Status = 'Available'";
        if(noProducts(condition)){
            System.out.println("Доступные для закупки товары отсутствуют");
        }
        printProducts(condition);

        System.out.println("Введите любой символ для выхода");
        String ch =scanner.nextLine();
    }

    public static void buyProduct() throws SQLException {
        System.out.println("Выберите товар для закупки");
        if(noProducts()){
            System.out.println("Подходящие товары отсутствуют");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        }
        printProducts();
        int productId = Integer.parseInt(scanner.nextLine());
        Product product = productRepository.getById(productId);

        System.out.println("Выберите склад на который необходимо доставить товар");
        if(StoragesService.noStorages()){
            System.out.println("Склады отсутствуют");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        }
        StoragesService.printStorages();
        int storageId = Integer.parseInt(scanner.nextLine());

        System.out.println("Введите количество товара");
        int amount = Integer.parseInt(scanner.nextLine());

        Cell cell = cellRepository.getById(cellRepository.getId("Storage_id = " + storageId + " AND Product_id = " + product.id));
        if(cell != null){
            cell.setProductAmount(amount + cell.productAmount);
            cellRepository.update(cell);
        } else{
            cell =new Cell(storageId);
            cell.setProductAmount(amount);
            cell.setProductId(productId);
            cellRepository.addToStorage(cell);
        }
    }

    public static void moveFromStorageToSalePoint() throws SQLException {
        System.out.println("Выберите склад");
        if (StoragesService.noStorages()) {
            System.out.println("Склады отсутствуют");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        }
        StoragesService.printStorages();
        int storageId = Integer.parseInt(scanner.nextLine());

        System.out.println("Выберите товар для перемещения в точку продаж");
        ArrayList<Cell> cells = cellRepository.getAll("Storage_id = " + storageId);
        ArrayList<Product> products = new ArrayList<>();
        for(Cell cell : cells){
            Product product = productRepository.getById(cell.productId);
            products.add(product);
        }
        if(products.isEmpty()){
            System.out.println("Нет доступных товаров");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        }
        else{
            for(Product product : products){
                System.out.println(product);
            }
        }
        int productId = Integer.parseInt(scanner.nextLine());
        Product product = productRepository.getById(productId);

        Cell oldCell = cellRepository.getById(cellRepository.getId("Product_id = " + product.id + " AND Storage_id = " + storageId));
        System.out.println("Доступно: " + oldCell.productAmount);

        System.out.println("Введите количество товара для перемещения");
        int amount = Integer.parseInt(scanner.nextLine());
        while(amount > oldCell.productAmount){
            System.out.println("Не валидное количество, попробуйте еще раз");
            amount = Integer.parseInt(scanner.nextLine());
        }
        oldCell.setProductAmount(oldCell.productAmount - amount);
        if(oldCell.productAmount == 0){
            cellRepository.delete(oldCell.id);
        }

        cellRepository.update(oldCell);

        System.out.println("Выберите пункт продаж");
        if(SalePointsService.noSalePoints()){
            System.out.println("Пункты продаж отсутствуют");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        }
        SalePointsService.printSalePoints();
        int salePointId = Integer.parseInt(scanner.nextLine());

        Cell newCell =cellRepository.getById(cellRepository.getId("SalePoint_id = " + salePointId + " AND Product_id = " + product.id ));
        if(newCell != null){
            newCell.setProductAmount(newCell.productAmount + amount);
            cellRepository.update(newCell);
        } else {
            newCell = new Cell(salePointId);
            newCell.setProductAmount(amount);
            newCell.setProductId(product.id);
            cellRepository.addToSalePoint(newCell);
        }
    }

    public static void moveFromSalePointToStorage() throws SQLException {
        System.out.println("Выберите пункт продаж");
        if(SalePointsService.noSalePoints()){
            System.out.println("Пункты продаж отсутствуют");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        }
        SalePointsService.printSalePoints();
        int salePointId = Integer.parseInt(scanner.nextLine());

        System.out.println("Выберите товар для перемещения на склад");
        ArrayList<Cell> cells = cellRepository.getAll("SalePoint_id = " + salePointId);
        ArrayList<Product> products = new ArrayList<>();
        for(Cell cell : cells){
            Product product = productRepository.getById(cell.productId);
            products.add(product);
        }
        if(products.isEmpty()){
            System.out.println("Нет доступных товаров");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        }
        else{
            for(Product product : products){
                System.out.println(product);
            }
        }
        int productId = Integer.parseInt(scanner.nextLine());
        Product product = productRepository.getById(productId);

        Cell oldCell = cellRepository.getById(cellRepository.getId("Product_id = " + product.id + " AND SalePoint_id = " + salePointId));
        System.out.println("Доступно: " + oldCell.productAmount);

        System.out.println("Введите количество товара для перемещения");
        int amount = Integer.parseInt(scanner.nextLine());
        while(amount > oldCell.productAmount){
            System.out.println("Не валидное количество, попробуйте еще раз");
            amount = Integer.parseInt(scanner.nextLine());
        }
        oldCell.setProductAmount(oldCell.productAmount - amount);
        if(oldCell.productAmount == 0){
            cellRepository.delete(oldCell.id);
        }

        cellRepository.update(oldCell);

        System.out.println("Выберите склад");
        if (StoragesService.noStorages()) {
            System.out.println("Склады отсутствуют");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        }
        StoragesService.printStorages();
        int storageId = Integer.parseInt(scanner.nextLine());

        Cell newCell =cellRepository.getById(cellRepository.getId("Storage_id = " + storageId + " AND Product_id = " + product.id ));
        if(newCell != null){
            newCell.setProductAmount(newCell.productAmount + amount);
            cellRepository.update(newCell);
        } else {
            newCell = new Cell(storageId);
            newCell.setProductAmount(amount);
            newCell.setProductId(product.id);
            cellRepository.addToStorage(newCell);
        }

    }

    public static void moveFromStorageToStorage() throws SQLException {
        System.out.println("Выберите склад");
        if (StoragesService.noStorages()) {
            System.out.println("Склады отсутствуют");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        }
        StoragesService.printStorages();
        int oldStorageId = Integer.parseInt(scanner.nextLine());

        System.out.println("Выберите товар для перемещения на склад");
        ArrayList<Cell> cells = cellRepository.getAll("Storage_id = " + oldStorageId);
        ArrayList<Product> products = new ArrayList<>();
        for(Cell cell : cells){
            Product product = productRepository.getById(cell.productId);
            products.add(product);
        }
        if(products.isEmpty()){
            System.out.println("Нет доступных товаров");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        }
        else{
            for(Product product : products){
                System.out.println(product);
            }
        }
        int productId = Integer.parseInt(scanner.nextLine());
        Product product = productRepository.getById(productId);

        Cell oldCell = cellRepository.getById(cellRepository.getId("Product_id = " + product.id + " AND Storage_id = " + oldStorageId));
        System.out.println("Доступно: " + oldCell.productAmount);

        System.out.println("Введите количество товара для перемещения");
        int amount = Integer.parseInt(scanner.nextLine());
        while(amount > oldCell.productAmount){
            System.out.println("Не валидное количество, попробуйте еще раз");
            amount = Integer.parseInt(scanner.nextLine());
        }
        oldCell.setProductAmount(oldCell.productAmount - amount);
        if(oldCell.productAmount == 0){
            cellRepository.delete(oldCell.id);
        }

        cellRepository.update(oldCell);

        System.out.println("Выберите склад");
        if (StoragesService.noStorages()) {
            System.out.println("Склады отсутствуют");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        }
        StoragesService.printStorages();
        int newStorageId = Integer.parseInt(scanner.nextLine());

        Cell newCell =cellRepository.getById(cellRepository.getId("Storage_id = " + newStorageId + " AND Product_id = " + product.id ));
        if(newCell != null){
            newCell.setProductAmount(newCell.productAmount + amount);
            cellRepository.update(newCell);
        } else {
            newCell = new Cell(newStorageId);
            newCell.setProductAmount(amount);
            newCell.setProductId(product.id);
            cellRepository.addToStorage(newCell);
        }
    }

    public static void moveFromSalePointToSalePoint() throws SQLException {
        System.out.println("Выберите точку продаж");
        if (SalePointsService.noSalePoints()) {
            System.out.println("Точки продаж отсутствуют");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        }
        SalePointsService.printSalePoints();
        int oldSalePointId = Integer.parseInt(scanner.nextLine());

        System.out.println("Выберите товар для перемещения на склад");
        ArrayList<Cell> cells = cellRepository.getAll("SalePoint_id = " + oldSalePointId);
        ArrayList<Product> products = new ArrayList<>();
        for(Cell cell : cells){
            Product product = productRepository.getById(cell.productId);
            products.add(product);
        }
        if(products.isEmpty()){
            System.out.println("Нет доступных товаров");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        }
        else{
            for(Product product : products){
                System.out.println(product);
            }
        }
        int productId = Integer.parseInt(scanner.nextLine());
        Product product = productRepository.getById(productId);

        Cell oldCell = cellRepository.getById(cellRepository.getId("Product_id = " + product.id + " AND SalePoint_id = " + oldSalePointId));
        System.out.println("Доступно: " + oldCell.productAmount);

        System.out.println("Введите количество товара для перемещения");
        int amount = Integer.parseInt(scanner.nextLine());
        while(amount > oldCell.productAmount){
            System.out.println("Не валидное количество, попробуйте еще раз");
            amount = Integer.parseInt(scanner.nextLine());
        }
        oldCell.setProductAmount(oldCell.productAmount - amount);
        if(oldCell.productAmount == 0){
            cellRepository.delete(oldCell.id);
        }

        cellRepository.update(oldCell);

        System.out.println("Выберите точку продаж");
        if (SalePointsService.noSalePoints()) {
            System.out.println("Точки продаж отсутствуют");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        }
        SalePointsService.printSalePoints();
        int newSalePointId = Integer.parseInt(scanner.nextLine());

        Cell newCell =cellRepository.getById(cellRepository.getId("Storage_id = " + newSalePointId + " AND Product_id = " + product.id ));
        if(newCell != null){
            newCell.setProductAmount(newCell.productAmount + amount);
            cellRepository.update(newCell);
        } else {
            newCell = new Cell(newSalePointId);
            newCell.setProductAmount(amount);
            newCell.setProductId(product.id);
            cellRepository.addToSalePoint(newCell);
        }
    }

    public static boolean noProducts() throws SQLException {
        ArrayList<Product> products = productRepository.getAll();
        return products.isEmpty();
    }

    public static boolean noProducts(String condition) throws SQLException {
        ArrayList<Product> products = productRepository.getAll(condition);
        return products.isEmpty();
    }

    public static void printProducts() throws SQLException {
        ArrayList<Product> products = productRepository.getAll();
        for (Product product : products) {
            int amount = cellRepository.getTotalAmountOfProduct(product.id);
            System.out.println("Товар: " + product + ", количество:" + amount);
        }
    }

    public static void printProducts(String condition) throws SQLException {
        ArrayList<Product> products = productRepository.getAll(condition);
        for(Product product : products){
            int amount = cellRepository.getTotalAmountOfProduct(product.id);
            System.out.println("Товар: " + product + ", количество:" + amount);
        }

    }}
