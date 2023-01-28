import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Basket implements Serializable {
    private Product[] products = new Product[3];
    private ClientLog clientLog = new ClientLog();

    public Basket(Product[] products) {
        this.products[0] = products[0];
        this.products[1] = products[1];
        this.products[2] = products[2];
    }

    public void addToCartTxt(int productNum, int amount, File file, Product[] products) {
        products[productNum].addAmount(amount);
        clientLog.log(productNum, amount);
        clientLog.exportAsCSV();
        saveTxt(file);
    }

    public void addToCartJson(int productNum, int amount, File file) {
        products[productNum].addAmount(amount);
        clientLog.log(productNum, amount);
        clientLog.exportAsCSV();
        saveJson(file);
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
        } catch (Exception e) {
            System.err.println("ERROR");
            System.out.println(e.getMessage());
        }
    }

    public void saveJson(File fileJson) {
        try (FileWriter file = new FileWriter(fileJson)) {
            JSONObject obj = new JSONObject();
            for (Product product : products) {
                obj.put(product.getIndex(), product.getAmount());
            }
            file.write(obj.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Basket loadJson(File jsonFile, Product[] products) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(jsonFile));
            int i = 0;
            for (Product product : products) {
                product.setAmount(jsonObject.get(Integer.toString(i)));
                i++;
            }
        } catch (Exception e) {
            System.err.println("loadJson err");
            System.err.println(e.getMessage());
        }
        return new Basket(products);
    }

    public static Basket loadFromTxtFile(File textFile, Product[] products) {
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
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return new Basket(products);
    }
}
