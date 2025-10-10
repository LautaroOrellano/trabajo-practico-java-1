package interfaces;

public interface IStock {
    public int checkStock (int id);
    public void addStock (int id, int quantity);
    public void removeStock (int id, int quantity);
}
