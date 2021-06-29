package tech.meliora.natujenge.threads.repository;

import tech.meliora.natujenge.threads.domain.Order;
import tech.meliora.natujenge.threads.enumeration.OrderStatus;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    private DataSource dataSource;

    public OrderRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * returns a list of orders in status NEW from the last record id loaded
     *
     * @param batchSize
     * @param lastRecordId
     * @return list of orders to be processed, can be empty
     */
    public List<Order> poll(int batchSize, int lastRecordId) throws SQLException {

        List<Order> list = new ArrayList<>();

        String query = "SELECT * FROM demo1.orders WHERE id > ? AND status = 'NEW' ORDER BY id ASC LIMIT " + batchSize;

        try (Connection connection = dataSource.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, lastRecordId);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setProduct(rs.getString("product"));
                order.setQuantity(rs.getInt("quantity"));
                order.setCustomerName(rs.getString("name"));
                order.setPhoneNumber(rs.getString("phonenumber"));
                order.setPrice(rs.getInt("price"));
                order.setTotal(rs.getInt("total"));

                //order status - convert from sting to enum
                OrderStatus orderStatus = OrderStatus.NEW;
                try{
                    orderStatus = OrderStatus.valueOf(rs.getString("status"));
                }catch(Exception ex){
                    //do nothing
                }
                order.setStatus(orderStatus);

                list.add(order);
            }
        }

        return list;
    }


    /**
     * saves order, ensure that you change the object status
     *
     * @param order
     * @throws SQLException
     */
    public void save(Order order) throws SQLException {
        String query = "UPDATE demo1.orders" +
                " SET " +
                " product = ?, " +
                " quantity = ?, " +
                " name = ?, " +
                " phonenumber = ?, " +
                " price = ?, " +
                " total = ?, " +
                " status = ?, " +
                " last_updated_on = NOW() " +
                " WHERE id = ?";


        try (Connection connection = dataSource.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, order.getProduct());
            statement.setInt(2, order.getQuantity());
            statement.setString(3, order.getCustomerName());
            statement.setString(4, order.getPhoneNumber());
            statement.setInt(5, order.getPrice());
            statement.setInt(6, order.getTotal());
            statement.setString(7, order.getStatus().toString());
            statement.setInt(8, order.getId());


            //execute query
            statement.execute();
        }
    }

}
