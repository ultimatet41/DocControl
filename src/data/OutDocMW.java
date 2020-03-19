package data;

import utils.DBControl;

import java.sql.SQLException;

public class OutDocMW {

    public OutDocMW(OutDoc outDoc) {
        this.outDoc = outDoc;
    }

    public String getAbonnents() {
        abonnents = "";
        try {
            for (Abonent abonent : DBControl.AbonentLink.getOfDoc(outDoc)) {
                abonnents += abonent.getNameAbonent() +"\n";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return abonnents;
    }

    public OutDoc getOutDoc() {
        return outDoc;
    }

    public String getNumDoc() {
        return outDoc.getNumDoc();
    }

    public String getDateDc() {
        return outDoc.getDateDc();
    }

    public String getDescOutDoc() {
        return outDoc.getDescOutDoc();
    }

    private data.OutDoc outDoc;
    private String abonnents = "";
}
