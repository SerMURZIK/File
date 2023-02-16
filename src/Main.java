import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Scanner;

public class Main {
    private boolean loadEnabled;
    private String loadFileName;
    private String loadFormat;
    private boolean saveEnabled;
    private String saveFileName;
    private String saveFormat;
    private boolean logEnabled;
    private String logFileName;

    public Main() {
        File fileXml = new File("shop.xml");
        load(fileXml);
        Scanner scanner = new Scanner(System.in);
        Product milk = new Product("Молко", 125, 0);
        Product bread = new Product("Хлеб", 50, 1);
        Product crisps = new Product("Чипсы", 200, 2);
        Product[] products = {milk, bread, crisps};
        File fileTxt = new File("src/Files", "basket.txt");
        File fileJsonSave = new File("src/Files", saveFileName);
        File fileJsonLoad = new File("src/Files", loadFileName);
        File fileCSV = new File("src/Files", saveFileName);
        Basket basket = new Basket(products);
        ClientLog clientLog = new ClientLog();

        printAll();

        if (loadEnabled) {
            try {
                basket = Basket.loadJson(fileJsonLoad, products);
            } catch (Exception e) {
                System.err.println("load err");
                System.err.println(e.getMessage());
            }
        }

        if (basket == null) {
            basket = new Basket(products);
        }

        String checkCase;
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
            System.out.println("Введите номер продукта и количество продуктов через пробел или \"end\", или \"clean\", или \"show basket\".");
            checkCase = scanner.nextLine();
            if (checkCase.equalsIgnoreCase("End")) {
                break;
            }
            if (checkCase.equalsIgnoreCase("clean")) {
                for (Product product : products) {
                    product.setAmount(0);
                    if (saveEnabled) {
                        basket.saveJson(fileJsonSave);
                    }
                }
                if (saveEnabled) {
                    basket.saveJson(fileJsonSave);
                }
                continue;
            }
            if (checkCase.equalsIgnoreCase("show basket")) {
                basket.printCart();
                continue;
            }
            parts = checkCase.split(" ");
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
                if (saveEnabled) {
                    basket.addToCartJson(numberOfProduct, amountOfProducts, fileJsonSave);
                }
                if (logEnabled) {
                    clientLog.log(numberOfProduct, amountOfProducts);
                    clientLog.exportAsCSV();
                }
            } catch (Exception e) {
                System.err.println("save in Main err");
                System.err.println(e.getMessage());
            }

        }
        System.out.println("Ваша корзина: ");
        System.out.println();
        basket.printCart();
    }

    public static void main(String[] args) {
        new Main();
    }

    public void printAll() {
        System.out.println(loadEnabled);
        System.out.println(loadFileName);
        System.out.println(loadFormat);
        System.out.println(saveEnabled);
        System.out.println(saveFileName);
        System.out.println(saveFormat);
        System.out.println(logEnabled);
        System.out.println(logFileName);
    }

    public void load(File file) {//TODO Понять как получить конткретный node и извлечь инфу
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            Node node = doc.getFirstChild();
            NodeList mainList = node.getChildNodes();

            for (int i = 0; i < mainList.getLength(); i++) {
                Node node1 = mainList.item(i);

                if (node1.getNodeType() == Node.ELEMENT_NODE) {
                } else {
                    continue;
                }
                NodeList childNodeList = node1.getChildNodes();
                for (int j = 0; j < childNodeList.getLength(); j++) {
                    if (childNodeList.item(j).getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }

                    if (node1.getNodeName().equals("load")) {
                        if (childNodeList.item(j).getNodeName().equals("enabled")) {
                            getLoadEnabled(childNodeList.item(j));
                            continue;
                        }
                        if (childNodeList.item(j).getNodeName().equals("fileName")) {
                            getLoadFileName(childNodeList.item(j));
                            continue;
                        }
                        if (childNodeList.item(j).getNodeName().equals("format")) {
                            getLoadFormat(childNodeList.item(j));
                            continue;
                        }
                    }

                    if (node1.getNodeName().equals("save")) {
                        if (childNodeList.item(j).getNodeName().equals("enabled")) {
                            getSaveEnabled(childNodeList.item(j));
                            continue;
                        }
                        if (childNodeList.item(j).getNodeName().equals("fileName")) {
                            getSaveFileName(childNodeList.item(j));
                            continue;
                        }
                        if (childNodeList.item(j).getNodeName().equals("format")) {
                            getSaveFormat(childNodeList.item(j));
                            continue;
                        }
                    }

                    if (node1.getNodeName().equals("log")) {
                        if (childNodeList.item(j).getNodeName().equals("enabled")) {
                            getLogEnabled(childNodeList.item(j));
                            continue;
                        }
                        if (childNodeList.item(j).getNodeName().equals("fileName")) {
                            getLogFileName(childNodeList.item(j));
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("load in amin err " + e.getMessage());
        }
    }

    public void getLoadEnabled(Node node) {
        this.loadEnabled = node.getTextContent().equals("true");
    }

    public void getLoadFileName(Node node) {
        this.loadFileName = node.getTextContent();
    }

    public void getLoadFormat(Node node) {
        this.loadFormat = node.getTextContent();
    }

    public void getSaveEnabled(Node node) {
        this.saveEnabled = node.getTextContent().equals("true");
    }

    public void getSaveFileName(Node node) {
        this.saveFileName = node.getTextContent();
    }

    public void getSaveFormat(Node node) {
        this.saveFormat = node.getTextContent();
    }

    public void getLogEnabled(Node node) {
        this.logEnabled = node.getTextContent().equals("true");
    }

    public void getLogFileName(Node node) {
        this.logFileName = node.getTextContent();
    }
}
