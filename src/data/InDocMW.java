package data;

import utils.DBControl;

import java.sql.SQLException;

public class InDocMW {
    public InDocMW(InDoc inDoc) {
        this.inDoc = inDoc;
    }

    public String getInNum() {
        return inDoc.getInNum();
    }

    public String getDateDc() {
        return inDoc.getDateDc();
    }

    public String getDescInDoc() {
        return inDoc.getDescInDoc();
    }

    public String getCurrNum() {
        return inDoc.getCurrNum();
    }

    public String getAbonnents() {
        abonnents = "";
        try {
            for (Abonent abonent : DBControl.AbonentLink.getOfDoc(inDoc)) {
                abonnents += abonent.getNameAbonent() +"\n";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return abonnents;
    }

    public void setAbonnents(String abonnents) {
        this.abonnents = abonnents;
    }

    public data.InDoc getInDoc() {
        return inDoc;
    }

    private data.InDoc inDoc;
    private String abonnents = "";
}
