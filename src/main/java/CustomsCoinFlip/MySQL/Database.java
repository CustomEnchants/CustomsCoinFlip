package CustomsCoinFlip.MySQL;

import CustomsCoinFlip.CustomsCoinFlipPlugin;
import CustomsCoinFlip.Objects.UserDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Abstract Database class, serves as a base for any connection method (MySQL,
 * SQLite, etc.)
 *
 * @author -_Husky_-
 * @author tips48
 */
public abstract class Database {

    protected Connection connection;

    /**
     * Plugin instance, use for plugin.getDataFolder()
     */
    public final CustomsCoinFlipPlugin plugin = CustomsCoinFlipPlugin.getInstance();

    /**
     * Creates a new Database
     *

     */
    protected Database() {
        this.connection = null;
    }

    /**
     * Opens a connection with the database
     *
     * @return Opened connection
     * @throws SQLException           if the connection can not be opened
     * @throws ClassNotFoundException if the driver cannot be found
     */
    public abstract Connection openConnection() throws SQLException,
            ClassNotFoundException;

    /**
     * Checks if a connection is open with the database
     *
     * @return true if the connection is open
     * @throws SQLException if the connection cannot be checked
     */
    public boolean checkConnection() throws SQLException {
        return connection != null && !connection.isClosed();
    }

    /**
     * Gets the connection with the database
     *
     * @return Connection with the database, null if none
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Closes the connection with the database
     *
     * @return true if successful
     * @throws SQLException if the connection cannot be closed
     */
    public boolean closeConnection() throws SQLException {
        if (connection == null) {
            return false;
        }
        connection.close();
        return true;
    }

    /**
     * Executes a SQL Query<br>
     * <p>
     * If the connection is closed, it will be opened
     *
     * @param query Query to be run
     * @return the results of the query
     * @throws SQLException           If the query cannot be executed
     * @throws ClassNotFoundException If the driver cannot be found; see {@link #openConnection()}
     */
    public ResultSet querySQL(String query) throws SQLException,
            ClassNotFoundException {
        if (!checkConnection()) {
            openConnection();
        }
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    /**
     * Executes an Update SQL Query<br>
     * See {@link Statement#executeUpdate(String)}<br>
     * If the connection is closed, it will be opened
     *
     * @param query Query to be run
     * @return Result Code, see {@link Statement#executeUpdate(String)}
     * @throws SQLException           If the query cannot be executed
     * @throws ClassNotFoundException If the driver cannot be found; see {@link #openConnection()}
     */
    public int updateSQL(String query) throws SQLException,
            ClassNotFoundException {
        if (!checkConnection()) {
            openConnection();
        }
        Statement statement = connection.createStatement();
        return statement.executeUpdate(query);
    }

    public void setupPlayersTable(){
        try {
            String sql = "CREATE TABLE IF NOT EXISTS `players` (UUID VARCHAR(36), Username VARCHAR(36), Wins INT(6), Losses INT(6), NotificationsEnabled INT(6));";
            connection.createStatement().executeUpdate(sql);
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public boolean isUserRegistered(UUID uuid){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT UUID FROM players WHERE UUID='%uuid%';".replace("%uuid%", "" + uuid.toString()));
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void registerUser(UserDAO user){
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO players (UUID, Username, Wins , Losses, NotificationsEnabled) VALUES (?, ?, ?, ?, ?);");
            ps.setString(1, user.getUUID().toString());
            ps.setString(2, user.getName());
            ps.setInt(3, 0);
            ps.setInt(4, 0);
            ps.setInt(5,1);
            ps.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public String getUsername(UUID uuid){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Username FROM players WHERE UUID='%uuid%';".replace("%uuid%",""+uuid.toString()));
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.getString("Username");
        }catch(SQLException e){
            return "";
        }
    }

    public int getWins(UUID uuid){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Wins FROM players WHERE UUID='%uuid%';".replace("%uuid%",""+uuid.toString()));
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.getInt("Wins");
        }catch(SQLException e){
            return 0;
        }
    }

    public int getLosses(UUID uuid){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Losses FROM players WHERE UUID='%uuid%';".replace("%uuid%",""+uuid.toString()));
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.getInt("Losses");
        }catch(SQLException e){
            return 0;
        }
    }

    public boolean getNotificationsEnabled(UUID uuid){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT NotificationsEnabled FROM players WHERE UUID='%uuid%';".replace("%uuid%",""+uuid.toString()));
            ResultSet resultSet = preparedStatement.executeQuery();
            return (resultSet.getInt("NotificationsEnabled") == 1);
        }catch(SQLException e){
            return true;
        }
    }

    public void updateUser(UserDAO user){
        try {
            String sql = "UPDATE players SET Wins='" + user.getWins() + "' WHERE UUID='%uuid%';".replace("%uuid%", user.getUUID().toString());
            String sql2 = "UPDATE players SET Losses='" + user.getLosses() + "' WHERE UUID='%uuid%';".replace("%uuid%", user.getUUID().toString());
            String sql3 = "UPDATE players SET Username='" + user.getName()+ "' WHERE UUID='%uuid%';".replace("%uuid%", user.getUUID().toString());
            String sql4 = "UPDATE players SET NotificationsEnabled='" + (user.hasNotificationsEnabled() ? 1 : 0)+ "' WHERE UUID='%uuid%';".replace("%uuid%", user.getUUID().toString());
            connection.createStatement().executeUpdate(sql);
            connection.createStatement().executeUpdate(sql2);
            connection.createStatement().executeUpdate(sql3);
            connection.createStatement().executeUpdate(sql4);
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }


    public ArrayList<String> getTop10Losses(){
        ArrayList<String> result = new ArrayList<>();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Username, Losses FROM players ORDER BY Losses DESC LIMIT 10;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                result.add(resultSet.getString("Username")  + " "+resultSet.getInt("Losses"));
            }
            resultSet.close();
        }catch(SQLException e){
            System.out.println(e.getLocalizedMessage());
        }
        return result;
    }


    public ArrayList<String> getTop10Wins(){
        ArrayList<String> result = new ArrayList<>();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Username, Wins FROM players ORDER BY Wins DESC LIMIT 10;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                result.add(resultSet.getString("Username")  + " "+resultSet.getInt("Wins"));
            }
            resultSet.close();
        }catch(SQLException e){
            System.out.println(e.getLocalizedMessage());
        }
        return result;
    }

}