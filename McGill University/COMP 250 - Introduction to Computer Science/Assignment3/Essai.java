package assignment3;

import java.util.Arrays;

public class Essai{
  public static void main(String[] args) {
    
    OneBuilding ob1 =  new OneBuilding("Building 1", 1987, 50, 2025, 35);
    OneBuilding ob2 = new OneBuilding("Building 2", 1988, 60, 2023, 50);
    OneBuilding ob3 = new OneBuilding("Building 3", 1987, 55, 2024, 23);
    OneBuilding ob4 = new OneBuilding("Building 4", 1985, 50, 2022, 5);
    OneBuilding ob5 = new OneBuilding("Building 5", 1985, 45, 2021, 42);
    OneBuilding ob6 = new OneBuilding("Building 6", 1985, 60, 2024, 26);
    OneBuilding ob7 = new OneBuilding("Building 7", 1986, 55, 2024, 11);
    OneBuilding ob8 = new OneBuilding("Building 8", 1995, 55, 2019, 46);
    OneBuilding ob9 = new OneBuilding("Building 9", 1987, 55, 2025, 22);
    OneBuilding ob10 = new OneBuilding("Building 10", 1993, 55, 2025, 9);
    OneBuilding ob11 = new OneBuilding("Building 11", 1985, 47, 2018, 10);
    Building b1 = new Building(ob1);
    b1 = b1.addBuilding(ob4);
    b1 = b1.addBuilding(ob5);
    
    System.out.println("**** Testing addBuilding **** /n");
    System.out.println(b1.addBuilding(ob2).data.name + " should be Building 1 ");
    System.out.println(b1.addBuilding(ob3).data.name + " should be Building 3 ");
    System.out.println(b1.same.data.name + " should be Building 1" );
    System.out.println(b1.addBuilding(ob9).data.name + " should be Building 3 ");
    System.out.println(b1.same.data.name + " should be Building 9");
    System.out.println(b1.same.same.data.name + " should be Building 1");
    System.out.println(b1.younger.data.name + " should be Building 2");
    System.out.println(b1.older.data.name + " should be Building 4");
    System.out.println(b1.older.same.data.name + " should be Building 5");
    b1 = b1.addBuilding(ob6);
    b1 = b1.addBuilding(ob7);
    System.out.println(b1.older.data.name + " should be Building 6");
    System.out.println(b1.older.younger.data.name + " should be Building 7");
    b1 = b1.addBuilding(ob8);
    b1 = b1.addBuilding(ob11);
    System.out.println(b1.addBuilding(ob10).data.name + " should be Building 3 ");
    System.out.println(b1.younger.younger.data.name + " should be Building 8");
    System.out.println(b1.younger.younger.older.data.name + " should be Building 10");
    System.out.println(b1.older.same.same.data.name + " should be Building 11");
    OneBuilding ob12 = new OneBuilding("Building 12 ", 1987, 70, 2025, 10);
    OneBuilding ob13 = new OneBuilding("Building 13 ", 1988, 70, 2024, 10);
    OneBuilding ob14 = new OneBuilding("Building 14 ", 1985, 70, 2023, 10);
    OneBuilding ob15 = new OneBuilding("Building 15 ", 1987, 65, 2022, 10);
    OneBuilding ob16 = new OneBuilding("Building 16 ", 1991, 70, 2021, 10);
    OneBuilding ob17 = new OneBuilding("Building 17 ", 1983, 70, 2020, 10);
    OneBuilding ob18 = new OneBuilding("Building 18 ", 1991, 65, 2020, 10);
    OneBuilding ob19 = new OneBuilding("Building 19 ", 1985, 49, 2020, 10);
    System.out.println("\n");
    Building b2 = new Building(ob12);
    b2 = b2.addBuilding(ob13);
    b2 = b2.addBuilding(ob14);
    b2 = b2.addBuilding(ob15);
    b2 = b2.addBuilding(ob16);
    b2 = b2.addBuilding(ob17);
    b2 = b2.addBuilding(ob18);
    b2 = b2.addBuilding(ob19);
    System.out.println(b2.data.name + " should be Building 12");
    System.out.println(b2.same.data.name + " should be Building 15");
    System.out.println(b2.older.data.name + " should be Building 14");
    System.out.println(b2.younger.data.name + " should be Building 13");
    System.out.println(b2.younger.younger.data.name + " should be Building 16");
    System.out.println(b2.older.older.data.name + " should be Building 17");
    System.out.println(b2.younger.younger.same.data.name + " should be Building 18");
    System.out.println(b2.older.same.data.name + " should be Building 19");
    
    System.out.println(" \n *** Testing addBuildings *** \n ");
    b1 = b1.addBuildings(b2);
    System.out.println(b1.data.name + " should be Building 12");
    System.out.println(b1.same.data.name + " should be Building 15");
    System.out.println(b1.same.same.data.name + " should be Building 3");
    System.out.println(b1.same.same.same.data.name + " should be Building 9");
    System.out.println(b1.same.same.same.same.data.name + " should be Building 1");
    System.out.println(b1.older.data.name + " should be Building 14");
    System.out.println(b1.older.same.data.name + " should be Building 6");
    System.out.println(b1.older.same.same.data.name + " should be Building 4");
    System.out.println(b1.older.same.same.same.data.name + " should be Building 19");
    System.out.println(b1.older.same.same.same.same.data.name + " should be Building 11");
    System.out.println(b1.older.same.same.same.same.same.data.name + " should be Building 5");
    System.out.println(b1.older.older.data.name + " should be Building 17");
    System.out.println(b1.younger.data.name + " should be Building 13");
    System.out.println(b1.younger.same.data.name + " should be Building 2");
    System.out.println(b1.younger.younger.data.name + " should be Building 8");
    System.out.println(b1.younger.younger.older.data.name + " should be Building 10");
    System.out.println(b1.younger.younger.older.older.data.name + " should be Building 16");
    System.out.println(b1.younger.younger.older.older.same.data.name + " should be Building 18");
    System.out.println(b1.older.younger.data.name + " should be Building 7");
    
    System.out.println("\n *** Testing RemoveBuilding *** \n");
    b1 = b1.removeBuilding(b1.data);
    System.out.println(b1.data.name + " should be Building 15");
    System.out.println(b1.older.data.name + " should be Building 14");
    b1 = b1.removeBuilding(b1.data);
    System.out.println(b1.data.name + " should be Building 3");
    System.out.println(b1.older.data.name + " should be Building 14");
    b1 = b1.removeBuilding(b1.data);
    b1 = b1.removeBuilding(b1.data);
    System.out.println(b1.data.name + " should be Building 1");
    System.out.println(b1.older.data.name + " should be Building 14");
    b1 = b1.removeBuilding(b1.data);
    System.out.println(b1.data.name + " should be Building 14");
    System.out.println(b1.younger.data.name + " should be Building 7");
    System.out.println(b1.younger.younger.data.name + " should be Building 13");
    b1 = b1.removeBuilding(b1.younger.younger.younger.data);
    System.out.println(b1.younger.younger.younger.data.name + " should be Building 10");
    System.out.println(b1.younger.younger.younger.older.data.name + " should be Building 16");
    System.out.println(b1.younger.younger.younger.older.same.data.name + " should be Building 18");
    b1 = b1.removeBuilding(b1.data);
    System.out.println(b1.data.name + " should be Building 6");
    System.out.println(b1.older.data.name + " should be Building 17");
    b1 = b1.removeBuilding(b1.data);
    System.out.println(b1.data.name + " should be Building 4");
    System.out.println(b1.younger.data.name + " should be Building 7");
    b1 = b1.removeBuilding(b1.data);
    b1 = b1.removeBuilding(b1.younger.data);
    System.out.println(b1.data.name + " should be Building 19");
    System.out.println(b1.younger.data.name + " should be builing 13");
    b1 = b1.removeBuilding(b1.data);
    b1 = b1.removeBuilding(b1.younger.data);
    System.out.println(b1.data.name + " should be Building 11");
    System.out.println(b1.younger.data.name + " should be builing 2");
    b1 = b1.removeBuilding(b1.data);
    b1 = b1.removeBuilding(b1.younger.data);
    System.out.println(b1.data.name + " should be Building 5");
    System.out.println(b1.younger.data.name + " should be builing 10");
    System.out.println(b1.younger.older.data.name + " should be builing 16");
    b1 = b1.removeBuilding(ob16);
    System.out.println(b1.younger.older.data.name + " should be builing 18");
    b1 = b1.removeBuilding(ob5);
    b1 = b1.removeBuilding(ob17);
    System.out.println(b1.data.name + " should be Building 10");
    b1 = b1.removeBuilding(ob10);
    System.out.println(b1.data.name + " should be Building 18");
    System.out.println(b1.same + " " + b1.older + " " + b1.younger + " should be null null null");
    b1 = b1.removeBuilding(ob18);
    
    System.out.println("\n ***Testing oldest*** \n");
    b1 = new Building(ob8);
    System.out.println(b1.oldest()+ " should be 1995");
    b1.addBuilding(ob5);
    System.out.println(b1.oldest()+ " should be 1985");
    b1 = b1.addBuilding(ob1);
    b1 = b1.addBuilding(ob4);
    b1 = b1.addBuilding(ob2);
    b1 = b1.addBuilding(ob3);
    b1 = b1.addBuilding(ob6);
    b1 = b1.addBuilding(ob7);
    b1 = b1.addBuilding(ob11);
    b1 = b1.addBuilding(ob9);
    b1 = b1.addBuilding(ob10);
    b1 = b1.addBuildings(b2);
    System.out.println(b1.oldest()+ " should be 1983");
    
    System.out.println("\n ***Testing highest*** \n");
    System.out.println(b1.highest()+ " should be 70");
    OneBuilding ob20 = new OneBuilding("Building 20 ", 1984, 80, 2020, 10);
    b1  = b1.addBuilding(ob20);
    System.out.println(b1.highest()+ " should be 80");
    
    System.out.println(" \n ***Testing highest from year*** \n");
    System.out.println(b1.highestFromYear(1985).name + " should be building 14");
    System.out.println(b1.highestFromYear(1987).name + " should be building 12");
    System.out.println(b1.highestFromYear(1991).name + " should be building 16");
    System.out.println(b1.highestFromYear(1980) + " should be null");
 
    System.out.println("\n ***Testing numberFromYears*** \n");
    System.out.println(b1.numberFromYears(1900, 2000)+ " should be 20");
    System.out.println(b1.numberFromYears(1985, 1985)+ " should be 6");
    System.out.println(b1.numberFromYears(1984, 1985)+ " should be 7");
    System.out.println(b1.numberFromYears(1983, 1985)+ " should be 8");
    System.out.println(b1.numberFromYears(1983, 1988)+ " should be 16");
    System.out.println(b1.numberFromYears(1983, 1993)+ " should be 19");
    System.out.println(b1.numberFromYears(1995, 1993)+ " should be 0");
    System.out.println(b1.numberFromYears(1995, 2000)+ " should be 1");
    System.out.println(b1.numberFromYears(1996, 2000)+ " should be 0");
    
    System.out.println("\n ***Testing costPlanning*** \n");
    int[] costs = b1.costPlanning(8);
    for (int i = 0; i<costs.length; i++){
      System.out.print(" " + costs[i] + " ");
    }
    System.out.println(" should be 10 46 40 52 15 60 70 76");
    costs = b1.costPlanning(9);
    for (int i = 0; i<costs.length; i++){
      System.out.print(" " + costs[i] + " ");
    }
    System.out.println(" should be 10 46 40 52 15 60 70 76 0");
    costs = b1.costPlanning(3);
    for (int i = 0; i<costs.length; i++){
      System.out.print(" " + costs[i] + " ");
    }
    System.out.println(" should be 10 46 40 ");
  }
}