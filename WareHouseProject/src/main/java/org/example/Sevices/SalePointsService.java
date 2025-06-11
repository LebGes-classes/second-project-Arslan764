package org.example.Sevices;

import org.example.Models.Person.Worker;
import org.example.Models.Product;
import org.example.Models.storage.Cell;
import org.example.Models.storage.SalePoint;
import org.example.Models.storage.Storage;
import org.example.dataBase.Repository.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class SalePointsService {
    private static Scanner scanner = new Scanner(System.in);

    static SalePointRepository salePointRepository = new SalePointRepository();
    static ProductRepository productRepository = new ProductRepository();
    static WorkerRepository workerRepository = new WorkerRepository();
    static CellRepository cellRepository = new CellRepository();

    public static void printAllSalePoints() throws SQLException {
        if (noSalePoints()) {
            System.out.println("Открытых пунктов продаж нет");
        }
        printSalePoints();

        System.out.println("Введите любой символ для выхода");
        String choice = scanner.nextLine();
        return;
    }

    public static void printSalePoints() throws SQLException {
        ArrayList<SalePoint> salePoints = salePointRepository.getAll();
        for (SalePoint salePoint : salePoints) {
            System.out.println(salePoint);
        }
    }

    public static void closeSalePoint() throws SQLException {
        System.out.println("Выберите пункт продаж для закрытия");
        if (noSalePoints()) {
            System.out.println("Открытых пунктов продаж нет");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        }
        printSalePoints();
        String choice = scanner.nextLine();
        int salePointId = Integer.parseInt(choice);

        Worker worker = workerRepository.getById(workerRepository.getId("Work_place_id = " + salePointId + "AND Status = 'SalePoint Admin'"));
        if (worker != null) {
            worker.setWorkPlaceId(0);
            worker.setStatus("SalePoint Staff");
            workerRepository.update(worker);
        }

        salePointRepository.delete(salePointId);
    }


    public static void openSalePoint() throws SQLException {
        SalePoint salePoint = new SalePoint();

        System.out.println("Введите адрес пункта продаж");
        String address = scanner.nextLine();
        salePoint.setStreet(address);

        salePointRepository.add(salePoint);
    }

    public static void productsInformation() throws SQLException {
        System.out.println("Выберите пункт продаж");
        if (noSalePoints()) {
            System.out.println("Открытых пунктов продаж нет");
        }
        printSalePoints();
        String choice = scanner.nextLine();
        int salePointId = Integer.parseInt(choice);

        ArrayList<Cell> cells = cellRepository.getAll("SalePoint_id = " + salePointId);
        ArrayList<Product> products = new ArrayList<>();
        for (Cell cell : cells) {
            Product product = productRepository.getById(cell.productId);
            products.add(product);
        }

        if(products.isEmpty()) {
            System.out.println("В пункте продаж отсутствуют товары");
        } else {
            for (Product product : products) {
                int amount = cellRepository.getAmountOfProductInSalePoint(product.id, salePointId);
                System.out.println(product + ", quantity: " + amount);
            }
        }
        System.out.println("Введите любой символ для выхода");
        String ch = scanner.nextLine();
        return;
    }

    public static void changeAdmin() throws SQLException {
        System.out.println("Выберите склад для смены ответственного лица");
        if (noSalePoints()) {
            System.out.println("Открытых складов нет");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        }
        printSalePoints();
        String choice = scanner.nextLine();
        int salePointId = Integer.parseInt(choice);
        SalePoint salePoint = salePointRepository.getById(salePointId);

        System.out.println("Выберите новое ответственное лицо");
        if(WorkerService.noWorkers("Status = 'SalePoint Staff'")){
            System.out.println("Подходящие работники отсутствуют");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        }
        WorkerService.printWorkers("Status = 'SalePoint Staff'");
        choice = scanner.nextLine();
        int newAdminId = Integer.parseInt(choice);

        Worker oldAdmin = workerRepository.getById(salePoint.adminId);
        if (oldAdmin != null) {
            oldAdmin.setStatus("SalePoint Staff");
            workerRepository.update(oldAdmin);
        }
        salePoint.setAdminId(newAdminId);
        Worker newAdmin = workerRepository.getById(newAdminId);
        newAdmin.setStatus("SalePoint Admin");
        workerRepository.update(newAdmin);

        salePointRepository.update(salePoint);
    }

    //Проверка на отсутствие хранилищ
    public static boolean noSalePoints() throws SQLException {
        ArrayList<SalePoint> salePoints = salePointRepository.getAll();
        boolean noSalePoints = salePoints.isEmpty();

        return noSalePoints;
    }
}
