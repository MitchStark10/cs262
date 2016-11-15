package edu.calvin.cs262;

import com.google.gson.Gson;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This module implements a RESTful service for the player table of the monopoly database.
 * Only the player relation is supported, not the game or playergame objects.
 * The server requires Java 1.7 (not 1.8).
 * <p>
 * I tested these services using IDEA's REST Client test tool. Run the server and open
 * Tools-TestRESTService and set the appropriate HTTP method, host/port, path and request body and then press
 * the green arrow (submit request).
 *
 * @author kvlinden
 * @version summer, 2016 - upgraded to JSON; added Player POJO; removed unneeded libraries
 */
@Path("/monopoly")
public class MonopolyResource {

    private static final String DB_URI = "jdbc:postgresql://localhost:5432/monopoly";
    private static final String DB_LOGIN_ID = "postgres";
    private static final String DB_PASSWORD = "postgres";

    /**
     * Run this main method to fire up the service.
     *
     * @param args command-line arguments (ignored)
     * @throws IOException if server does not connect
     */
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServerFactory.create("http://localhost:9998/");
        server.start();

        System.out.println("Server running...");
        System.out.println("Web clients should visit: http://localhost:9998/monopoly");
        System.out.println("Android emulators should visit: http://LOCAL_IP_ADDRESS:9998/monopoly");
        System.out.println("Hit return to stop...");
        //noinspection ResultOfMethodCallIgnored
        System.in.read();
        System.out.println("Stopping server...");
        server.stop(0);
        System.out.println("Server stopped...");
    }

    private List retrievePlayers() throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<Player> players = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URI, DB_LOGIN_ID, DB_PASSWORD);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM Player");
            while (rs.next()) {
                players.add(new Player(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
        } catch (SQLException e) {
            throw (e);
        } finally {
            rs.close();
            statement.close();
            connection.close();
        }
        return players;
    }

    /**
     * a hello-world resource
     *
     * @return a simple string value
     */
    @SuppressWarnings("SameReturnValue")
    @GET
    @Path("/hello")
    @Produces("text/plain")
    public String getClichedMessage() {
        return "Hello, Jersey!";
    }

    /**
     * GET method that returns a list of all monopoly players
     *
     * @return a JSON list representation of the player records
     */
    @GET
    @Path("/players")
    @Produces("application/json")
    public String getPlayers() {
        try {
            return new Gson().toJson(retrievePlayers());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
