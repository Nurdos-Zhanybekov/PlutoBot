package io.project.plutobot.Service;

import io.project.plutobot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot{
    private static int sultan = 0;
    private static int bekzhan = 0;
    private static int emir = 0;
    private static int nurdos = 0;

    private final BotConfig config;

    public TelegramBot(BotConfig config){
        this.config = config;
    }

    @Override
    public String getBotUsername(){
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText){
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getFrom().getFirstName());
                    break;
                case "kawasaki kidala":
                    bekzhan++;
                    if (bekzhan == 1) {
                        sendMessage(chatId, "Bekzhan kinul " + bekzhan + " raz");
                    }else{
                        sendMessage(chatId, "Bekzhan kinul " + bekzhan + " raza");
                    }
                    break;
                case "@treqter8888 kidala":
                    emir++;
                    if (emir == 1) {
                        sendMessage(chatId, "Emir kinul " + emir + " raz");
                    }else{
                        sendMessage(chatId, "Emir kinul " + emir + " raza");
                    }
                    break;
                case "@Nea006 kidala":
                    sultan++;
                    if (sultan == 1) {
                        sendMessage(chatId, "Sulya kinul " + sultan + " raz");
                    }else{
                        sendMessage(chatId, "Sulya kinul " + sultan + " raza");
                    }
                    break;
                case "nickname13579 kidala":
                    nurdos++;
                    if (sultan == 1) {
                        sendMessage(chatId, "Svetoi prostil vas " + nurdos + " raz");
                    }else{
                        sendMessage(chatId, "Svetoi prostil vas " + nurdos + " raza");
                    }
                    break;
                default:
                    return;

            }
        }
    }

    private void startCommandReceived(long chatId, String name){

        String answer = "Hi, " + name + ", nice to meet you!";

        log.info("Replied to user {}", name);

        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try{
            execute(message);
        }catch (TelegramApiException e){
            log.error("Error occurred: {}", e.getMessage());
        }
    }
}
