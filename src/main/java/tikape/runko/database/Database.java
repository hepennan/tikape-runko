package tikape.runko.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseAddress);
    }

    public void init() {
        List<String> lauseet = sqliteLauseet();

        // "try with resources" sulkee resurssin automaattisesti lopuksi
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            // jos tietokantataulu on jo olemassa, ei komentoja suoriteta
            System.out.println("Error >> " + t.getMessage());
        }
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
        lista.add("CREATE TABLE Resepti(\n" +
                    "id integer PRIMARY KEY,\n" +
                    "nimi varchar(200),\n" + 
                    "ohje varchar(3000));");
        lista.add("CREATE TABLE RaakaAine(\n" +
                    "id integer PRIMARY KEY,\n" +
                    "nimi varchar(200));");
        lista.add("CREATE TABLE OhjeRivi(\n" +
                    "id integer PRIMARY KEY,\n"+
                    "raaka_aine_id integer,\n" +
                    "resepti_id integer,\n" +
                    "maara varchar(200),\n" +
                    "rivinumero integer,\n" +
                    "foreign key (raaka_aine_id) references raakaAine(id),\n" +
                    "foreign key (resepti_id) references Resepti(id)\n" +
                    ");");
        lista.add("INSERT INTO Resepti (nimi) VALUES ('PANNUKAKKU');");

        return lista;
    }
}
