package data;

public class Abonent {

    public Abonent(Integer idAbonent, String nameAbonent, String descAbonent) {
        this.idAbonent = idAbonent;
        this.nameAbonent = nameAbonent;
        this.descAbonent = descAbonent;
    }

    public int getIdAbonent() {
        return idAbonent;
    }

    public void setIdAbonent(int idAbonent) {
        this.idAbonent = idAbonent;
    }

    public String getNameAbonent() {
        return nameAbonent;
    }

    public void setNameAbonent(String nameAbonent) {
        this.nameAbonent = nameAbonent;
    }

    public String getDescAbonent() {
        return descAbonent;
    }

    public void setDescAbonent(String descAbonent) {
        this.descAbonent = descAbonent;
    }

    @Override
    public String toString() {
        return getNameAbonent();
    }

    private Integer idAbonent;
    private String nameAbonent;
    private String descAbonent;
}
