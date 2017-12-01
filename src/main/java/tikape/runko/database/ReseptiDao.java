/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import tikape.runko.domain.OhjeRivi;
import tikape.runko.domain.Resepti;

/**
 *
 * @author Helena
 */
public class ReseptiDao implements Dao<Resepti, Integer>{
    
    private Database database;
    private OhjeRiviDao ohjerividao;
    
    public ReseptiDao(Database database){
        this.database = database;
        this.ohjerividao = new OhjeRiviDao(database);
    }

    @Override
    public Resepti findOne(Integer key) throws SQLException {
        try(Connection conn = database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Resepti WHERE id = ?");
            stmt.setInt(1, key);
            ResultSet res  = stmt.executeQuery();
            while(res.next()){
                String nimi = res.getString("nimi");
                int id = res.getInt("id");
                String ohje =res.getString("ohje");
                List<OhjeRivi> ohjerivit = ohjerividao.findAllForResepti(id);
                return new Resepti(id,nimi,ohje,ohjerivit);
            }
        }
        return null;
        
    }

    //palauttaa kaikki reseptit aakkosjärjestyksessä
    @Override
    public List<Resepti> findAll() throws SQLException {
        List<Resepti> reseptit = new ArrayList<>();
        try(Connection conn= database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Resepti ORDER BY nimi ASC");
            ResultSet res = stmt.executeQuery();
            while(res.next()){

                String nimi = res.getString("nimi");
                int id = res.getInt("id");
                String ohje = res.getString("ohje");
                List<OhjeRivi> ohjerivit = this.ohjerividao.findAllForResepti(id);
                reseptit.add(new Resepti(id,nimi,ohje,ohjerivit));                
            }
        }
        
        return reseptit;
    }

    //palauttaa listan resepteistä joissa raaka-aineen kuuluu reseptin aineksiin
    public List<Resepti> findAllForRaakaAine(Integer raakaAineId) throws SQLException {
        List<Resepti> reseptit = new ArrayList<>();
        try(Connection conn= database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("SELECT resepti_id FROM OhjeRivi where raaka_aine_id = ?");
            stmt.setInt(1, raakaAineId);
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                int id = res.getInt("resepti_id");
                reseptit.add(this.findOne(id));                
            }
        }
        Collections.sort(reseptit);

        
        return reseptit;
    }
    
    //kutsuu metodia joka deletoi kaikki reseptiin linkatut ohjerivit ja deletoi reseptin
    @Override
    public void delete(Integer key) throws SQLException {
        try(Connection conn = this.database.getConnection()){
            this.ohjerividao.deleteAllForResepti(key);
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Resepti WHERE id = ?");
            stmt.setInt(1, key);
            stmt.executeUpdate();
        }
    }
    
    //jos reseptiä ei löydy samalla nimellä, lisää uuden reseptin
    public Resepti addNew(String reseptiNimi)throws SQLException{
        Resepti resepti = null;
        try(Connection conn = database.getConnection()){
            resepti = findByNimi(reseptiNimi);
            if(resepti!=null){
                return resepti;
            }
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Resepti(nimi) VALUES(?)");
            stmt.setString(1, reseptiNimi);
            stmt.executeUpdate();
            resepti = findByNimi(reseptiNimi);
         }
        return resepti;
        
    }
    
    //etsii reseptin nimen perusteella
    public Resepti findByNimi(String key) throws SQLException {
        Resepti resepti = null;
        try(Connection conn = database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Resepti WHERE LOWER(nimi) = ?");
            stmt.setString(1, key.toLowerCase());
            ResultSet res  = stmt.executeQuery();
            while(res.next()){
                String nimi = res.getString("nimi");
                int id = res.getInt("id");
                String ohje = res.getString("nimi");
                List<OhjeRivi> ohjerivit = ohjerividao.findAllForResepti(id);
                resepti = new Resepti(id,nimi, ohje, ohjerivit);
            }
        }
        if(resepti == null){
            System.out.println("Resepti not found");
        }
        return resepti;
        
    }
    
    //päivittää reseptin ohjetekstin
    public void paivitaReseptinOhje (int id, String ohje) throws SQLException{
        try(Connection conn = database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("UPDATE Resepti SET ohje = ? where id =?");
            stmt.setString(1, ohje);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }
    
    }
