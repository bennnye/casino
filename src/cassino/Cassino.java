package cassino;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Cassino {

    //these global variables are used across the whole program to assit the games
    private static Scanner scan = new Scanner(System.in);
    private static Random random = new Random();
    private static int betAmount = 0;
    private static String Continue = "";

    //these global variables store data about the players for use in the program
    private static ArrayList<player> players = new ArrayList<>();
    private static String username1 = "a";
    private static int coins = 0;
    private static String playername = "";

    //these are global variables used in the higher or lower classes
    private static int currentCardNum = 0;
    private static int newCardNum = 0;

    //these are global variables used in the slot machine classes
    private static String wheel1 = "";
    private static String wheel2 = "";
    private static String wheel3 = "";

    public static void main(String[] args) {
        players = CassinoFileHandeling.readFile(); // getting all the accounts that already existed off of the file

        //bellow is my log in menu
        System.out.println("welcome to Ben's casino. Where you can gamble non-existant coins to your hearts content without spending a single real penny.");
        System.out.println("please log in");
        System.out.println("press 1 to log in");
        System.out.println("press 2 to create an account");
        System.out.println("press 0 to exit");
        int userChoice = scan.nextInt();

        switch (userChoice) {
            case 1:
                logIn();
                break;
            case 2:
                createAccount();
                break;
            case 0:
                System.exit(0);
        }
    }

    public static void logIn() {
        // this is getting the player to enter their log in details
        System.out.println("please enter your usename");
        username1 = scan.next();
        System.out.println("please enter your password");
        String password = scan.next();

        //this is checking the log in details are correct
        int index = getindex();
        if (index != -1) {
            if (password.equals(players.get(index).getPassword())) {

                //this code gets the variables reqired so that they can be printed in the welcome back message
                playername = players.get(index).getName();
                getCoins();
                System.out.println("welcome back " + playername + " you have " + coins + " to play with.");
                menu();

            } else {
                logIn();
            }
        } else {
            System.out.println("this account does not seem to exist");
        }

    }

    public static void createAccount() {
        //this code is getting the player to enter the details, defined in the player class, so thatan account an be created
        System.out.println("enter your name player: ");
        String name = scan.next();
        System.out.println("enter your username " + name);
        String username = scan.next();
        System.out.println("enter your password");
        String password = scan.next();

        //this makes sure that the user will get their password is what they ment it to be
        System.out.println("please confirm your password");
        String confirmPassword = scan.next();
        if (!password.equals(confirmPassword)) {
            System.out.println("your passwords don't match please try and create your account again");
            createAccount();
        }
        //this sets the number of coins the player has at the start
        System.out.println("we will give you 1000 coins to start playing now");
        int coins = 1000;

        //this will allow the getindex method to work
        username1 = username;

        //this sets up the players account
        player newPlayer = new player(name, username, password, coins);
        players.add(newPlayer);

        //this gets the players name for future reference
        int index = getindex();
        if (index != -1) {
            if (password.equals(players.get(index).getPassword())) {
                playername = players.get(index).getName();
            }
        }
        menu();
    }

    public static int getindex() {
        if (!players.isEmpty()) { //this will get the index of the player provided that the players array lis is not empty
            for (int i = 0; i < players.size(); i++) {
                if (username1.equals(players.get(i).getUsername())) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static void menu() {
        //this is the casino checking that the player can play
        getCoins();

        //if the player has no coins then their accont is deleted
        if (coins == 0) {
            System.out.println("sorry this account has no coins left you will have to create a new account and this one will be deleted");
            int index = getindex();
            players.remove(index);
            CassinoFileHandeling.writeFile(players);
            System.exit(0);
        } //this is the menu system
        else {
            System.out.println("what would you like to do? press:");
            System.out.println("1 - check the number of coins in your bank");
            System.out.println("2 - play roulette");
            System.out.println("3 - play higher or lower");
            System.out.println("4 - play a slot machine");
            System.out.println("0 - exit");
            int userchoice = scan.nextInt();

            switch (userchoice) {
                case 1:
                    getCoins();
                    System.out.println(playername + " you have " + coins + " coins to play with");
                    menu();
                case 2:
                    roulette();
                    break;
                case 3:
                    higherOrLower();
                    break;
                case 4:
                    slotMachine();
                    break;
                case 0:
                    CassinoFileHandeling.writeFile(players); //this line is in place to write the players array list to the file before the system is shut down
                    System.exit(0);
            }
        }
    }

    public static void getCoins() {
        //this will find out how many coins a player has
        int index = getindex();
        coins = players.get(index).getCoins();

    }

    public static void roulette() {
        getCoins();
        //explaining the rules
        System.out.println(playername + " welcome to roulette. Incase you are not familliar with the rules of the game a ball is spun around the wheel and the number it lands on is the winning number. If the number is odd it is red and if it is even then it is black. You can bet on a number winning, or a colour winning.");
        System.out.println("would you like to continue (yes for roulette or no for the menu)?"); //allowing for thinking time
        Continue = scan.next();
        if (Continue.equals("yes")) {
            //selecting the type of bet
            System.out.println("would you like to bet on a number or colour? A correct number will get you four times the bet and a correct colour gets you twice the bet.");
            String betType = scan.next();

            //finding out what the type of bet will be
            if (betType.equals("number")) {
                //placing the bet
                System.out.println("enter a number between 1 and 79");
                int numberBet = scan.nextInt();
                System.out.println("how many coins are you placing on the number?");
                betAmount = scan.nextInt();
                //taking the bet out of the players coins
                coins = coins - betAmount;
                changeCoins();

                //spinning the wheel
                int landedOn = rouletteSpin();
                //deciding if the player has won or lost
                if (landedOn == numberBet) {
                    System.out.println("congratulations " + playername + " you won " + betAmount * 4 + " coins");
                    coins = coins + betAmount * 4;
                    changeCoins();
                } else {
                    System.out.println("sorry you lost");
                }
                //this will allow the player to play again or go back to the menu
                System.out.println("would you like to continue to the menu(yes for menu or no for another game of roulette)?");
                Continue = scan.next();
                if (Continue.equals("yes")) {
                    menu();
                } else {
                    roulette();
                }
            } else {
                //plaing the bet on a colour
                System.out.println("enter the coulour of choice (red or black)?");
                String colour = scan.next();
                System.out.println("how many coins are you placing on the colour?");
                betAmount = scan.nextInt();
                //subtracting the bet from the players coins
                coins = coins - betAmount;
                changeCoins();
                //spinning the wheel
                int landedOn = rouletteSpin();
                //deciding if the player won or lost
                if (colour.equals("black") && landedOn % 2 == 0) {
                    System.out.println("congratulations " + playername + " you won " + betAmount * 2 + " coins");
                    coins = coins + betAmount * 2;
                    changeCoins();
                } else if (colour.equals("red") && landedOn % 2 == 1) {
                    System.out.println("congratulations " + playername + " you won " + betAmount * 2 + " coins");
                    coins = coins + betAmount * 2;
                    changeCoins();
                } else {
                    System.out.println("sorry you lost");
                }
                //allowing the player to play again or go to the menu
                System.out.println("would you like to continue to the menu(yes for menu or no for another game of roulette)?");
                Continue = scan.next();
                if (Continue.equals("yes")) {
                    menu();
                } else {
                    roulette();
                }
            }
        } else {
            menu();
        }
    }

    public static int rouletteSpin() {
        //finding out what numbr the ball landed on
        int landedOn = random.nextInt((78) + 1);
        if (landedOn % 2 == 1) {
            System.out.println("\u001b[31m you landed on " + landedOn + "\u001b[30m"); //this line will print out in red because the number is odd
        } else {
            System.out.println("you landed on " + landedOn);
        }
        return landedOn;
    }

    public static void higherOrLower() {
        //the explaining the rules segment
        System.out.println(playername + " welcome to higher or lower the game is simple bet on wether or not the next card is higher or lower than the current card. \nA win is worth 2 times the bet. \nIn this game ace is counted as 1");
        //allows players to change their minds about playing
        System.out.println("would you like to continue (yes for higer or lower or no for the menu)?");
        Continue = scan.next();
        if (Continue.equals("yes")) {
            //decides how many games will be played befor the program repeates
            System.out.println("how many games would you like to play");
            int games = scan.nextInt();

            //this sets up the first card
            getCardNumber();
            //this turns the number into a playing card
            ArrayList<String> card = getCard();
            for (int i = 0; i < games; i++) { //allows multiple games to be played without the use of the menu
                //printing the current card
                System.out.println("the current card is " + card);
                //placing the bet
                System.out.println("is the next card higher or lower than " + card);
                String guess = scan.next();
                System.out.println("how much are you putting on your guess");
                betAmount = scan.nextInt();
                coins = coins - betAmount;
                changeCoins();
                //getting the new card
                getCardNumber2();
                ArrayList<String> newCard = getCard2();
                System.out.println("the new card is " + newCard);
                //deciding if the player won or lost
                if (newCardNum < currentCardNum && guess.equals("lower")) {
                    System.out.println("congratulations " + playername + " you won " + betAmount * 2 + " coins");
                    //making sure that the player gets the amount of coins updated
                    coins = coins + betAmount * 2;
                    changeCoins();
                } else if (newCardNum > currentCardNum && guess.equals("higher")) {
                    System.out.println("congratulations " + playername + " you won " + betAmount * 2 + " coins");
                    //updating the coins
                    coins = coins + betAmount * 2;
                    changeCoins();
                } else if (newCardNum == currentCardNum) {
                    System.out.println("the cards are the same have your bet money back");
                    coins = coins + betAmount;
                    changeCoins();
                } else {
                    System.out.println("hard luck " + playername + "you got it wrong");
                }
                //allowing the player reading time
                System.out.println("enter yes to continue");
                Continue = scan.next();
                //this sets the new card to be the current card
                currentCardNum = newCardNum;
                card = newCard;
            }
            menu();
        } else {
            menu();
        }

    }

    public static void getCardNumber() {
        currentCardNum = random.nextInt(12); //generating a random number
    }

    public static void getCardNumber2() {
        newCardNum = random.nextInt(12); //generating a random number
    }

    public static ArrayList<String> getCard() {
        //setting up arrays to allow the computer to create a card
        String[] numbers = {"ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king"};
        String[] suits = {"hearts", "diamonds", "spade", "clubs"};
        //setting up the array list to save the card variables to
        ArrayList<String> card = new ArrayList<>();
        //setting up the card suit
        int suit = random.nextInt(3);
        for (int i = 0; i < 3; i++) {
            if (i == suit) {
                card.add(suits[i]);
            }
        }
        //setting up the card value
        for (int i = 0; i < 13; i++) {
            if (i == currentCardNum) {
                card.add(numbers[i]);
            }
        }
        return card;
    }

    public static ArrayList<String> getCard2() {
        //setting up arrays to allow the computer to create a card
        String[] numbers = {"ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king"};
        String[] suits = {"hearts", "diamonds", "spade", "clubs"};
        //setting up the array list to save the card variables to
        ArrayList<String> card = new ArrayList<>();
        //setting up the card suit
        int suit = random.nextInt(3);
        for (int i = 0; i < 3; i++) {
            if (i == suit) {
                card.add(suits[i]);
            }
        }
        //setting up the card value
        for (int i = 0; i < 13; i++) {
            if (i == newCardNum) {
                card.add(numbers[i]);
            }
        }
        return card;
    }

    public static void slotMachine() {
        getCoins(); //making sure that the coins variable is correct
        System.out.println(playername + " welcome to the slot machine. this game is a coin per spin. If two of the wheels match then you win 20 coins and if all three match you win 30 coins.");
        //allows the plauer to change their mind
        System.out.println("would you like to continue (yes for the slot machine or no for the menu)?");
        Continue = scan.next();
        if (Continue.equals("yes")) {
            //entering the coins to spin the slot machine
            System.out.println("how many spins do you want?");
            int spins = scan.nextInt();
            coins = coins - spins;
            changeCoins();
            for (int i = 0; i < spins; i++) {//allowing multiple pins in one go
                slotMachineRoll();
                //deciding if the player has one anything
                System.out.println("you rolled " + wheel1 + ", " + wheel2 + ", " + wheel3);
                if (wheel1.equals(wheel2) && wheel2.equals(wheel3)) {
                    System.out.println("you won 30 coins");
                    coins = coins + 30;
                    changeCoins();
                } else if (wheel1.equals(wheel2) || wheel2.equals(wheel3) || wheel1.equals(wheel3)) {
                    System.out.println("you won 20 coins");
                    coins = coins + 20;
                    changeCoins();
                } else {
                    System.out.println("you didn't get a match this time, please play again some other time though.");
                }
                //allowing the player time to read
                System.out.println("enter yes to continue");
                Continue = scan.next();
                if (Continue.equals("yes")) {

                }
            }
            menu();
        } else {
            menu();
        }
    }

    public static void slotMachineRoll() {
        //this will allow the slot machine to roll
        //the array of possible results
        String[] options = {"orange", "apple", "bannana", "mango"};
        //setting the values that will be turned into results for the wheels
        int rolled1 = random.nextInt(3);
        int rolled2 = random.nextInt(3);
        int rolled3 = random.nextInt(3);
        //turning the values into results
        for (int i = 0; i < 3; i++) {
            if (i == rolled1) {
                wheel1 = options[i];
            }
        }
        for (int i = 0; i < 3; i++) {
            if (i == rolled2) {
                wheel2 = options[i];
            }
        }
        for (int i = 0; i < 3; i++) {
            if (i == rolled3) {
                wheel3 = options[i];
            }
        }
    }

    public static void changeCoins() {
        //this will allow the coins variable in the array list
        int index = getindex();
        players.get(index).setCoins(coins);
    }
}
