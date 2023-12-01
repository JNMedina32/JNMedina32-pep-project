package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        
        app.post("/register", this::createAccountHandler);
        app.post("/login",  this::userLoginHandler);
        app.post("/messages", this::createMessageHandler);

        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccount_idHandler);

        app.delete("/messages/{message_id}", this::deleteMessageHandler);

        app.patch("/messages/{message_id}", this::updateMessageHandler);

        return app;
    }

    private void createAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = this.accountService.createAccount(account);
        if(addedAccount != null){
            ctx.json(addedAccount);
        } else {
            ctx.status(400);
        }
    }

    private void userLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account userLogin = this.accountService.userLogin(account);
        if(userLogin != null){
            ctx.json(userLogin);
        } else {
            ctx.status(401);
        }
    }

    private void createMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        System.out.println("new message to create: " + message);
        Message newMessage = this.messageService.createMessage(message);
        System.out.println("newly created message " + newMessage);
        if(newMessage != null){
            ctx.json(newMessage);
        } else {
            ctx.status(400);
        }
    }

    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        List<Message> allMessages = this.messageService.getAllMessages();
        ctx.json(allMessages);
    }

    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = this.messageService.getMessageById(message_id);
        if(message.getMessage_id() == 0){
            ctx.status(200);
        } else {
        ctx.json(message);
        }
    }

    private void deleteMessageHandler(Context ctx) throws JsonProcessingException {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = this.messageService.deleteMessage(message_id);
        if(message.getMessage_id() == 0){
            ctx.status(200);
        } else {
        ctx.json(message);
        }
    }

    private void updateMessageHandler(Context ctx) throws JsonProcessingException {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message updatedMessage = this.messageService.updateMessage(message_id, message);
        if(updatedMessage != null){
            ctx.json(updatedMessage);
            ctx.status(200);
        } else {
            ctx.status(400);
        }
    }

    private void getAllMessagesByAccount_idHandler(Context ctx) throws JsonProcessingException {
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> allUserMessages = this.messageService.getAllMessagesByAccount_id(account_id);
        ctx.json(allUserMessages);
    }

}