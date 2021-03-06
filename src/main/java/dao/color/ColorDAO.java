package dao.color;

import com.sun.scenario.effect.impl.prism.PrReflectionPeer;
import connection.SingletonConnection;
import model.Color;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ColorDAO implements IColorDAO{

    public static final String SELECT_ALL_COLOR_SQL = "select * from color;";
    public static final String INSERT_COLOR_SQL = "INSERT INTO color (name) VALUES (?);";
    public static final String FIND_BY_ID = "select id,name from color where id = ?;";
    public static final String DELETE_COLOR_SQL = "delete from color where id = ?;";
    public static final String UPDATE_COLOR_SQL = "update color set name = ? where id = ?;";

    @Override
    public List<Color> findAll() {
        List<Color> colorList = new ArrayList<>();
        try(
                Connection connection = SingletonConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_COLOR_SQL);
                ){
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                colorList.add(new Color(id,name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return colorList;
    }

    @Override
    public Color findById(int id) {
        Color color = null;
        try(
                Connection connection = SingletonConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID);
                ) {
            preparedStatement.setInt(1,id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                String name = rs.getString("name");
                color = new Color(id, name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return color;
    }

    @Override
    public void create(Color color) {
        try(
                Connection connection = SingletonConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_COLOR_SQL);
                ) {
            preparedStatement.setString(1,color.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean update(Color color) {
        boolean rowUpdate;
        try(
                Connection connection = SingletonConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_COLOR_SQL);
        ){
            preparedStatement.setString(1,color.getName());
            preparedStatement.setInt(2, color.getId());

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
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_COLOR_SQL);
        ){
            preparedStatement.setInt(1,id);
            rowDelete = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowDelete;
    }
}
