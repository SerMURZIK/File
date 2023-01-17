import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        Product milk = new Product("Молко", 125, 0);
        Product bread = new Product("Хлеб", 50, 1);
        Product crisps = new Product("Чипсы", 200, 2);
        Product[] products = {milk, bread, crisps};
        File fileSerial = new File("src/Files", "basket.bin");

        Basket basket = new Basket(products);
        try {
            basket = Basket.loadFromBinFile(fileSerial, products);
            for (int i = 0; i < products.length; i++) {
                products[i] = basket.getProduct(i);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        String checkEnd;
        String[] parts;
        int numberOfProduct;
        int amountOfProducts;

        System.out.println("Список товаров:");
        System.out.println();
        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + ": " + products[i].getTitle() + " " + products[i].getPrice()
                    + " рублей за штуку");
        }
        System.out.println();
        while (true) {
            System.out.println();
            System.out.println("Введите номер продукта и количество продуктов через пробел, или \"end\" или \"clean\"");
            checkEnd = scanner.nextLine();
            if (checkEnd.equalsIgnoreCase("End")) {
                break;
            }
            if (checkEnd.equalsIgnoreCase("clean")) {
                for (Product product : products) {
                    product.setAmount(0);
                    basket.addToCartSerial(product.getIndex(), product.getAmount());
                }
                basket.saveBin(fileSerial);
                continue;
            }
            parts = checkEnd.split(" ");
            numberOfProduct = Integer.parseInt(parts[0]) - 1;
            amountOfProducts = Integer.parseInt(parts[1]);
            if (parts.length != 2) {
                System.out.println("Надо ввести два числа через пробел!");
                continue;
            }
            if (numberOfProduct < 0 || numberOfProduct > 3) {
                System.out.println("Ведите номер продукта из каталога!");
                continue;
            }
            if (amountOfProducts < 1) {
                System.out.println("Количество должно быть пложительными!");
                continue;
            }
            try {
                basket.addToCartSerial(numberOfProduct, amountOfProducts);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
        System.out.println("Ваша корзина: ");
        System.out.println();
        basket.printCart();
    }
}
