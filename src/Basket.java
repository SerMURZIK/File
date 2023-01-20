import java.io.*;

public class Basket {
    private static Product[] products = new Product[3];
    private static File file = new File("src/Files", "basket.txt");

    public Basket(Product[] products) {
        Basket.products[0] = products[0];
        Basket.products[1] = products[1];
        Basket.products[2] = products[2];
    }

    public void addToCart(int productNum, int amount) {
        products[productNum].addAmount(amount);
        saveTxt(file);
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

    public void saveTxt(File textFile) {
        int adjustmentOfSpace = 0;
        try (OutputStream outputStream = new FileOutputStream(textFile)) {
            for (Product product : products) {
                String message = product.getIndex() + "-" + product.getAmount();
                if (adjustmentOfSpace >= 0 && products.length > 1 && adjustmentOfSpace != products.length) {
                    outputStream.write((message + " ").getBytes());
                }
                if (adjustmentOfSpace == products.length) {
                    outputStream.write((message).getBytes());
                }
                adjustmentOfSpace++;
            }
            outputStream.close();
        } catch (Exception e) {
            System.err.println("ERROR");
            System.out.println(e.getMessage());
        }
    }

    public static Basket loadFromTxtFile(File textFile) {
        try (InputStream inputStream = new FileInputStream(textFile)) {
            int current;
            StringBuilder sb = new StringBuilder();
            while ((current = inputStream.read()) != -1) {
                sb.append(Character.toChars(current));
            }
            String result = sb.toString();
            String[] arrForGiveSplittedResultToMainArr = result.split(" ");
            if (arrForGiveSplittedResultToMainArr.length > 1) {
                for (int i = 0; i < arrForGiveSplittedResultToMainArr.length; i++) {
                    products[Integer.parseInt(arrForGiveSplittedResultToMainArr[i].split("-")[0])].setAmount(Integer.parseInt(arrForGiveSplittedResultToMainArr[i].split("-")[1]));
                }

            }
            inputStream.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return new Basket(products);
    }
}
