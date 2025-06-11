package org.example.UI;

import org.example.Sevices.*;

import java.sql.SQLException;
import java.util.Scanner;

public class Menu {
    private static Scanner scanner = new Scanner(System.in);

    //главное меню
    public static void start() throws SQLException {
        clearConsole();
        System.out.println("Добро пожаловать");
        System.out.println("Выберите действие:");
        System.out.println("1) Управление складами");
        System.out.println("2) Управление пунктами продаж");
        System.out.println("3) Управление сотрудниками");
        System.out.println("4) Управление товарами");
        System.out.println("5) Управление заказами");
        System.out.println("6) Информация о доходности");
        System.out.println("0) Выход");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                manageStorages();
                break;
            case "2":
                manageSalePoints();
            case "3":
                manageEmployees();
                break;
            case "4":
                manageProducts();
                break;
            case "5":
                manageOrders();
                break;
            case "6":
                accountingInformation();
                break;
            case "0":
                System.exit(0);
                break;
            default:
                start();
                break;
        }
    }

    public static void manageStorages() throws SQLException {
        clearConsole();
        System.out.println("Управление складами");
        System.out.println("Выберите действие:");
        System.out.println("1) Просмотр всех складов");
        System.out.println("2) Закрыть  склад");
        System.out.println("3) Открыть новый склад");
        System.out.println("4) Информация о товарах на складе");
        System.out.println("5) Сменить ответственное лицо склада");
        System.out.println("0) Выход");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                StoragesService.printAllStorages();
                break;
            case "2":
                StoragesService.closeStorage();
                break;
            case "3":
                StoragesService.openStorage();
                break;
            case "4":
                StoragesService.productsInformation();
                break;
            case "5":
                StoragesService.changeAdmin();
                break;
            case "0":
                start();
                break;
            default:
                manageStorages();
                break;
        }
        manageStorages();
    }

    public static void manageSalePoints() throws SQLException {
        clearConsole();
        System.out.println("Управление пунктами продаж");
        System.out.println("Выберите действие:");
        System.out.println("1) Просмотр всех пунктов продаж");
        System.out.println("2) Закрыть  пункт продаж");
        System.out.println("3) Открыть новый пункт продаж");
        System.out.println("4) Информация о товарах в пункте продаж");
        System.out.println("5) Сменить ответственное лицо пункта продаж");
        System.out.println("0) Выход");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                SalePointsService.printAllSalePoints();
                break;
            case "2":
                SalePointsService.closeSalePoint();
                break;
            case "3":
                SalePointsService.openSalePoint();
                break;
            case "4":
                SalePointsService.productsInformation();
                break;
            case "5":
                SalePointsService.changeAdmin();
                break;
            case "0":
                start();
                break;
            default:
                manageSalePoints();
                break;
        }
        manageSalePoints();
    }

    public static void manageEmployees() throws SQLException {
        clearConsole();
        System.out.println("Управление сотрудниками:");
        System.out.println("1) Нанять сотрудника");
        System.out.println("2) Уволить сотрудника");
        System.out.println("3) Перевести сотрудника на другое рабочее место");
        System.out.println("4) Посмотреть информацию о всех действующих сотрудниках");
        System.out.println("0) Выход");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                WorkerService.hireWorker();
                break;
            case "2":
                WorkerService.fireWorker();
                break;
            case "3":
                WorkerService.changeWorkPlace();
                break;
            case "4":
                WorkerService.printAllWorkers();
                break;
            case "0":
                start();
                break;
            default:
                manageEmployees();
                break;
        }
        manageEmployees();
    }

    public static void manageProducts() throws SQLException {
        clearConsole();
        System.out.println("Управление товарами");
        System.out.println("1) Добавить товар");
        System.out.println("2) Посмотреть доступные для покупки товары");
        System.out.println("3) Закупить товар");
        System.out.println("4) Переместить товар со склада на пункт продаж");
        System.out.println("5) Переместить товар с пункта продаж на склад");
        System.out.println("6) Переместить товар со склада на другой склад");
        System.out.println("7) Переместить товар из пункта продаж на другой пункт продаж");
        System.out.println("0) Выход");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                ProductService.addProduct();
                break;
            case "2":
                ProductService.printAvailableProducts();
                break;
            case "3":
                ProductService.buyProduct();
                break;
            case "4":
                ProductService.moveFromStorageToSalePoint();
                break;
            case "5":
                ProductService.moveFromSalePointToStorage();
                break;
            case "6":
                ProductService.moveFromStorageToStorage();
                break;
            case "7":
                ProductService.moveFromSalePointToSalePoint();
                break;
            case "0":
                start();
                break;
            default:
                manageProducts();
                break;
        }
        manageProducts();
    }

    public static void manageOrders() throws SQLException {
        clearConsole();
        System.out.println("Управление заказами");
        System.out.println("1) Создать новый заказ");
        System.out.println("2) Оформить возврат заказа");
        System.out.println("3) Просмотр всех заказов определенного товара");
        System.out.println("4) Просмотр всех заказов определенного покупателя");
        System.out.println("0) Выход");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                OrdersService.makeOrder();
                break;
            case "2":
                OrdersService.makeRefund();
                break;
            case "3":
                OrdersService.printOrdersByProduct();
                break;
            case "4":
                OrdersService.printOrdersByBuyer();
                break;
            case "0":
                start();
                break;
            default:
                manageOrders();
                break;
        }
        manageOrders();
    }

    public static void accountingInformation() throws SQLException {
        clearConsole();
        System.out.println("Информация о доходности");
        System.out.println("1) Информация о доходности предприятия");
        System.out.println("2) Информация о доходности пункта продаж");
        System.out.println("0) Выход");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                AccountingService.printOrganisationRevenue();
                break;
            case "2":
                AccountingService.printSalePointRevenue();
                break;
            case "0":
                start();
                break;
            default:
                accountingInformation();
                break;
        }
        accountingInformation();
    }

    //метод очищающий консоль
    public static void clearConsole(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
