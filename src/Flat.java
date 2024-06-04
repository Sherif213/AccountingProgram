public class Flat {
    private String name;
    private double price;
    private int size;

    // Constructor
    public Flat(String name, double price, int size) {
        this.name = name;
        this.price = price;
        this.size = size;
    }

    // Getters
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getSize() {
        return size;
    }
}
