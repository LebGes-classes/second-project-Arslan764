package org.example.Sevices;

import org.example.Models.storage.SalePoint;
import org.example.dataBase.Repository.SalePointRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class AccountingService {

    private static final Scanner scanner = new Scanner(System.in);

    static SalePointRepository salePointRepository = new SalePointRepository();

    public static void printOrganisationRevenue() throws SQLException {
        ArrayList<SalePoint> salePoints = salePointRepository.getAll();
        double totalRevenue = 0;

        for(SalePoint salePoint : salePoints) {
            System.out.println("Доходность пункта продаж " + salePoint.id + ": " + salePoint.revenue);
            totalRevenue += salePoint.revenue;
        }

        System.out.println();
        System.out.println("Общая доходность компании: " + totalRevenue);
        System.out.println("Введите любой символ для выхода");
        String ch = scanner.nextLine();
    }

    public static void printSalePointRevenue() throws SQLException {
        System.out.println("Выберите пункт продаж");
        ArrayList<SalePoint> salePoints = salePointRepository.getAll();
        if(salePoints.isEmpty()) {
            System.out.println("Пункты продаж отсутствуют");
            System.out.println("Введите любой символ для выхода");
            String ch = scanner.nextLine();
            return;
        } else{
            for(SalePoint salePoint : salePoints) {
                System.out.println(salePoint);
            }
        }
        int salePointId = Integer.parseInt(scanner.nextLine());
        SalePoint salePoint = salePointRepository.getById(salePointId);
        System.out.println("Доходность пункта продаж " + salePoint.id + ": " + salePoint.revenue);
        System.out.println("Введите любой символ для выхода");
        String ch = scanner.nextLine();
    }

}
