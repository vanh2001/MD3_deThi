package dao.category;

import connection.SingletonConnection;
import model.Category;
import model.Color;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO implements ICategoryDAO{

    public static final String SELECT_ALL_CATEGORY_SQL = "select * from category;";
    public static final String INSERT_CATEGORY_SQL = "INSERT INTO category (name) VALUES (?);";
    public static final String FIND_BY_ID = "select id, name from category where id = ?;";
    public static final String DELETE_CATEGORY_SQL = "delete from category where id = ?;";
    public static final String UPDATE_CATEGORY_SQL = "update category set name = ? where id = ?;";

    @Override
    public List<Category> findAll() {
        List<Category> categoryList = new ArrayList<>();
        try(
                Connection connection = SingletonConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CATEGORY_SQL);
        ){
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                categoryList.add(new Category(id,name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryList;
    }

    @Override
    public Category findById(int id) {
        Category category = null;
        try(
                Connection connection = SingletonConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID);
        ) {
            preparedStatement.setInt(1,id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                String name = rs.getString("name");
                category = new Category(id, name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }

    @Override
    public void create(Category category) {
        try(
                Connection connection = SingletonConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CATEGORY_SQL);
        ) {
            preparedStatement.setString(1,category.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean update(Category category) {
        boolean rowUpdate;
        try(
                Connection connection = SingletonConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CATEGORY_SQL);
        ){
            preparedStatement.setString(1,category.getName());
            preparedStatement.setInt(2, category.getId());

            rowUpdate = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowUpdate;
    }

    @Override
    public boolean delete(int id) {
        boolean rowDelete;
        try(
                Connection connection = SingletonConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CATEGORY_SQL);
        ){
            preparedStatement.setInt(1, id);
            rowDelete = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowDelete;
    }
}
