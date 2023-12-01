package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * Message Table:
 * int message_id - auto
 * int posted_by - fk account_id
 * String message_text - varchar(255) NOT NULL
 * long time_posted_epoch - time in seconds from 1 Jan 1970
 */

public class MessageDAO {

    /**
     * @param message to be created
     * @return created message with message_id;
     */
    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String insertSQL = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
            PreparedStatement insertStatement = connection.prepareStatement(insertSQL);

            int posted_by = message.getPosted_by();
            String message_text = message.getMessage_text();
            long time_posted = message.getTime_posted_epoch();

            insertStatement.setInt(1, posted_by);
            insertStatement.setString(2, message_text);
            insertStatement.setLong(3, time_posted);

            insertStatement.executeUpdate();
            
            String selectSQL = "SELECT * FROM message WHERE posted_by = ? ORDER BY message_id DESC LIMIT 1;";
            PreparedStatement selectStatement = connection.prepareStatement(selectSQL);
            
            selectStatement.setInt(1, posted_by);

            ResultSet resultSet = selectStatement.executeQuery();
            if(resultSet.next()){
                return new Message(resultSet.getInt("message_id"), posted_by, message_text, time_posted);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * @return List of all messages
     */
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message ORDER BY time_posted_epoch DESC;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int posted_by = resultSet.getInt("posted_by");
                String message_text = resultSet.getString("message_text");
                long time_posted_epoch = resultSet.getLong("time_posted_epoch");
                int message_id = resultSet.getInt("message_id");
                Message message = new Message(message_id, posted_by, message_text, time_posted_epoch);
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * @param message_id
     * @return Message obj found in message table OR empty obj
     */
    public Message getMessageById(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int posted_by = resultSet.getInt("posted_by");
                String message_text = resultSet.getString("message_text");
                long time_posted_epoch = resultSet.getLong("time_posted_epoch");
                return new Message(message_id, posted_by, message_text, time_posted_epoch);
            } else {
                return new Message();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * @param message_id
     * @return Message obj that was deleted from table OR empty obj
     */
    public Message deleteMessage(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String selectSQL = "SELECT * FROM message WHERE message_id = ?;";

            PreparedStatement selectStatement = connection.prepareStatement(selectSQL);
            selectStatement.setInt(1, message_id);

            ResultSet selResultSet = selectStatement.executeQuery();
            Message deletedMessage;
            if(selResultSet.next()){
                deletedMessage = new Message(message_id, selResultSet.getInt("posted_by"), selResultSet.getString("message_text"), selResultSet.getLong("time_posted_epoch"));
            } else {
                deletedMessage = new Message();
            }


            String deleteSQL = "DELETE FROM message WHERE message_id = ?;";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteSQL);

            deleteStatement.setInt(1, message_id);

            deleteStatement.execute();
        
            return deletedMessage;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * @param message_id
     * @param message
     * @return Message obj with updated message from table
     */
    public Message updateMessage(int message_id, Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String updateSQL = "UPDATE message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement updateStatement = connection.prepareStatement(updateSQL);

            String message_text = message.getMessage_text();

            updateStatement.setString(1, message_text);
            updateStatement.setInt(2, message_id);

            updateStatement.executeUpdate();

            String selectSQL = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement selectStatement = connection.prepareStatement(selectSQL);

            selectStatement.setInt(1, message_id);

            ResultSet resultSet = selectStatement.executeQuery();
            resultSet.next();
            int posted_by = resultSet.getInt("posted_by");
            long time_posted_epoch = resultSet.getLong("time_posted_epoch");

            return new Message(message_id, posted_by, message_text, time_posted_epoch);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * @param account_id
     * @return List of all messages by a given account
     */
    public List<Message> getAllMessagesByAccount_id(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ? ORDER BY time_posted_epoch DESC;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, account_id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int posted_by = resultSet.getInt("posted_by");
                String message_text = resultSet.getString("message_text");
                long time_posted_epoch = resultSet.getLong("time_posted_epoch");
                int message_id = resultSet.getInt("message_id");
                Message message = new Message(message_id, posted_by, message_text, time_posted_epoch);
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * This method is used to verify an account_id exists
     *
     * @param account_id
     * @return boolean
     */
    public boolean validPosted_id(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT account_id FROM account WHERE account_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, account_id);
        
            return preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
