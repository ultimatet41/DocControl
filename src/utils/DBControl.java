package utils;

import data.AbstrDoc;
import data.SystemTransfer;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DBControl {
    public final static String LOCAL_ADDRESS = "jdbc:mysql://localhost/";
    public final static int DATA_EXIST = -1;
    public final static int DATA_IS_NOT_CREATED = -2;
    public static final String START_DATE = "START_DATE";
    public static final String END_DATE = "END_DATE";
    public static final String NUM_DOC = "NUM_DOC";
    public static final String NUM_INDOC = "NUM_INDOC";
    public static final String DESC_DOC = "DESC_DOC";
    public static final String OTHER_DATA_DOC = "OTHER_DATA_DOC";
    public static final String ID_ABONENT = "ID_ABONENT";

    private static Connection connect;

    public static void connectDB(String address, String nameDB,
                                 String userName, String password) throws Exception {
        connect = DriverManager.getConnection(address + "?user=" + userName + "&password=" + password
                + "&useLegacyDatetimeCode=false&serverTimezone=UTC");
        createDBifNotExist(nameDB);
        connect = DriverManager.getConnection(address + nameDB + "?user=" + userName + "&password=" + password
                + "&useLegacyDatetimeCode=false&serverTimezone=UTC");
    }

    public static void disconnectDB() throws SQLException {
        connect.close();
    }

    private static void createDBifNotExist(String nameDB) throws SQLException {
        connect.createStatement().executeUpdate("CREATE DATABASE IF NOT EXISTS " + nameDB + ";");
        Statement statement= connect.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS `" + nameDB +"`.`InDoc` (\n" +
                "  `idInDoc` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `inNum` VARCHAR(45) NULL,\n" +
                "  `currNum` VARCHAR(45) NULL,\n" +
                "  `dateDc` DATE NULL,\n" +
                "   `dateIn` DATE NULL, \n" +
                "  `descInDoc` MEDIUMTEXT NULL,\n" +
                "  `otherData` MEDIUMTEXT NULL,\n" +
                "  PRIMARY KEY (`idInDoc`))\n" +
                "ENGINE = InnoDB;";
        statement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS `" + nameDB + "`.`OutDoc` (\n" +
                "  `idOutDoc` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `numDoc` VARCHAR(45) NULL,\n" +
                "  `dateDc` DATE NULL,\n" +
                "  `descOutDoc` MEDIUMTEXT NULL,\n" +
                "  `otherData` MEDIUMTEXT NULL,\n" +
                "  PRIMARY KEY (`idOutDoc`))\n" +
                "ENGINE = InnoDB;";
        statement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS `" + nameDB + "`.`Person` (\n" +
                "  `idPerson` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `fname` VARCHAR(45) NULL,\n" +
                "  `lname` VARCHAR(45) NULL,\n" +
                "  `patron` VARCHAR(45) NULL,\n" +
                "  `descPerson` VARCHAR(45) NULL,\n" +
                "  PRIMARY KEY (`idPerson`))\n" +
                "ENGINE = InnoDB;";
        statement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS `" + nameDB + "`.`SystemTransfer` (\n" +
                "  `idSysTransfer` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `name` VARCHAR(150) NULL,\n" +
                "  `descSysTrf` MEDIUMTEXT NULL,\n" +
                "  PRIMARY KEY (`idSysTransfer`))\n" +
                "ENGINE = InnoDB;";
        statement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS `" + nameDB + "`.`Abonent` (\n" +
                "  `idAbonent` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `name` VARCHAR(150) NULL,\n" +
                "  `descAbonent` MEDIUMTEXT NULL,\n" +
                "  PRIMARY KEY (`idAbonent`))\n" +
                "ENGINE = InnoDB;";
        statement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS `" +nameDB + "`.`DocLink` (\n" +
                "  `idDocLink` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `idDocOf` INT NULL,\n" +
                "  `idDocIn` INT NULL,\n" +
                "  `typeDoc` ENUM(\"i\", \"o\") NULL,\n" +
                "  `dateDc` DATE NULL,\n" +
                "  PRIMARY KEY (`idDocLink`))\n" +
                "ENGINE = InnoDB;";
        statement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS `" +nameDB + "`.`PersonLink` (\n" +
                "  `idPersonLink` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `idDoc` INT NULL,\n" +
                "  `typeDoc` ENUM(\"i\", \"o\") NULL,\n" +
                "  `dateDc` DATE NULL,\n" +
                "  `idPerson` INT NULL,\n" +
                "  PRIMARY KEY (`idPersonLink`))\n" +
                "ENGINE = InnoDB;";
        statement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS `" + nameDB + "`.`SysTrfLink` (\n" +
                "  `idSysTrfLink` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `idDoc` INT NULL,\n" +
                "  `typeDoc` ENUM(\"i\", \"o\") NULL,\n" +
                "  `dateDc` DATE NULL,\n" +
                "  `idSysTrf` INT NULL,\n" +
                "  PRIMARY KEY (`idSysTrfLink`))\n" +
                "ENGINE = InnoDB;";
        statement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS `" + nameDB + "`.`AbonentLink` (\n" +
                "  `idAbonentLink` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `idDoc` INT NULL,\n" +
                "  `typeDoc` ENUM(\"i\", \"o\") NULL,\n" +
                "  `dateDc` DATE NULL,\n" +
                "  `idAbonent` INT NULL,\n" +
                "  PRIMARY KEY (`idAbonentLink`))\n" +
                "ENGINE = InnoDB;";
        statement.execute(sql);

    }

    public static class InDoc {
        public static int add(data.InDoc inDoc) throws SQLException {
            String sql = "INSERT INTO InDoc (inNum, currNum, dateDc, dateIn, descInDoc, otherData) VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement statement = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, inDoc.getInNum());
            statement.setString(2, inDoc.getCurrNum());
            statement.setString(3, inDoc.getDateDc());
            statement.setString(4, inDoc.getDateIn());
            statement.setString(5, inDoc.getDescInDoc());
            statement.setString(6, inDoc.getOtherData());
            Integer numero = statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            int newId = DATA_IS_NOT_CREATED;
            if (rs.next()){
                newId = rs.getInt(1);
            }
            System.out.println(newId);
            return newId;
        }

        public static int update(data.InDoc inDoc) throws SQLException {
            String sql = "UPDATE InDoc SET inNum = ?, currNum = ?, dateDc = ?, dateIn = ?, descInDoc = ?, otherData= ? WHERE idInDoc = " + inDoc.getIdInDoc() + ";";
            System.out.println(sql);
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setString(1, inDoc.getInNum());
            statement.setString(2, inDoc.getCurrNum());
            statement.setString(3, inDoc.getDateDc());
            statement.setString(4, inDoc.getDateIn());
            statement.setString(5, inDoc.getDescInDoc());
            statement.setString(6, inDoc.getOtherData());
            statement.execute();
            return inDoc.getIdInDoc();
        }

        public static ArrayList<data.InDoc> getFromDate(String startDate, String endDate) throws SQLException {
            String sql = "SELECT * FROM InDoc WHERE dateDc BETWEEN '" + startDate + "' AND '" + endDate + "' " +
                    "ORDER BY currNum;";
            ResultSet set = connect.createStatement().executeQuery(sql);
            ArrayList<data.InDoc> inDocs = new ArrayList<>();
            while (set.next()) {
                Integer idDocIn = set.getInt("idInDoc");
                String inNum = set.getString("inNum");
                String currNum = set.getString("currNum");
                String dateDc = set.getDate("dateDc").toString();
                String dateIn = set.getDate("dateIn").toString();
                String descInDoc = set.getString("descInDoc");
                String otherData = set.getString("otherData");
                inDocs.add(new data.InDoc(idDocIn, inNum, currNum, dateDc, dateIn, descInDoc, otherData));
            }
            return inDocs;
        }

        public static data.InDoc getFromID(Integer idIndDoc) throws SQLException {
            String sql = "SELECT * FROM InDoc WHERE idInDoc = " + idIndDoc + ";";
            ResultSet set = connect.createStatement().executeQuery(sql);
            if (set.next()) {
                Integer idDocIn = set.getInt("idInDoc");
                String inNum = set.getString("inNum");
                String currNum = set.getString("currNum");
                String dateDc = set.getDate("dateDc").toString();
                String dateIn = set.getDate("dateIn").toString();
                String descInDoc = set.getString("descInDoc");
                String otherData = set.getString("otherData");
                return new data.InDoc(idDocIn, inNum, currNum, dateDc, dateIn, descInDoc, otherData);
            }
            return null;
        }

        public static data.InDoc getFromInNumAndDate(String inNum, String date) throws SQLException {
            String sql = "SELECT * FROM InDoc WHERE inNum = '" + inNum + "' AND dateDc = '" + date +"';";
            ResultSet set = connect.createStatement().executeQuery(sql);
            set.next();
            Integer idDocIn = set.getInt("idInDoc");
            String inNum_m = set.getString("inNum");
            String currNum = set.getString("currNum");
            String dateDc = set.getDate("dateDc").toString();
            String dateIn = set.getDate("dateIn").toString();
            String descInDoc = set.getString("descInDoc");
            String otherData = set.getString("otherData");
            return new data.InDoc(idDocIn, inNum_m, currNum, dateDc, dateIn, descInDoc, otherData);
        }

        public static ArrayList<data.InDoc> findDocs(HashMap<String, String> param) throws SQLException {
            String sql;
            if (param.containsKey(ID_ABONENT)) {
                sql = "SELECT * FROM InDoc i LEFT JOIN AbonentLink al ON al.idDoc = i.idInDoc " +
                        " WHERE al.idAbonent = " + param.get(ID_ABONENT) + " AND al.typeDoc = 'i'";
                if (param.size() > 1) sql += " AND ";
                else sql += ";";
                param.remove(ID_ABONENT);
            }
            else sql = "SELECT * FROM InDoc i WHERE ";
            int s = param.size();
            for (HashMap.Entry<String, String> entry : param.entrySet()) {
                switch (entry.getKey()) {
                    case START_DATE: sql = sql + "i.dateDc >= '" + entry.getValue() + "'";
                        if (s > 1) sql += " AND ";
                        break;

                    case END_DATE: sql += "i.dateDc <= '" + entry.getValue() + "'";
                        if (s > 1) sql += " AND ";
                        break;

                    case NUM_DOC: sql += "i.currNum LIKE '%" + entry.getValue() +"%'";
                        if (s > 1) sql += " AND ";
                        break;

                    case NUM_INDOC: sql += "i.inNum LIKE '%" + entry.getValue() + "%'";
                        if (s > 1) sql += " AND ";
                        break;

                    case DESC_DOC:
                        sql += "i.descInDoc LIKE '%" + entry.getValue() +"%'";
                        if (s > 1) sql += " AND ";
                        break;

                    case OTHER_DATA_DOC:
                        sql += "i.otherData LIKE '%" + entry.getValue() + "%'";
                        if (s > 1) sql += " AND ";
                        break;
                }
                --s;
                System.out.println(s + " " + sql);
            }
            sql += ";";
            ResultSet set = connect.createStatement().executeQuery(sql);
            ArrayList<data.InDoc> inDocs = new ArrayList<>();
            while (set.next()) {
                Integer idDocIn = set.getInt("idInDoc");
                String inNum = set.getString("inNum");
                String currNum = set.getString("currNum");
                String dateDc = set.getDate("dateDc").toString();
                String dateIn = set.getDate("dateIn").toString();
                String descInDoc = set.getString("descInDoc");
                String otherData = set.getString("otherData");
                inDocs.add(new data.InDoc(idDocIn, inNum, currNum, dateDc, dateIn, descInDoc, otherData));
            }
            return inDocs;
        }

        public static void delete(data.InDoc inDoc) throws SQLException {
            String sql = "DELETE FROM InDoc WHERE idInDoc = " + inDoc.getIdInDoc() +";";
            connect.createStatement().execute(sql);
        }
    }

    public static class OutDoc {
        public static Integer add(data.OutDoc outDoc) throws SQLException {
            String sql = "INSERT INTO OutDoc (numDoc, dateDc, descOutDoc, otherData) VALUES (?, ?, ?, ?);";
            PreparedStatement statement = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, outDoc.getNumDoc());
            statement.setString(2, outDoc.getDateDc());
            statement.setString(3,outDoc.getDescOutDoc());
            statement.setString(4, outDoc.getOtherData());
            Integer numero = statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            int newId = DATA_IS_NOT_CREATED;
            if (rs.next()){
                newId = rs.getInt(1);
            }
            System.out.println(newId);
            return newId;
        }

        public static int update(data.OutDoc outDoc) throws SQLException {
            String sql = "UPDATE OutDoc SET numDoc = ?, dateDc = ?, descOutDoc = ?, otherData = ? WHERE idOutDoc = ?;";
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setString(1, outDoc.getNumDoc());
            statement.setString(2, outDoc.getDateDc());
            statement.setString(3, outDoc.getDescOutDoc());
            statement.setString(4, outDoc.getOtherData());
            statement.setInt(5, outDoc.getIdOutDoc());
            statement.execute();
            return outDoc.getIdOutDoc();
        }

        public static data.OutDoc getFromID(Integer idOutDoc) throws SQLException {
            String sql = "SELECT * FROM OutDoc WHERE idOutDoc = " + idOutDoc + ";";
            ResultSet set = connect.createStatement().executeQuery(sql);
            set.next();
            Integer id = set.getInt("idOutDoc");
            String numDoc = set.getString("numDoc");
            String date = set.getDate("dateDc").toString();
            String descOutDoc = set.getString("descOutDoc");
            String otherData = set.getString("otherData");
            return new data.OutDoc(id, numDoc, date, descOutDoc, otherData);
        }

        public static ArrayList<data.OutDoc> getFromDate(String startDate, String endDate) throws SQLException {
            String sql = "SELECT * FROM OutDoc WHERE dateDc BETWEEN '" + startDate + "' AND '" + endDate + " " +
                    "ORDER BY numDoc';";
            ResultSet set= connect.createStatement().executeQuery(sql);
            ArrayList<data.OutDoc> outDocs = new ArrayList<>();
            while (set.next()) {
                Integer id = set.getInt("idOutDoc");
                String numDoc = set.getString("numDoc");
                String date = set.getDate("dateDc").toString();
                String descOutDoc = set.getString("descOutDoc");
                String otherData = set.getString("otherData");
                outDocs.add(new data.OutDoc(id, numDoc, date, descOutDoc, otherData));
            }
            return outDocs;
        }

        public static ArrayList<data.OutDoc> findDocs(HashMap<String, String> param) throws SQLException {
            String sql;
            if (param.containsKey(ID_ABONENT)) {
                sql = "SELECT * FROM OutDoc o LEFT JOIN AbonentLink al ON al.idDoc = o.idOutDoc " +
                        " WHERE al.idAbonent = " + param.get(ID_ABONENT) + " AND al.typeDoc = 'o'";
                if (param.size() > 1) sql += " AND ";
                else sql += ";";
                param.remove(ID_ABONENT);
            }
            else sql = "SELECT * FROM OutDoc o WHERE ";

            int s = param.size();
            for (HashMap.Entry<String, String> entry : param.entrySet()) {
                switch (entry.getKey()) {
                    case START_DATE:
                        sql = sql + "o.dateDc >= '" + entry.getValue() + "'";
                        if (s > 1) sql += " AND ";
                        break;

                    case END_DATE:
                        sql += "o.dateDc <= '" + entry.getValue() + "'";
                        if (s > 1) sql += " AND ";
                        break;

                    case NUM_DOC:
                        sql += "o.numDoc LIKE '%" + entry.getValue() + "%'";
                        if (s > 1) sql += " AND ";
                        break;

                    case DESC_DOC:
                        sql += "o.descOutDoc LIKE '%" + entry.getValue() + "%'";
                        if (s > 1) sql += " AND ";
                        break;

                    case OTHER_DATA_DOC:
                        sql += "o.otherData LIKE '%" + entry.getValue() + "%'";
                        if (s > 1) sql += " AND ";
                        break;
                }
                --s;
                System.out.println(s + " " + sql);
            }
            sql += ";";
            ResultSet set= connect.createStatement().executeQuery(sql);
            ArrayList<data.OutDoc> outDocs = new ArrayList<>();
            while (set.next()) {
                Integer id = set.getInt("idOutDoc");
                String numDoc = set.getString("numDoc");
                String date = set.getDate("dateDc").toString();
                String descOutDoc = set.getString("descOutDoc");
                String otherData = set.getString("otherData");
                outDocs.add(new data.OutDoc(id, numDoc, date, descOutDoc, otherData));
            }
            return outDocs;
        }

        public static void delete(data.OutDoc outDoc) throws SQLException {
            String sql = "DELETE FROM OutDoc WHERE idOutDoc = " + outDoc.getIdOutDoc() +";";
            connect.createStatement().execute(sql);
        }
    }

    public static class Abonent {
        public static Integer add(data.Abonent abonent) throws SQLException {
            String sql = "INSERT INTO Abonent (name, descAbonent) VALUES (?, ?);";
            PreparedStatement statement = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, abonent.getNameAbonent());
            statement.setString(2, abonent.getDescAbonent());
            Integer numero = statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            int newId = DATA_IS_NOT_CREATED;
            if (rs.next()){
                newId = rs.getInt(1);
            }
            System.out.println(newId);
            return newId;
        }

        public static void update(data.Abonent abonent) throws SQLException {
            String sql = "UPDATE Abonent SET name = ?, descAbonent = ? WHERE idAbonent = ?;";
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setString(1, abonent.getNameAbonent());
            statement.setString(2, abonent.getDescAbonent());
            statement.setInt(3, abonent.getIdAbonent());
            statement.execute();
        }

        public static data.Abonent getFromID(Integer idAbonent) throws SQLException {
            String sql = "SELECT * FROM Abonent WHERE idAbonent = " + idAbonent + ";";
            ResultSet set = connect.createStatement().executeQuery(sql);
            if (set.next()) {
                Integer id = set.getInt("idAbonent");
                String name = set.getString("name");
                String descAbonent = set.getString("descAbonent");
                return new data.Abonent(id, name, descAbonent);
            }
            return null;
        }

        public static ArrayList<data.Abonent> getAll() throws SQLException {
            String sql = "SELECT * FROM Abonent;";
            ResultSet set = connect.createStatement().executeQuery(sql);
            ArrayList<data.Abonent> abonents = new ArrayList<>();
            while (set.next()) {
                Integer id = set.getInt("idAbonent");
                String name = set.getString("name");
                String descAbonent = set.getString("descAbonent");
                abonents.add(new data.Abonent(id, name, descAbonent));
            }
            return abonents;
        }

        public static ArrayList<data.Abonent> findForName(String inName) throws SQLException {
            String sql = "SELECT * FROM Abonent WHERE name LIKE '%" + inName + "%';";
            ResultSet set = connect.createStatement().executeQuery(sql);
            ArrayList<data.Abonent> abonents = new ArrayList<>();
            while (set.next()) {
                Integer id = set.getInt("idAbonent");
                String name = set.getString("name");
                String descAbonent = set.getString("descAbonent");
                abonents.add(new data.Abonent(id, name, descAbonent));
            }
            return abonents;
        }
    }

    public static class Person {
        public static Integer add(data.Person person) throws SQLException {
            String sql = "INSERT INTO Person (fname, lname, patron, descPerson) VALUES (?, ?, ?, ?);";
            PreparedStatement statement = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, person.getFirstName());
            statement.setString(2, person.getLastName());
            statement.setString(3, person.getPatronPers());
            statement.setString(4, person.getDescPerson());
            Integer numero = statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            int newId = DATA_IS_NOT_CREATED;
            if (rs.next()){
                newId = rs.getInt(1);
            }
            System.out.println(newId);
            return newId;
        }

        public static void update(data.Person person) throws SQLException {
            String sql = "UPDATE Person SET fname = ?, lname = ?, patron = ?, descPerson = ? WHERE idPerson = ?;";
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setString(1, person.getFirstName());
            statement.setString(2, person.getLastName());
            statement.setString(3, person.getPatronPers());
            statement.setString(4, person.getDescPerson());
            statement.setInt(5, person.getIdPerson());
            statement.execute();
        }

        public static data.Person getFromID(Integer idPerson) throws SQLException {
            String sql = "SELECT * FROM Person WHERE idPerson = " + idPerson + ";";
            ResultSet set = connect.createStatement().executeQuery(sql);
            if (set.next()) {
                Integer id = set.getInt("idPerson");
                String fname = set.getString("fname");
                String lname = set.getString("lname");
                String patron = set.getString("patron");
                String desc = set.getString("descPerson");
                return new data.Person(id, fname, lname, patron, desc);
            }
            return null;
        }

        public static ArrayList<data.Person> getAll() throws SQLException {
            String sql = "SELECT * FROM Person;";
            ResultSet set = connect.createStatement().executeQuery(sql);
            ArrayList<data.Person> people = new ArrayList<>();
            while (set.next()) {
                Integer id = set.getInt("idPerson");
                String fname = set.getString("fname");
                String lname = set.getString("lname");
                String patron = set.getString("patron");
                String desc = set.getString("descPerson");
                people.add(new data.Person(id, fname, lname, patron, desc));
            }
            return people;
        }

        public static ArrayList<data.Person> findForLName(String lName) throws SQLException {
            String sql = "SELECT * FROM Person WHERE lname LIKE '%" + lName + "%';";
            ResultSet set = connect.createStatement().executeQuery(sql);
            ArrayList<data.Person> people = new ArrayList<>();
            while (set.next()) {
                Integer id = set.getInt("idPerson");
                String fname = set.getString("fname");
                String lname = set.getString("lname");
                String patron = set.getString("patron");
                String desc = set.getString("descPerson");
                people.add(new data.Person(id, fname, lname, patron, desc));
            }
            return people;
        }
    }

    public static class SysTrf {
        public static Integer add(data.SystemTransfer transfer) throws SQLException {
            String sql = "INSERT INTO SystemTransfer  (name, descSysTrf) VALUES (?, ?);";
            PreparedStatement statement = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, transfer.getNameSysTrf());
            statement.setString(2, transfer.getDescSysTrf());
            Integer numero = statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            int newId = DATA_IS_NOT_CREATED;
            if (rs.next()){
                newId = rs.getInt(1);
            }
            System.out.println(newId);
            return newId;
        }

        public static void update(SystemTransfer transfer) throws SQLException {
            String sql = "UPDATE SystemTransfer SET name = ?, descSysTrf = ? WHERE idSysTransfer = ?;";
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setString(1, transfer.getNameSysTrf());
            statement.setString(2, transfer.getDescSysTrf());
            statement.setInt(3, transfer.getIdSysTransfer());
            statement.execute();
        }

        public static SystemTransfer getFromID(Integer idSystemTransfer) throws SQLException {
            String sql = "SELECT * FROM SystemTransfer WHERE idSysTransfer = " + idSystemTransfer + ";";
            ResultSet set = connect.createStatement().executeQuery(sql);
            if (set.next()) {
                Integer id = set.getInt("idSysTransfer");
                String name = set.getString("name");
                String desc = set.getString("descSysTrf");
                return new SystemTransfer(id, name, desc);
            }
            return null;
        }

        public static ArrayList<SystemTransfer> getAll() throws SQLException {
            String sql = "SELECT * FROM SystemTransfer";
            ResultSet set = connect.createStatement().executeQuery(sql);
            ArrayList<SystemTransfer> transfers = new ArrayList<>();
            while (set.next()) {
                Integer id = set.getInt("idSysTransfer");
                String name = set.getString("name");
                String desc = set.getString("descSysTrf");
                transfers.add(new SystemTransfer(id, name, desc));
            }
            return transfers;
        }

        public static ArrayList<SystemTransfer> findForName(String inName) throws SQLException {
            String sql = "SELECT * FROM SystemTransfer WHERE name LIKE '%" + inName + "%';";
            ResultSet set = connect.createStatement().executeQuery(sql);
            ArrayList<SystemTransfer> transfers = new ArrayList<>();
            while (set.next()) {
                Integer id = set.getInt("idSysTransfer");
                String name = set.getString("name");
                String descSysTrf = set.getString("descSysTrf");
                transfers.add(new SystemTransfer(id, name, descSysTrf));
            }
            return transfers;
        }
    }

    public static class DocLink {
        public static Integer add(AbstrDoc docOut, AbstrDoc docIn) throws SQLException {
            if (isExist(docOut, docIn)) return DATA_EXIST;
            String sql = "INSERT INTO DocLink (idDocOf, idDocIn, typeDoc, dateDc) VALUES (?, ?, ?, ?);";
            PreparedStatement statement = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if (docOut.getType().equals(AbstrDoc.INDOC)) {
                data.InDoc  inDoc = (data.InDoc) docOut;
                data.OutDoc outDoc = (data.OutDoc) docIn;
                statement.setInt(1, inDoc.getIdInDoc());
                statement.setInt(2, outDoc.getIdOutDoc());
                statement.setString(3, docOut.getType());
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date dateobj = new java.util.Date();
                System.out.println(df.format(dateobj));
                statement.setString(4, df.format(dateobj));
                Integer numero = statement.executeUpdate();
                ResultSet rs = statement.getGeneratedKeys();
                int newId = DATA_IS_NOT_CREATED;
                if (rs.next()){
                    newId = rs.getInt(1);
                }
                System.out.println(newId);
                return newId;
            }
            else {
                data.OutDoc outDoc = (data.OutDoc) docOut;
                data.InDoc inDoc = (data.InDoc) docIn;
                statement.setInt(1, outDoc.getIdOutDoc());
                statement.setInt(2, inDoc.getIdInDoc());
                statement.setString(3, docOut.getType());
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date dateobj = new java.util.Date();
                statement.setString(4, df.format(dateobj));
                statement.execute();
                sql = "SELECT * FROM DocLink WHERE idDocOf = " + outDoc.getIdOutDoc() + " AND " +
                        "idDocIn = " + inDoc.getIdInDoc() + " AND " +
                        "typeDoc = '" + docOut.getType() +"';";
                ResultSet set = connect.createStatement().executeQuery(sql);
                set.next();
                return set.getInt("idDocLink");
            }
        }

        public static boolean isExist (AbstrDoc docOf, AbstrDoc docIn) throws SQLException {
            if (docOf.getType().equals(AbstrDoc.INDOC)) {
                data.InDoc  inDoc = (data.InDoc) docOf;
                data.OutDoc outDoc = (data.OutDoc) docIn;
                String sql = "SELECT * FROM DocLink WHERE idDocOf = " + inDoc.getIdInDoc() + " AND " +
                        "idDocIn = " + outDoc.getIdOutDoc() + " AND " +
                        "typeDoc = '" + inDoc.getType() +"';";
                ResultSet set = connect.createStatement().executeQuery(sql);
                return set.next();
            }
            else {
                data.OutDoc outDoc = (data.OutDoc) docOf;
                data.InDoc inDoc = (data.InDoc) docIn;
                String sql = "SELECT * FROM DocLink WHERE idDocOf = " + outDoc.getIdOutDoc() + " AND " +
                        "idDocIn = " + inDoc.getIdInDoc() + " AND " +
                        "typeDoc = '" + docOf.getType() +"';";
                ResultSet set = connect.createStatement().executeQuery(sql);
                return set.next();
            }
        }

        public static ArrayList<AbstrDoc> getOutDocs(AbstrDoc docOf) throws SQLException {
            String sql;
            ArrayList<AbstrDoc> docs = new ArrayList<>();
            if (docOf.getType().equals(AbstrDoc.INDOC)) {
                data.InDoc inDoc = (data.InDoc) docOf;
                sql = "SELECT * FROM DocLink WHERE idDocOf = " + inDoc.getIdInDoc() + " AND " +
                        "typeDoc = '" + docOf.getType() +"';";
                ResultSet set = connect.createStatement().executeQuery(sql);
                while (set.next()) {
                    docs.add(DBControl.OutDoc.getFromID(set.getInt("idDocIn")));
                }
            }
            else {
                data.OutDoc outDoc = (data.OutDoc) docOf;
                sql = "SELECT * FROM DocLink WHERE idDocOf = " + outDoc.getIdOutDoc() + " AND " +
                        "typeDoc = '" + docOf.getType() +"';";
                ResultSet set = connect.createStatement().executeQuery(sql);
                while (set.next()) {
                    docs.add(DBControl.InDoc.getFromID(set.getInt("idDocIn")));
                }
            }
            return docs;
        }

        public static ArrayList<AbstrDoc> getInDocs(AbstrDoc docIn) throws SQLException {
            String sql;
            ArrayList<AbstrDoc> docs = new ArrayList<>();
            if (docIn.getType().equals(AbstrDoc.INDOC)) {
                data.InDoc inDoc = (data.InDoc) docIn;
                sql = "SELECT * FROM DocLink WHERE idDocIn = " + inDoc.getIdInDoc() + " AND " +
                        "typeDoc = '" + AbstrDoc.OUTDOC +"';";
                ResultSet set = connect.createStatement().executeQuery(sql);
                while (set.next()) {
                    docs.add(DBControl.OutDoc.getFromID(set.getInt("idDocOf")));
                }
            }
            else {
                data.OutDoc outDoc = (data.OutDoc) docIn;
                sql = "SELECT * FROM DocLink WHERE idDocIn = " + outDoc.getIdOutDoc() + " AND " +
                        "typeDoc = '" + AbstrDoc.INDOC +"';";
                ResultSet set = connect.createStatement().executeQuery(sql);
                while (set.next()) {
                    docs.add(DBControl.InDoc.getFromID(set.getInt("idDocOf")));
                }
            }
            return docs;
        }

        public static void delete(AbstrDoc docOf, AbstrDoc docIn) throws SQLException {
            if (docOf.getType().equals(AbstrDoc.INDOC)) {
                data.InDoc inDoc = (data.InDoc) docOf;
                data.OutDoc outDoc = (data.OutDoc) docIn;
                String sql = "DELETE FROM DocLink WHERE idDocOf = " + inDoc.getIdInDoc() + " AND " +
                        "idDocIn = " + outDoc.getIdOutDoc() + " AND " +
                        "typeDoc = '" + inDoc.getType() +"';";
                connect.createStatement().execute(sql);
            }
            else {
                data.OutDoc outDoc = (data.OutDoc) docOf;
                data.InDoc inDoc = (data.InDoc) docIn;
                String sql = "DELETE FROM DocLink WHERE idDocOf = " + outDoc.getIdOutDoc() + " AND " +
                        "idDocIn = " + inDoc.getIdInDoc() + " AND " +
                        "typeDoc = '" + docOf.getType() +"';";
                connect.createStatement().execute(sql);
            }
        }

    }

    public static class AbonentLink {
        public static Integer add(AbstrDoc doc, data.Abonent abonent) throws SQLException {
            if (isExist(doc,abonent)) return DATA_EXIST;
            String sql = "INSERT INTO AbonentLink (idDoc, typeDoc, dateDc, idAbonent) VALUES (?, ?, ?, ?);";
            PreparedStatement statement = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date dateobj = new java.util.Date();
            if(doc.getType().equals(AbstrDoc.INDOC)) {
                data.InDoc inDoc = (data.InDoc) doc;

                statement.setInt(1, inDoc.getIdInDoc());
                statement.setString(2, inDoc.getType());
                statement.setString(3, df.format(dateobj));
                statement.setInt(4, abonent.getIdAbonent());
                Integer numero = statement.executeUpdate();
                ResultSet rs = statement.getGeneratedKeys();
                int newId = DATA_IS_NOT_CREATED;
                if (rs.next()){
                    newId = rs.getInt(1);
                }
                System.out.println(newId);
                return newId;
            }
            else {
                data.OutDoc outDoc = (data.OutDoc) doc;
                statement.setInt(1, outDoc.getIdOutDoc());
                statement.setString(2, outDoc.getType());
                statement.setString(3, df.format(dateobj));
                statement.setInt(4, abonent.getIdAbonent());
                statement.execute();
                sql = "SELECT * FROM AbonentLink WHERE idDoc = " + outDoc.getIdOutDoc() +
                        " AND typeDoc = '" + outDoc.getType() +"'" +
                        " AND idAbonent = " + abonent.getIdAbonent() + ";";
                ResultSet set = connect.createStatement().executeQuery(sql);
                set.next();
                return set.getInt("idAbonentLink");
            }
        }

        public static boolean isExist(AbstrDoc doc, data.Abonent abonent) throws SQLException {
            if (doc.getType().equals(AbstrDoc.INDOC)) {
                data.InDoc inDoc = (data.InDoc) doc;
                String sql = "SELECT * FROM AbonentLink WHERE idDoc = " + inDoc.getIdInDoc()
                        + " AND typeDoc = '" + inDoc.getType() + "'"
                        + " AND idAbonent = " + abonent.getIdAbonent() + ";";
                ResultSet set = connect.createStatement().executeQuery(sql);
                return set.next();
            }
            else {
                data.OutDoc outDoc = (data.OutDoc) doc;
                String sql = "SELECT * FROM AbonentLink WHERE idDoc = " + outDoc.getIdOutDoc()
                        + " AND typeDoc = '" + outDoc.getType() + "'"
                        + " AND idAbonent = " + abonent.getIdAbonent() + ";";
                ResultSet set = connect.createStatement().executeQuery(sql);
                return set.next();
            }
        }

        public static ArrayList<data.Abonent> getOfDoc(AbstrDoc doc) throws SQLException {
            ArrayList<data.Abonent> abonents = new ArrayList<>();
            if (doc.getType().equals(AbstrDoc.INDOC)) {
                data.InDoc inDoc = (data.InDoc) doc;
                String sql = "SELECT * FROM AbonentLink WHERE idDoc = " + inDoc.getIdInDoc()
                        + " AND typeDoc = '" + inDoc.getType() + "';";
                ResultSet set = connect.createStatement().executeQuery(sql);
                while (set.next()) {
                    abonents.add(DBControl.Abonent.getFromID(set.getInt("idAbonent")));
                }
            }
            else {
                data.OutDoc outDoc = (data.OutDoc) doc;
                String sql = "SELECT * FROM AbonentLink WHERE idDoc = " + outDoc.getIdOutDoc()
                        + " AND typeDoc = '" + outDoc.getType() + "';";
                ResultSet set = connect.createStatement().executeQuery(sql);
                while (set.next()) {
                    abonents.add(DBControl.Abonent.getFromID(set.getInt("idAbonent")));
                }
            }
            return abonents;
        }

        public static void delete(AbstrDoc doc, data.Abonent abonent) throws SQLException {
            if (doc.getType().equals(AbstrDoc.INDOC)) {
                data.InDoc inDoc = (data.InDoc) doc;
                String sql = "DELETE FROM AbonentLink WHERE idDoc = " + inDoc.getIdInDoc()
                        + " AND typeDoc = '" + inDoc.getType() + "'"
                        + " AND idAbonent = " + abonent.getIdAbonent() + ";";
                connect.createStatement().execute(sql);
            }
            else {
                data.OutDoc outDoc = (data.OutDoc) doc;
                String sql = "DELETE FROM AbonentLink WHERE idDoc = " + outDoc.getIdOutDoc()
                        + " AND typeDoc = '" + outDoc.getType() + "'"
                        + " AND idAbonent = " + abonent.getIdAbonent() + ";";
                connect.createStatement().execute(sql);
            }
        }
    }

    public static class PersonLink {
        public static Integer add(AbstrDoc doc, data.Person person) throws SQLException {
            if (isExist(doc, person)) return DATA_EXIST;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date dateobj = new java.util.Date();
            String sql = "INSERT INTO PersonLink (idDoc, typeDoc, dateDc, idPerson) VALUES (?, ?, ?, ?);";
            PreparedStatement statement = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if (doc.getType().equals(AbstrDoc.INDOC)) {
                data.InDoc inDoc = (data.InDoc) doc;
                statement.setInt(1, inDoc.getIdInDoc());
                statement.setString(2, inDoc.getType());
                statement.setString(3, df.format(dateobj));
                statement.setInt(4, person.getIdPerson());
                Integer numero = statement.executeUpdate();
                ResultSet rs = statement.getGeneratedKeys();
                int newId = DATA_IS_NOT_CREATED;
                if (rs.next()){
                    newId = rs.getInt(1);
                }
                System.out.println(newId);
                return newId;
            }
            else {
                data.OutDoc outDoc = (data.OutDoc) doc;
                statement.setInt(1, outDoc.getIdOutDoc());
                statement.setString(2, outDoc.getType());
                statement.setString(3, df.format(dateobj));
                statement.setInt(4, person.getIdPerson());
                statement.execute();
                sql = "SELECT * FROM PersonLink WHERE idDoc = " + outDoc.getIdOutDoc()
                        + " AND typeDoc = '" + outDoc.getType() + "'"
                        + " AND idPerson = " + person.getIdPerson() + ";";
                ResultSet set = connect.createStatement().executeQuery(sql);
                set.next();
                return set.getInt("idPersonLink");
            }
        }

        public static boolean isExist(AbstrDoc doc, data.Person person) throws SQLException {
            String sql;
            if(doc.getType().equals(AbstrDoc.INDOC)) {
                data.InDoc inDoc = (data.InDoc) doc;
                sql = "SELECT * FROM PersonLink WHERE idDoc = " + inDoc.getIdInDoc()
                        + " AND typeDoc = '" + inDoc.getType() + "'"
                        + " AND idPerson = " + person.getIdPerson() + ";";
                ResultSet set = connect.createStatement().executeQuery(sql);
                return set.next();
            }
            else {
                data.OutDoc outDoc = (data.OutDoc) doc;
                sql = "SELECT * FROM PersonLink WHERE idDoc = " + outDoc.getIdOutDoc()
                        + " AND typeDoc = '" + outDoc.getType() + "'"
                        + " AND idPerson = " + person.getIdPerson() + ";";
                ResultSet set = connect.createStatement().executeQuery(sql);
                return set.next();
            }
        }

        public static ArrayList<data.Person> getOfDoc(AbstrDoc doc) throws SQLException {
            String sql;
            ArrayList<data.Person> people = new ArrayList<>();
            if (doc.getType().equals(AbstrDoc.INDOC)) {
                data.InDoc inDoc = (data.InDoc) doc;
                sql = "SELECT * FROM PersonLink WHERE idDoc = " + inDoc.getIdInDoc()
                        + " AND typeDoc = '" + inDoc.getType() + "';";
                ResultSet set = connect.createStatement().executeQuery(sql);
                while (set.next()) {
                    people.add(DBControl.Person.getFromID(set.getInt("idPerson")));
                }
            }
            else {
                data.OutDoc outDoc = (data.OutDoc) doc;
                sql = "SELECT * FROM PersonLink WHERE idDoc = " + outDoc.getIdOutDoc()
                        + " AND typeDoc = '" + outDoc.getType() + "';";
                ResultSet set = connect.createStatement().executeQuery(sql);
                while (set.next()) {
                    people.add(DBControl.Person.getFromID(set.getInt("idPerson")));
                }
            }
            return people;
        }

        public static void delete(AbstrDoc doc, data.Person person) throws SQLException {
            String sql;
            if (doc.getType().equals(AbstrDoc.INDOC)) {
                data.InDoc inDoc = (data.InDoc) doc;
                sql = "DELETE FROM PersonLink WHERE idDoc = " + inDoc.getIdInDoc()
                        + " AND typeDoc = '" + inDoc.getType() + "'"
                        + " AND idPerson = " + person.getIdPerson() + ";";
                connect.createStatement().execute(sql);
            }
            else {
                data.OutDoc outDoc = (data.OutDoc) doc;
                sql = "DELETE FROM PersonLink WHERE idDoc = " + outDoc.getIdOutDoc()
                        + " AND typeDoc = '" + outDoc.getType() + "'"
                        + " AND idPerson = " + person.getIdPerson() + ";";
                connect.createStatement().execute(sql);
            }
        }
    }

    public static class SysTrfLink {
        public static Integer add(AbstrDoc doc, SystemTransfer transfer) throws SQLException {
            if (isExist(doc, transfer)) return DATA_EXIST;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date dateobj = new java.util.Date();
            String sql = "INSERT INTO SysTrfLink (idDoc, typeDoc, dateDc, idSysTrf) VALUES (?, ?, ?, ?);";
            PreparedStatement statement = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if (doc.getType().equals(AbstrDoc.INDOC)) {
                data.InDoc inDoc = (data.InDoc) doc;
                statement.setInt(1, inDoc.getIdInDoc());
                statement.setString(2, inDoc.getType());
                statement.setString(3, df.format(dateobj));
                statement.setInt(4, transfer.getIdSysTransfer());
                Integer numero = statement.executeUpdate();
                ResultSet rs = statement.getGeneratedKeys();
                int newId = DATA_IS_NOT_CREATED;
                if (rs.next()){
                    newId = rs.getInt(1);
                }
                System.out.println(newId);
                return newId;
            }
            else {
                data.OutDoc outDoc = (data.OutDoc) doc;
                statement.setInt(1, outDoc.getIdOutDoc());
                statement.setString(2, outDoc.getType());
                statement.setString(3, df.format(dateobj));
                statement.setInt(4, transfer.getIdSysTransfer());
                statement.execute();
                sql = "SELECT * FROM SysTrfLink WHERE idDoc = " + outDoc.getIdOutDoc()
                        + " AND typeDoc = '" + outDoc.getType() +"'"
                        + " AND idSysTrf = " + transfer.getIdSysTransfer();
                ResultSet set = connect.createStatement().executeQuery(sql);
                set.next();
                return set.getInt("idSysTrfLink");
            }
        }

        public static boolean isExist(AbstrDoc doc,SystemTransfer transfer) throws SQLException {
            String sql;
            if (doc.getType().equals(AbstrDoc.INDOC)) {
                data.InDoc inDoc = (data.InDoc) doc;
                sql = "SELECT * FROM SysTrfLink WHERE idDoc = " + inDoc.getIdInDoc()
                        + " AND typeDoc = '" + inDoc.getType() +"'"
                        + " AND idSysTrf = " + transfer.getIdSysTransfer() +";";
                ResultSet set = connect.createStatement().executeQuery(sql);
                return set.next();
            }
            else {
                data.OutDoc outDoc = (data.OutDoc) doc;
                sql = "SELECT * FROM SysTrfLink WHERE idDoc = " + outDoc.getIdOutDoc()
                        + " AND typeDoc = '" + outDoc.getType() +"'"
                        + " AND idSysTrf = " + transfer.getIdSysTransfer() +";";
                ResultSet set = connect.createStatement().executeQuery(sql);
                return set.next();
            }
        }

        public static ArrayList<SystemTransfer> getOfDoc(AbstrDoc doc) throws SQLException {
            String sql;
            ArrayList<SystemTransfer> transfers = new ArrayList<>();
            if (doc.getType().equals(AbstrDoc.INDOC)) {
                data.InDoc inDoc = (data.InDoc) doc;
                sql = "SELECT * FROM SysTrfLink WHERE idDoc = " + inDoc.getIdInDoc()
                        + " AND typeDoc = '" + inDoc.getType() +"';";
                ResultSet set = connect.createStatement().executeQuery(sql);
                while (set.next()) {
                    transfers.add(DBControl.SysTrf.getFromID(set.getInt("idSysTrf")));
                }
            }
            else {
                data.OutDoc outDoc = (data.OutDoc) doc;
                sql = "SELECT * FROM SysTrfLink WHERE idDoc = " + outDoc.getIdOutDoc()
                        + " AND typeDoc = '" + outDoc.getType() +"';";
                ResultSet set = connect.createStatement().executeQuery(sql);
                while (set.next()) {
                    transfers.add(DBControl.SysTrf.getFromID(set.getInt("idSysTrf")));
                }
            }
            return transfers;
        }

        public static void delete(AbstrDoc doc, SystemTransfer transfer) throws SQLException {
            String sql;
            if (doc.getType().equals(AbstrDoc.INDOC)) {
                data.InDoc inDoc = (data.InDoc) doc;
                sql = "DELETE FROM SysTrfLink WHERE idDoc = " + inDoc.getIdInDoc()
                        + " AND typeDoc = '" + inDoc.getType() +"'"
                        + " AND idSysTrf = " + transfer.getIdSysTransfer() +";";
            }
            else {
                data.OutDoc outDoc = (data.OutDoc) doc;
                sql = "DELETE FROM SysTrfLink WHERE idDoc = " + outDoc.getIdOutDoc()
                        + " AND typeDoc = '" + outDoc.getType() +"'"
                        + " AND idSysTrf = " + transfer.getIdSysTransfer() +";";
            }
            connect.createStatement().execute(sql);
        }
    }
}
