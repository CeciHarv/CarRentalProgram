/*
Program to allow customers to select a rental car, 
how many days they would like to rent it, marks the car as unavailable, 
and gives the customer the total including tax.
 */
package finalproject;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author cecilia 
 */
public class FinalProject {

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {
      
      //Decl and init vars
      Scanner input = new Scanner(System.in);
      String name; //customer name
      char[] carCode; //customer preferred car type, will be converted to char
      int numDays = 0; //number of days the custome wants to keep the car
      String [] carDescription = new String[]{
                                     "1967 Ford Mustang",
                                     "1922 Ford Model T",
                                     "2008 Lincoln Continental",
                                     "2002 Lexus GS F", 
                                     "2007 BMW M5 Competition Sedan",
                                     "1910 Mercer Runabout",
                                     "2009 Mercedes Benz S-Class Cabriolet",
                                     "1930 Cadillac V-16"}; //year, make, model of each car
      double [] dailyFee = new double[]{65.0, 95.0, 135.0, 140.0, 160.0, 165.0, 200.0, 205.0}; //amount charged per day for the chosen vehicle
      char [] carType = {'A', 'A', 'L', 'L', 'L', 'A', 'L', 'A'}; //array storing car type (luxury or antique) to be referenced by carCode
      boolean [] available = new boolean[] {true, true, true, true, true, true, true, true, }; //array storing values of cars that are available or unavailable to be referenced with carType chosen
      boolean anotherCar; //boolean controlling the continuation of the program after renting a car
      int carRented; //index # of car rented
      double totalCost = 0.0; //
      boolean onLot;
      
      
      System.out.println("Please enter your name: ");
      name = input.nextLine();
         
      do {
         //gather pertinent info from customer
         do { //require input beginning with either A or L 
            input = new Scanner(System.in);
            System.out.println("Please enter your preferred car type: ((L)-Luxury or (A)-Antique)");
            carCode = input.nextLine().toUpperCase().toCharArray(); 
         } while (carCode[0] != 'A' && carCode[0] != 'L');
         
         onLot = PreferredAvailable(carCode, carType, available);
            if (!onLot) {
                onLot = OtherAvailable(available);
                if (onLot) {
                    System.out.println("Unfortunately, there are no cars left of your preferred type.");
                    if (carCode[0] ==  'L') {
                        System.out.println("There are still Antique cars available. Would you like to try one?");
                    }//end if they wanted a luxury car
                    
                    else {
                        System.out.println("There are still Luxury cars available. Would you like to try one?");
                    }//end if they wanted an antique

                }
                else {
                    System.out.println("Unfortunately there are no cars left to rent. Please try again another day.");
                    anotherCar = false;
                }//end if no car available. 
            }//end if not onLot
            
            else {
         
                //method forces valid input and exception handling on the rental period. Returns the number of days rented
                numDays = RentalPeriod(); 

                //invoke method FulfillRequest to find car and assign it a rental fee, returns the index # of the rented car or -1 if there was no such car
                carRented = FulfillRequest(name, carCode, carDescription, dailyFee, carType, available);

                //invoke pricing display method that will print pricing information for the customer. argument (carRented)
                totalCost += Billing(name, carRented, dailyFee, numDays, carType);
                //System.out.println(totalCost);
            }
         //check to see if there are any avilable cars left, and if not, change anotherCar to false;
         anotherCar = RentAnother(available, name);
         
      } while (anotherCar);// end do while outer loop to begint he program again or exit
      
      //try printing available array out to a file and read the values from the file when it starts back up again
      
      //graceful closing
      System.out.println("Thank you for choosing Cost is No Object for your car experience!");
      System.out.printf("%nYour order total is: $%.2f%n", totalCost);
      
   }//end main
   
