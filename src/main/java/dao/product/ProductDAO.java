package dao.product;

import connection.SingletonConnection;
import model.Category;
import model.Color;
import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements IProductDAO{

    public static final String SELECT_ALL_PRODUCT_SQL = "select p.id as id,p.name as name, price, quantity, c2.name as nameColor, C.name as nameCategory, description\n" +
                                                        "from category C\n" +
                                                        "join product p on C.id = p.idCategory\n" +
                                                        "join productDetails pD on p.id = pD.idProduct\n" +
                                                        "join color c2 on pD.idColor = c2.id;";
    public static final String INSERT_PRODUCT_SQL = "select * from color;";
    public static final String FIND_BY_ID = "select p.id as id,p.name as name, price, quantity, c2.name as nameColor, C.name as nameCategory, description\n" +
                                            "from category C\n" +
                                            "join product p on C.id = p.idCategory\n" +
                                            "join productDetails pD on p.id = pD.idProduct\n" +
                                            "join color c2 on pD.idColor = c2.id\n" +
                                            "where p.id = ?;";
    public static final String DELETE_PRODUCT_SQL = "delete from color where id = ?;";
    public static final String UPDATE_PRODUCT_SQL = "update color set name = ? where id = ?;";

    @Override
    public List<Product> findAll() {
        List<Product> productList = new ArrayList<>();
        try(
                Connection connection = SingletonConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PRODUCT_SQL);
        ){
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String nameColor = rs.getString("nameColor");
                String nameCategory = rs.getString("nameCategory");
                String description = rs.getString("description");
                Color color = new Color(nameColor);
                Category category = new Category(nameCategory);
                Product product = new Product(id, name, price, quantity, color, category, description);
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    @Override
    public Product findById(int id) {
        Product product = null;
        try(
                Connection connection = SingletonConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID);
        ) {
            preparedStatement.setInt(1,id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String nameColor = rs.getString("nameColor");
                String nameCategory = rs.getString("nameCategory");
                String description = rs.getString("description");
                Color color = new Color(nameColor);
                Category category = new Category(nameCategory);
                product = new Product(id, name, price, quantity, color, category, description);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }


    @Override
    public boolean update(ProductDAO t) {
        return false;
    }

    public void create(Product product, int[] colors) {

    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
