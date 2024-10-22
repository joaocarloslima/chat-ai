package br.com.fiap.chatain.views;

import br.com.fiap.chatain.ChatService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.time.Instant;
import java.util.ArrayList;

@Route("")
public class HomeView extends VerticalLayout {

    private final ChatService chatService;
    private final MessageList messageList = new MessageList();
    private final TextField textField = new TextField();

    public HomeView(ChatService chatService) {

        Scroller scroller = new Scroller(messageList);
        messageList.getStyle().setBackground("#3a4b61");
        messageList.setHeightFull();
        scroller.setHeightFull();
        scroller.setWidthFull();

        setHeightFull();

        textField.setPlaceholder("digite uma mensagem");
        textField.setClearButtonVisible(true);
        textField.setPrefixComponent(VaadinIcon.CHAT.create());
        textField.setWidthFull();
        textField.addKeyDownListener(Key.ENTER, event -> {
           send();
        });

        Button button = new Button("enviar");
        button.setIcon(VaadinIcon.PAPERPLANE.create());
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickListener(clickEvent -> {
            send();
        });

        HorizontalLayout messageInputBar = new HorizontalLayout(textField, button);
        messageInputBar.setWidthFull();

        add(new H1("Chat de Ciências"));
        add(messageInputBar);
        add(scroller);
        this.chatService = chatService;
    }

    private void send(){
        String userMessage = textField.getValue();
        addMessage(userMessage, "Estudante", 1);

        String chatResponse = chatService.sentToAi(userMessage);
        addMessage(chatResponse, "Professor", 2);

        textField.clear();

    }

    private void addMessage(String message, String user, int color) {
        //Notification.show("enviando mensagem");
        MessageListItem messageListItem = new MessageListItem(message,Instant.now(),user);
        messageListItem.setUserColorIndex(color);
        var currentMessages = new ArrayList<>(messageList.getItems());
        currentMessages.add(messageListItem);
        messageList.setItems(currentMessages);
    }

}
