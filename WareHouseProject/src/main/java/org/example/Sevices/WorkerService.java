package org.example.Sevices;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import org.example.Models.storage.SalePoint;
import org.example.Models.storage.Storage;
import org.example.dataBase.Repository.*;
import org.example.Models.Person.Worker;

public class WorkerService {
    private static Scanner scanner = new Scanner(System.in);
    static WorkerRepository workerRepository = new WorkerRepository();
    static StorageRepository storageRepository = new StorageRepository();
    static SalePointRepository salePointRepository = new SalePointRepository();

    public static void hireWorker() throws SQLException{
        Worker worker = new Worker();

        System.out.println("Введите имя сотрудника");
        String firstName = scanner.nextLine();

        System.out.println("Введите фамилию сотрудника");
        String lastName = scanner.nextLine();

        System.out.println("Введите номер телефона сотрудника");
        String phoneNumber = scanner.nextLine();

        worker.setFirstName(firstName);
        worker.setLastName(lastName);
        worker.setPhoneNumber(phoneNumber);

        System.out.println("Выберите куда устроить сотрудника");
        System.out.println("1) Склад");
        System.out.println("2) Пункт продаж");
        System.out.println("0) Выход");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                System.out.println("Выберите склад для устройства сотрудника");
                if (!printStorages()){
                    return;
                }
                int storageId = Integer.parseInt(scanner.nextLine());
                worker.setWorkPlaceId(storageId);
                worker.setStatus("Storage Staff");

                workerRepository.add(worker);
                break;
            case "2":
                System.out.println("Выберите пункт продаж для устройства сотрудника");
                if(!printSalePoints()){
                    return;
                }
                int salePointId = Integer.parseInt(scanner.nextLine());
                worker.setWorkPlaceId(salePointId);
                worker.setStatus("SalePoint Staff");

                workerRepository.add(worker);
                break;
            case "0":
                return;
            default:
                hireWorker();
                break;
        }
    }

    public static void fireWorker() throws SQLException{
        String condition = "Status = 'SalePoint Staff' OR Status = 'SalePoint Admin' OR Status = 'Storage Staff' OR Status = 'Storage Admin'";
        System.out.println("Выберите сотрудника которого хотите уволить");
        if (noWorkers(condition)){
            System.out.println("Подходящих сотрудников не найдено");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        }
        printWorkers(condition);
        int workerId = Integer.parseInt(scanner.nextLine());
        Worker firedWorker = workerRepository.getById(workerId);
        firedWorker.setStatus("Fired");

        SalePoint salePoint = salePointRepository.getById(salePointRepository.getId("Admin_id = " + workerId));
        if (salePoint != null){
            salePoint.setAdminId(0);
            salePointRepository.update(salePoint);
        }

        Storage storage = storageRepository.getById(storageRepository.getId("Admin_id = " + workerId));
        if (storage != null){
            storage.setAdminId(0);
            storageRepository.update(storage);
        }

        workerRepository.update(firedWorker);
    }

    public static void changeWorkPlace() throws SQLException{
        System.out.println("1) Перевести сотрудника склада на точку продаж");
        System.out.println("2) Перевести сотрудника склада на другой склад");
        System.out.println("3) Перевести сотрудника точки продаж на склад");
        System.out.println("4) Перевести сотрудника точки продаж на другую точку продаж");
        System.out.println("0) Выход");

        String choice = scanner.nextLine();
        int workerId;
        Worker worker;
        int storageId;
        int salePointId;
        switch (choice) {
            case "1":
                if(!printStorageStaff()){
                    return;
                }
                workerId = Integer.parseInt(scanner.nextLine());
                worker = workerRepository.getById(workerId);

                System.out.println("Выберите точку продаж для перевода сотрудника");
                if(!printSalePoints()){
                    return;
                }
                salePointId = Integer.parseInt(scanner.nextLine());
                worker.setWorkPlaceId(salePointId);
                worker.setStatus("SalePoint Staff");

                workerRepository.update(worker);
                break;
            case "2":
                if(!printStorageStaff()){
                    return;
                }
                workerId = Integer.parseInt(scanner.nextLine());
                worker = workerRepository.getById(workerId);

                System.out.println("Выберите склад для перевода сотрудника");
                if(!printSalePoints()){
                    return;
                }
                storageId = Integer.parseInt(scanner.nextLine());
                worker.setWorkPlaceId(storageId);

                workerRepository.update(worker);
                break;
            case "3":
                if(!printSalePointStaff()){
                    return;
                }
                workerId = Integer.parseInt(scanner.nextLine());
                worker = workerRepository.getById(workerId);

                System.out.println("Выберите склад для перевода сотрудника");
                if(!printStorages()){
                    return;
                }
                storageId = Integer.parseInt(scanner.nextLine());
                worker.setWorkPlaceId(storageId);
                worker.setStatus("Storage Staff");

                workerRepository.update(worker);
                break;
            case "4":
                if(!printSalePoints()){
                    return;
                }
                workerId = Integer.parseInt(scanner.nextLine());
                worker = workerRepository.getById(workerId);

                System.out.println("Выберите точку продаж для перевода сотрудника");
                if(!printSalePoints()){
                    return;
                }
                salePointId = Integer.parseInt(scanner.nextLine());
                worker.setWorkPlaceId(salePointId);

                workerRepository.update(worker);
                break;
            case "0":
                return;
            default:
                changeWorkPlace();
                break;
        }
        changeWorkPlace();
    }

    public static boolean printStorageStaff() throws SQLException{
        String condition ="Status = 'Storage Staff'";
        if (noWorkers(condition)){
            System.out.println("Подходящих сотрудников не найдено");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return false;
        }
        printWorkers(condition);
        return true;
    }

    public static boolean printSalePointStaff() throws SQLException{
        String condition ="Status = 'SalePoint Staff'";
        if (noWorkers(condition)){
            System.out.println("Подходящих сотрудников не найдено");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return false;
        }
        printWorkers(condition);
        return true;
    }

    public static boolean printStorages() throws SQLException{
        ArrayList<Storage> storages = storageRepository.getAll();
        if(storages.isEmpty()){
            System.out.println("Точки продаж отсутствуют");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return false;
        } else{
            for (Storage storage : storages) {
                System.out.println(storage);
            }
        }
        return true;
    }

    public static boolean printSalePoints() throws SQLException{
        ArrayList<SalePoint> salePoints = salePointRepository.getAll();
        if(salePoints.isEmpty()){
            System.out.println("Точки продаж отсутствуют");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return false;
        } else{
            for (SalePoint salePoint : salePoints) {
                System.out.print(salePoint);
            }
        }
        return true;
    }

    public static void printAllWorkers() throws SQLException{
        String condition = "Status = 'SalePoint Staff' OR Status = 'SalePoint Admin' OR Status = 'Storage Staff' OR Status = 'Storage Admin'";
        if(noWorkers(condition)){
            System.out.println("Работники отсутствуют");
        }
        printWorkers(condition);
        System.out.println("Введите любой символ для выхода");
        String ch = scanner.nextLine();
        return;
    }

    public static void printWorkers(String condition) throws SQLException {
        ArrayList<Worker> workers = workerRepository.getAll(condition);
        for (Worker worker : workers) {
            System.out.print(worker);
        }
    }

    public static boolean noWorkers(String condition) throws SQLException {
        ArrayList<Worker> workers = workerRepository.getAll(condition);
        boolean noWorkers = workers.isEmpty();
        return noWorkers;
    }
}
