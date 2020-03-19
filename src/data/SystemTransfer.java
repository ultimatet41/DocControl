package data;

public class SystemTransfer {

    public SystemTransfer(Integer idSysTransfer, String nameSysTrf, String descSysTrf) {
        this.idSysTransfer = idSysTransfer;
        this.nameSysTrf = nameSysTrf;
        this.descSysTrf = descSysTrf;
    }

    public int getIdSysTransfer() {
        return idSysTransfer;
    }

    public void setIdSysTransfer(int idSysTransfer) {
        this.idSysTransfer = idSysTransfer;
    }

    public String getNameSysTrf() {
        return nameSysTrf;
    }

    public void setNameSysTrf(String nameSysTrf) {
        this.nameSysTrf = nameSysTrf;
    }

    public String getDescSysTrf() {
        return descSysTrf;
    }

    public void setDescSysTrf(String descSysTrf) {
        this.descSysTrf = descSysTrf;
    }

    @Override
    public String toString() {
        return "SystemTransfer{" +
                "idSysTransfer=" + idSysTransfer +
                ", nameSysTrf='" + nameSysTrf + '\'' +
                ", descSysTrf='" + descSysTrf + '\'' +
                '}';
    }

    private Integer idSysTransfer;
    private String nameSysTrf;
    private String descSysTrf;
}
