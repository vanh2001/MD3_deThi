package dao.product;

import dao.IDAO;
import model.Product;

import java.util.List;

public interface IProductDAO{
    List<Product> findAll();
    Product findById (int id);
    void create(Product product, int[] colors);
    boolean update(ProductDAO t);
    boolean delete(int id);

}
