package data;

public class OutDoc extends AbstrDoc {

    public OutDoc(Integer idOutDoc, String numDoc, String dateDc, String descOutDoc, String otherData) {
        this.idOutDoc = idOutDoc;
        this.numDoc = numDoc;
        this.dateDc = dateDc;
        this.descOutDoc = descOutDoc;
        this.otherData = otherData;
    }

    public Integer getIdOutDoc() {
        return idOutDoc;
    }

    public void setIdOutDoc(int idOutDoc) {
        this.idOutDoc = idOutDoc;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getDateDc() {
        return dateDc;
    }

    public void setDateDc(String dateDc) {
        this.dateDc = dateDc;
    }

    public String getDescOutDoc() {
        return descOutDoc;
    }

    public void setDescOutDoc(String descOutDoc) {
        this.descOutDoc = descOutDoc;
    }

    @Override
    public String getType() {
        return type;
    }

    public String getOtherData() {
        return otherData;
    }

    public void setOtherData(String otherData) {
        this.otherData = otherData;
    }

    @Override
    public String toString() {
        return "OutDoc{" +
                "type='" + type + '\'' +
                ", idOutDoc=" + idOutDoc +
                ", numDoc='" + numDoc + '\'' +
                ", dateDc='" + dateDc + '\'' +
                ", descOutDoc='" + descOutDoc + '\'' +
                ", otherData='" + otherData + '\'' +
                '}';
    }

    private final String type = "o";
    private Integer idOutDoc;
    private String numDoc;
    private String dateDc;
    private String descOutDoc;
    private String otherData;
}
