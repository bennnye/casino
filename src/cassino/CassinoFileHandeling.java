package cassino;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class CassinoFileHandeling {
    public static String folderDirectory = System.getProperty("user.dir") + "//cassinoPlayers.txt";
    
    public static void writeFile(ArrayList<player> players) {

        try {
            try (FileWriter writeToFile = new FileWriter(folderDirectory, false); PrintWriter printToFile = new PrintWriter(writeToFile)) {
                for (int i = 0; i < players.size(); i++) {
                    printToFile.println(players.get(i).toString());
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
    
    public static ArrayList<player> readFile() {
        ArrayList<player> players = new ArrayList<>();
        String lineFromFile;
        try {
            BufferedReader read = new BufferedReader(new FileReader(folderDirectory));
            while ((lineFromFile = read.readLine()) != null) {
                String[] playerDetails = lineFromFile.split(", ");
                player Player = new player(playerDetails[0], playerDetails[1], playerDetails[2], Integer.valueOf(playerDetails[3]));
                players.add(Player);
            }
            read.close();

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return players;
    }
}
