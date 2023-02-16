import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClientLog {
    private Map<Integer, Integer> products = new HashMap<>();
    private File file = new File("src/Files", "log.csv");

    public void log(int productNum, int amount) {
        if (products.containsKey(productNum)) {
            int oldAmount = products.get(productNum);
            products.put(productNum, oldAmount + amount);
        } else {
            products.put(productNum, amount);
        }
    }

    public void exportAsCSV() {
        for (Map.Entry<Integer, Integer> product : products.entrySet()) {
            String productStr = product.getKey() + " " + product.getValue();
            String[] productArr = productStr.split(",");
            try (CSVWriter writer = new CSVWriter(new FileWriter(this.file, true))) {
                writer.writeNext(productArr);
            } catch (IOException e) {
                System.err.println("ClientLog.log err");
                System.err.println(e.getMessage());
            }
        }
    }
}