   //method FulfillRequest will find first available car of selected type, display description and rental fee, change rental indicator, return index# of rental car
   public static int FulfillRequest(String name, char[] carCode, String[]carDescription, double[] dailyFee, char[] carType, boolean[] available) {
      int carRented = -1;
      //Find cars of the preferred car type and check to see if they are available, and change the availablility of that car
      for (int i = 0; i < 8; i++) { 
         if (carType[i] == carCode[0] && available[i]) {
            carRented = i;
            available[i] = false;
            //display description and daily rental fee of first available car of preferred type
            System.out.println(name + ", your next automobile experience will be with the " + carDescription[carRented] + "! ");
            System.out.printf("Your daily fee will be: $%.2f %n", dailyFee[carRented]);
            break;
         }//if car is the correct type
      }//end for loop find preferred car type
      
      //if there are no available cars of preferred type, assign carRented value of -1 and return
      if (carRented == -1) {
         System.out.println("Unfortunately, there are no cars of your preferred type available.\n");
      }//end if there are no cars available 
      
      return carRented;
   }//end method FulfillRequest
   
   //available method shows if there is a car left of a certain kind
   public static boolean PreferredAvailable(char[] carCode, char[] carType, boolean[] available) {
       for (int i = 0; i < 8; i++) {
           if (carType[i] == carCode[0]) {
               if (available[i]) {
                   return true;
               } //end if available        
           }//end if carType matches Car code, check availability
       } //end for 
       
       return false;
    
   } //method available checks for availability of car type requested, and if not available, then it checks the other car type to see if there is one available. Then it returns a boolean. 
   
   public static boolean OtherAvailable (boolean[] available) {
       
       for (int i = 0; i < 8; i++) {
            if (available[i]) {
                return true;
            } //end if available        
       } //end for 
       
       return false;
       
   }//end method, any car availalble
   
   //rent another car? method requires boolean[] available,boolean carsLeft,boolean anotherCar, and returns boolean anotherCar
   public static boolean RentAnother(boolean[] available, String name) {
      boolean carsLeft = true;
      String userResponse = "";
      Scanner input = new Scanner(System.in);
      boolean anotherCar; 
      
        for (int i = 0; i < available.length; i++) {
             if (available[i]) {
                carsLeft = true;
                break;
             }//end if 
             else {
                carsLeft = false;
             }//end not available
         }//end if all the cars are rented scenario
         
         if (carsLeft) {
            //reset the boolean anotherCar to see if the loop should go again 
            System.out.println("\nWill you be renting another vehicle with us, " + name +"? (Type Yes or No): ");
            userResponse = input.nextLine();  
            
            if (userResponse.equalsIgnoreCase("yes") || userResponse.equalsIgnoreCase("y")) {
                anotherCar = true;
            }//end if yes
            
            else {
                anotherCar = false;
            }//end else no
            
         }//end cars left;
         else {
            System.out.println("There are no cars left! Thank you for your patronage.");
            anotherCar = false;
            return anotherCar;
         }// end else no cars left
         
         return anotherCar;
   }//end RentAnother method
   
   //method for billing and displaying the totals, returns the total billing amount
   public static double Billing(String name, int carRented, double[] dailyFee, int numDays, char[] carType) {      
       double taxRate; 
       
       if (carType[carRented] == 'A') {
           taxRate = .06;
       }//end if antique
       
       else {
           taxRate = .08;
       }//end luxury tax tax rate
       
       double subTotal = (dailyFee[carRented] * numDays);
       double tax = (subTotal * taxRate);
       double billTotal = (dailyFee[carRented] * numDays) + tax;
       
       System.out.println("\nSubtotals for this car rental:");
       System.out.printf("Rental Fee: $%.2f%n", subTotal);
       System.out.printf("Tax: $%.2f%n", tax);
       System.out.printf("This Car Total: $%.2f%n", billTotal);
       
       return billTotal;
               
   }//end Billing method
   
   //Rental period
   public static int RentalPeriod() {
       int numDays = 0; 
       Scanner input = new Scanner(System.in);
       
       do { //forced valid input and exception handling on the num days
             
            System.out.println("Please enter the number of days you would like to keep the car: ");
            try {
                numDays = input.nextInt();
            }//end try
            catch (InputMismatchException err) {
                System.err.println("Wrong input type. Please try again.");
                input = new Scanner(System.in);
            }//
            
            if (numDays < 1) {
                System.out.println("You must rent the car for a minimum of 1 day.");
            }//end if negative rental days
            
         } while (numDays < 1);
       
       return numDays;
   }//end Rental Period method
   
}//end class
