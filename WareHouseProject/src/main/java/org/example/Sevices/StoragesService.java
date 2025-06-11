package org.example.Sevices;

import org.example.Models.Person.Worker;
import org.example.Models.Product;
import org.example.Models.storage.Cell;
import org.example.Models.storage.Storage;
import org.example.dataBase.Repository.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class StoragesService {
    private static Scanner scanner = new Scanner(System.in);


    static StorageRepository storageRepository = new StorageRepository();
    static ProductRepository productRepository = new ProductRepository();
    static WorkerRepository workerRepository = new WorkerRepository();
    static CellRepository cellRepository = new CellRepository();

    public static void printAllStorages() throws SQLException {
        if (noStorages()) {
            System.out.println("Открытых складов нет");
        }
        printStorages();

        System.out.println("Введите любой символ для выхода");
        String choice = scanner.nextLine();
        return;
    }

    public static void printStorages() throws SQLException {
        ArrayList<Storage> storages = storageRepository.getAll();
        for (Storage storage : storages) {
            System.out.println(storage);
        }
    }

    public static void closeStorage() throws SQLException {
        System.out.println("Выберите склад для закрытия");
        if (noStorages()) {
            System.out.println("Открытых складов нет");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        }
        printStorages();
        String choice = scanner.nextLine();
        int storageId = Integer.parseInt(choice);

        Worker worker = workerRepository.getById(workerRepository.getId("Work_place_id = " + storageId + "AND Status = 'Storage Admin'"));
        if (worker != null) {
            worker.setWorkPlaceId(0);
            worker.setStatus("Storage Staff");
            workerRepository.update(worker);
        }

        storageRepository.delete(storageId);
    }


    public static void openStorage() throws SQLException {
        Storage storage = new Storage();

        System.out.println("Введите адрес склада");
        String address = scanner.nextLine();
        storage.setStreet(address);

        storageRepository.add(storage);
    }

    public static void productsInformation() throws SQLException {
        System.out.println("Выберите склад");
        if (noStorages()) {
            System.out.println("Открытых складов нет");
        }
        printStorages();
        String choice = scanner.nextLine();
        int storageId = Integer.parseInt(choice);

        ArrayList<Cell> cells = cellRepository.getAll("Storage_id = " + storageId);
        ArrayList<Product> products = new ArrayList<>();
        for (Cell cell : cells) {
            Product product = productRepository.getById(cell.productId);
            products.add(product);
        }

        if(products.isEmpty()) {
            System.out.println("На складе отсутствуют товары");
        } else {
            for (Product product : products) {
                int amount = cellRepository.getAmountOfProductInStorage(product.id, storageId);
                System.out.println(product + ", quantity: " + amount);
            }
        }
        System.out.println("Введите любой символ для выхода");
        String ch = scanner.nextLine();
        return;
    }

    public static void changeAdmin() throws SQLException {
        System.out.println("Выберите склад для смены ответственного лица");
        if (noStorages()) {
            System.out.println("Открытых складов нет");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        }
        printStorages();
        String choice = scanner.nextLine();
        int storageId = Integer.parseInt(choice);
        Storage storage = storageRepository.getById(storageId);

        System.out.println("Выберите новое ответственное лицо");
        if(WorkerService.noWorkers("Status = 'Storage Staff'")){
            System.out.println("Подходящие работники отсутствуют");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        }
        WorkerService.printWorkers("Status = 'Storage Staff'");
        choice = scanner.nextLine();
        int newAdminId = Integer.parseInt(choice);

        Worker oldAdmin = workerRepository.getById(storage.adminId);
        if (oldAdmin != null) {
            oldAdmin.setStatus("Storage Staff");
            workerRepository.update(oldAdmin);
        }
        storage.setAdminId(newAdminId);
        Worker newAdmin = workerRepository.getById(newAdminId);
        newAdmin.setStatus("Storage Admin");
        workerRepository.update(newAdmin);

        storageRepository.update(storage);
    }

    //Проверка на отсутствие хранилищ
    public static boolean noStorages() throws SQLException {
        ArrayList<Storage> storages = storageRepository.getAll();
        boolean noStorages = storages.isEmpty();

        return noStorages;
    }
}
