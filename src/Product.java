public class Product {
    private String title;
    private int price;
    private int amount;
    private int index;

    public Product(String title, int price, int index) {
        this.title = title;
        this.price = price;
        amount = 0;
        this.index = index;
    }

    public void addAmount(int count) {
        if (count >= 0) {
            this.amount += count;
        }
    }

    public int getIndex() {
        return index;
    }

    public String getTitle() {
        return title;
    }

    public int getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAmount(int count) {
        this.amount = count;
    }
}
