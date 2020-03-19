package data;

public class InDoc extends AbstrDoc {

    public InDoc(Integer idInDoc, String inNum, String currNum, String dateDc,
                 String dateIn, String descInDoc, String otherData) {
        this.idInDoc = idInDoc;
        this.inNum = inNum;
        this.currNum = currNum;
        this.dateDc = dateDc;
        this.dateIn = dateIn;
        this.descInDoc = descInDoc;
        this.otherData = otherData;
    }

    public Integer getIdInDoc() {
        return idInDoc;
    }

    public void setIdInDoc(int idInDoc) {
        this.idInDoc = idInDoc;
    }

    public String getInNum() {
        return inNum;
    }

    public void setInNum(String inNum) {
        this.inNum = inNum;
    }

    public String getCurrNum() {
        return currNum;
    }

    public void setCurrNum(String currNum) {
        this.currNum = currNum;
    }

    public String getDateDc() {
        return dateDc;
    }

    public void setDateDc(String dateDc) {
        this.dateDc = dateDc;
    }

    public String getDescInDoc() {
        return descInDoc;
    }

    public void setDescInDoc(String descInDoc) {
        this.descInDoc = descInDoc;
    }

    public String getDateIn() {
        return dateIn;
    }

    public void setDateIn(String dateIn) {
        this.dateIn = dateIn;
    }

    @Override
    public String toString() {
        return "InDoc{" +
                "type='" + type + '\'' +
                ", idInDoc=" + idInDoc +
                ", inNum='" + inNum + '\'' +
                ", currNum='" + currNum + '\'' +
                ", dateDc='" + dateDc + '\'' +
                ", dateIn='" + dateIn + '\'' +
                ", descInDoc='" + descInDoc + '\'' +
                ", otherData='" + otherData + '\'' +
                '}';
    }

    public String getOtherData() {
        return otherData;
    }

    public void setOtherData(String otherData) {
        this.otherData = otherData;
    }

    @Override
    public String getType() {
        return type;
    }

    private final String type = AbstrDoc.INDOC;
    private Integer idInDoc;
    private String inNum;
    private String currNum;
    private String dateDc;
    private String dateIn;
    private String descInDoc;
    private String otherData;
}