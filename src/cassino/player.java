package cassino;

public class player {
    
    private String name;
    private String username;
    private String password;
    private int coins;

    public player(String name, String username, String password, int coins) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.coins = coins;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getCoins() {
        return coins;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public String toString(){
        return name + ", " + username + ", " + password + ", " + coins;
    }
}
