package data;

public class Person {

    public Person(Integer idPerson, String firstName, String lastName, String patronPers, String descPerson) {
        this.idPerson = idPerson;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronPers = patronPers;
        this.descPerson = descPerson;
    }

    public Integer getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(Integer idPerson) {
        this.idPerson = idPerson;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronPers() {
        return patronPers;
    }

    public void setPatronPers(String patronPers) {
        this.patronPers = patronPers;
    }

    public String getDescPerson() {
        return descPerson;
    }

    public void setDescPerson(String descPerson) {
        this.descPerson = descPerson;
    }

    @Override
    public String toString() {
        return "Person{" +
                "idPerson=" + idPerson +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronPers='" + patronPers + '\'' +
                ", descPerson='" + descPerson + '\'' +
                '}';
    }

    private Integer idPerson;
    private String firstName;
    private String lastName;
    private String patronPers;
    private String descPerson;
}
