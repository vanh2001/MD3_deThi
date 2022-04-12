package dao.product;

import connection.SingletonConnection;
import model.Category;
import model.Color;
import model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements IProductDAO{

    public static final String SELECT_ALL_PRODUCT_SQL = "select p.id as id,p.name as name, price, quantity, c2.name as nameColor, C.name as nameCategory, description\n" +
                                                        "from category C\n" +
                                                        "join product p on C.id = p.idCategory\n" +
                                                        "join productDetails pD on p.id = pD.idProduct\n" +
                                                        "join color c2 on pD.idColor = c2.id;";
    public static final String INSERT_PRODUCT_SQL = "insert into product (name, price, quantity, idCategory, description) VALUES (?,?,?,?,?);";
    public static final String INSERT_PRODUCT_DETAILS_SQL = "INSERT INTO productDetails (idProduct, idColor) VALUES (?,?);";
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

    @Override
    public void create(Product product, int[] colors) {
        Connection connection = null;
        PreparedStatement pstmtProduct = null;
        PreparedStatement pstmtProductDetails = null;
        ResultSet rs = null;
        try {
            connection = SingletonConnection.getConnection();
            connection.setAutoCommit(false);
            pstmtProduct = connection.prepareStatement(INSERT_PRODUCT_SQL, Statement.RETURN_GENERATED_KEYS);
            pstmtProduct.setString(1, product.getName());
            pstmtProduct.setDouble(2, product.getPrice());
            pstmtProduct.setInt(3, product.getQuantity());
            pstmtProduct.setInt(4, product.getCategory().getId());
            pstmtProduct.setString(4, product.getDescription());
            int rowAffected = pstmtProduct.executeUpdate();
            rs = pstmtProduct.getGeneratedKeys();
            int productId = 0;
            if (rs.next()){
                productId = rs.getInt(1);
            }
            if (rowAffected == 1){
                String sqlPivot = INSERT_PRODUCT_DETAILS_SQL;
                pstmtProductDetails = connection.prepareStatement(INSERT_PRODUCT_DETAILS_SQL);
                for (int color : colors){
                    pstmtProductDetails.setInt(1,productId);
                    pstmtProductDetails.setInt(2, color);
                    pstmtProductDetails.executeUpdate();
                }
                connection.commit();
            }else {
                connection.rollback();
            }
        } catch (SQLException e) {
            try {
                if (connection != null)
                    connection.rollback();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmtProduct != null) pstmtProduct.close();
                if (pstmtProductDetails != null) pstmtProductDetails.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
