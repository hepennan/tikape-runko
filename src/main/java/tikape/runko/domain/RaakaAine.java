/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

/**
 *
 * @author Helena
 */
public class RaakaAine {
    private String nimi;
    private int id;
    private int reseptienMaara;
    
    public RaakaAine(int id, String nimi){
        this.id = id;
        this.nimi = nimi;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReseptienMaara() {
        return reseptienMaara;
    }

    public void setReseptienMaara(int reseptienMaara) {
        this.reseptienMaara = reseptienMaara;
    }
    
    
}
