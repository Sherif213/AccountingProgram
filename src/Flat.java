public class Flat {
    private String name;
    private double price;
    private int size;
    private byte[] image;

    // Constructor
    public Flat(String name, double price, int size, byte[] image) {
        this.name = name;
        this.price = price;
        this.size = size;
        this.image = image;
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

    public byte[] getImage() {
        return image;
    }
}
