import java.io.*;

public class Basket implements Serializable {
    private final Product[] products = new Product[3];
    private static final File fileSerial = new File("src/Files", "basket.bin");

    public Basket(Product[] products) {
        this.products[0] = products[0];
        this.products[1] = products[1];
        this.products[2] = products[2];
    }

    public void addToCartSerial(int productNum, int amount) {
        products[productNum].addAmount(amount);
        saveBin(fileSerial);
    }

    public void printCart() {
        int fullSum = 0;
        boolean checkFullSum = false;
        for (Product product : products) {
            fullSum += product.getPrice() * product.getAmount();
            System.out.println(product.getTitle() + ", " + product.getAmount() + " штук, "
                    + product.getPrice() + " рублей за штуку," + " сумма " + product.getPrice()
                    * product.getAmount());
            checkFullSum = true;
        }
        if (checkFullSum) {
            System.out.println("Полная сумма " + fullSum);
        } else {
            System.out.println("Карзина пуста");
        }
    }

    public void saveBin(File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            fos.close();
            oos.close();
        } catch (Exception e) {
            System.err.println("ERROR");
            System.out.println(e.getMessage());
        }
    }

    public static Basket loadFromBinFile(File file, Product[] products) {
        Basket basket = new Basket(products);
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            basket = (Basket) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return basket;
    }

    public Product getProduct(int index) {
        return products[index];
    }
}
