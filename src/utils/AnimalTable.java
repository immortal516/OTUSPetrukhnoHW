package utils;

public class AnimalTable {
    private final ConnectionManager cm;

    public AnimalTable(ConnectionManager cm) {
        this.cm = cm;
    }

    public void create() {
        String sql = "CREATE TABLE IF NOT EXISTS animals_otus_petrukhno (" +
                "id SERIAL PRIMARY KEY," +
                "name VARCHAR(255)," +
                "age INT," +
                "weight INT," +
                "color VARCHAR(50)," +
                "type VARCHAR(50)" +
                ")";
        cm.executeUpdate(sql);
    }
}