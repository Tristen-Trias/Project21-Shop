import java.util.*;

public class Shop2 {
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		
		int uIn, totalDis;
		double rate = 0.0, total = 0.0;
		int[] amount = null, numDis = null;
		String[] item = null;
		double[] price = null;
		boolean setup = false, bought = false, done = false;
		
		while(true) {
			
			System.out.println("\nThis program supports 4 functions:");
			System.out.println("\t1. Setup Shop \n\t2. Buy \n\t3. List Items \n\t4. Checkout");
			System.out.print("Please choose the function you want: ");
			uIn = checkNum(s);
			
			if (uIn == 1) {
				
				System.out.print("\nPlease enter the number of items to setup shop: ");
				
				uIn = checkNum(s);
				
				amount = new int[uIn];
				item = new String[uIn];
				price = new double[uIn];
				numDis = new int[uIn];
				
				setup(s, amount, item, price, numDis);
				System.out.print("Enter the dollar amount to qualify for Additional Discount (or 0 if none offered): ");
				totalDis = checkNum(s);
				if (totalDis != 0) {
					System.out.print("Enter the Additional Discount rate (e.g., 0.1 for 10%): ");
					rate = checkNum(s, 1);
					while (true) {
						if (rate > 0.0 && rate <= 0.5) break;
						else {
							System.out.print("Invalid input. Enter a value > 0 and <= 0.5:");
							rate = checkNum(s, 1);
						}
					}
					
				}
				
				setup = true;
				
			} else if (uIn == 2) {
				
				if (setup) {
					buy(s, amount, item);
					bought = true;
				} else System.out.println("Shop is not set up yet!");
				
			} else if (uIn == 3) {
				
				if (setup) total = list(amount, item, price);
				else if (!bought) System.out.println("No items were purchaced.");
				else System.out.println("Shop is not set up yet!");
				
			} else if (uIn == 4) {
				
				if (setup) {
					
					checkOut(amount, item, price, numDis, rate, total); 
					done = true;
					
				} else System.out.println("Shop is not set up yet!");
				
			}
			
			if (done) {
				System.out.print("\n\n\nWould you like to re-run (1 for yes, 0 for no)? ");
				uIn = checkNum(s);
				if (uIn == 1) { //resets everything
					uIn = 0;
					amount = null;
					item = null;
					price = null;
					setup = false;
					bought = false;
					done = false;
				} else break;
			} 
			
		}
		s.close();
	}
	
	//Sets up the shop
	public static void setup(Scanner s, int[] amount, String[] item, double[] price, int[] numDis) { 
		
		for (int i = 0; i < amount.length; i++) {
			System.out.print("Enter the name of the " + sfx(i+1) + " product: ");
			item[i] = s.next();
			System.out.print("Enter the per package price of " + item[i] + ": ");
			price[i] = checkNum(s, 1);
			
			while(true) {
				if (price[i] < 0) System.out.print("Please enter a number > 0: ");
				else break;
			}
			
			System.out.print("Enter the number of packages ('x') to qualify for Special Discount (buy 'x' get 1 free) for " + item[i] + ", or 0 if no Special Discount offered: ");
			
			numDis[i] = checkNum(s);
			
			while(numDis[i] < 0) {
				if (numDis[i] > 0) break;
				else {
					System.out.print("Please enter a number > 0: ");
					numDis[i] = checkNum(s);
				}
			}
			
		}
		
	}
	
	public static void buy(Scanner s, int[] amount, String[] item) {
		for (int i = 0; i < amount.length; i++) {
			System.out.print("Enter the number of " + item[i] + " packages to buy: ");
			amount[i] = checkNum(s);
			while(true) {
				if (amount[i] < 0) System.out.print("Please enter a number > 0: ");
				else break;
			}
		}
	}
	
	public static double list(int[] amount, String[] item, double[] price) {
		double total = 0;
		int test = 0;
		System.out.println();
		
		for (int i = 0; i < amount.length; i++) {
			if (amount[i] > 0) test++;
		}
		
		if (test == 0) System.out.println("No items were purchased.");
		else {
			for (int i = 0; i < amount.length; i++) {
				System.out.printf(amount[i] + " packages of " + item[i] + " @ " + price[i] + " per pkg = $%.2f" , (amount[i] * price[i]));
				System.out.println();
				total += amount[i] * price[i];
			}
		}
		
		System.out.println();
		return total;
	}
	
	public static void checkOut(int[] amount, String[] item, double[] price, int[] numDis, double rate, double total) {
		System.out.printf("Original Subtotal: $%.2f" , total);
		
		double discount = 0.0;
		for (int i = 0; i < item.length; i++) {
			discount += getDis(amount, numDis, price, i);
		}
		
		if (discount == 0) System.out.print("\nNo Special Discounts applied");
		System.out.printf("\nSpecial Discounts: -$%.2f" , discount);
		
		total -= discount;
		System.out.printf("\nNew Subtotal: $%.2f" , total);
		
		if (rate > 0) {
			System.out.printf("\nAdditional " + (100 * rate) + "%% discount: $%.2f", rate * total);
			total *= 1 - rate;
		} else System.out.print("\nYou did not qualify for an Additional Discount");
		
		System.out.printf("\nFinal Subtotal: $%.2f" , total);
	}
	
	// gets the discounts for each item
	public static double getDis(int[] amount, int[] numDis, double[] price, int index) {
		double total = 0.0; 
		for(int i = 0; i < amount[index]; i += numDis[index]) {
			total += price[index];
		}
		return total;
	}
	
	//checks to see if the user input is a number
	public static int checkNum(Scanner s) { 
		int a = 0;
		try {
			a = s.nextInt();
		} catch (InputMismatchException e) {
			s.next(); //takes in the garbage from the InputMismatchException */
			System.out.print("Invalid input. ");
			a = checkNum(s);
		}
		return a;
	}
	
	/*
		same as above, except checks to see if double.
		p is just a dummy int 
	*/
	
	public static double checkNum(Scanner s, int p) {
		double a = 0;
		try {
			a = s.nextDouble();
		} catch (InputMismatchException e) {
			s.next(); 
			System.out.println("Invalid input.");
			a = checkNum(s, 0);
		}
		return a;
	}
	
	//renamed from numSuffic to sfx
	public static String sfx(int i) { 
		int rem = i % 10;
		switch (rem) {
			case 0:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
				return (i + "th");
			case 1:
				if (i % 100 != 11) return (i + "st");
				else return (i + "th");
			case 2:
				if (i % 100 != 12) return (i + "nd");
				else return (i + "th");
			case 3:
				if (i % 100 != 13) return (i + "rd");
				else return (i + "th");
			default:
			break;
		}
		
		return "";
	
	}
	
}
