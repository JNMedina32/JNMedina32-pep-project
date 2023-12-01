package Service;

import DAO.MessageDAO;
import Model.Message;
import java.util.List;

/*
 * Messages Table:
 * int message_id - auto
 * int posted_by - fk account_id
 * String message_text - varchar(255) NOT NULL
 * long time_posted_epoch - time in seconds from 1 Jan 1970
 */

public class MessageService {

    public MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public Message creatMessage(Message message) {
        if (verifyMessageAndPost_id(message.getMessage_text(), message.getPosted_by()) == false) {
            return null;
        }
        return messageDAO.creatMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int message_id) {
        return messageDAO.getMessageById(message_id);
    }

    public Message deleteMessage(int message_id) {
        return messageDAO.deleteMessage(message_id);
    }

    public Message updateMessage(int message_id, Message message) {
        if (verifyMessageAndPost_id(message.getMessage_text(), message.getPosted_by()) == false) {
            return null;
        }
        return messageDAO.updateMessage(message_id, message);
    }

    public List<Message> getAllMessagesByAccount_id(int account_id) {
        return messageDAO.getAllMessagesByAccount_id(account_id);
    }

    /**
     * This method checks if:
     *  message_text is empty OR
     *  message_text.length is > 255 OR
     *  posted_by references an existing account_id;
     * @param message_text
     * @param posted_by
     * @return boolean
     */
    public boolean verifyMessageAndPost_id(String message_text, int posted_by) {
        boolean validPosted_id = messageDAO.validPosted_id(posted_by);
        if (message_text == "" || message_text.length() > 255 || validPosted_id == false) {
            return false;
        }
        return true;
    }
}
