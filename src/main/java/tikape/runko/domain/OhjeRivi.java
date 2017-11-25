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
public class OhjeRivi {
    private RaakaAine raakaAine;
    private String maara;
    private int rivinumero;
    private int id;
    
    public OhjeRivi(int id, RaakaAine raakaAine, String maara, int rivinumero){
        this.raakaAine = raakaAine;
        this.maara = maara;
        this.rivinumero = rivinumero;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RaakaAine getRaakaAine() {
        return raakaAine;
    }

    public void setRaakaAine(RaakaAine raakaAine) {
        this.raakaAine = raakaAine;
    }


    public String getMaara() {
        return maara;
    }

    public void setMaara(String maara) {
        this.maara = maara;
    }

    public int getRivinumero() {
        return rivinumero;
    }

    public void setRivinumero(int rivinumero) {
        this.rivinumero = rivinumero;
    }
    
    @Override
    public String toString(){
        return "id = " + this.id + "; raaka-aine = " + this.raakaAine.getNimi() + "; maara=" + this.getMaara() + "; rivinumero= "+ this.getRivinumero();
    }
    
    
}
