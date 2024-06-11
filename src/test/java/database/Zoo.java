package database;

public class Zoo {
    private int id;
    private String name;

    public Zoo() {
    }

    public Zoo(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Zoo(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Zoo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
