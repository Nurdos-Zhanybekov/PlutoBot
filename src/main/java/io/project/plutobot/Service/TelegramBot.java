package io.project.plutobot.Service;

import io.project.plutobot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.util.Properties;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot{

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

            Properties properties = new Properties();
            try(FileInputStream fileInputStream = new FileInputStream("C:\\Users\\Public\\PlutoBot\\counter.properties")){
                properties.load(fileInputStream);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            switch (messageText){
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getFrom().getFirstName());
                    break;
                case "kawasaki kidala":

                    int bekzhan = Integer.parseInt(properties.getProperty("beka", "0"));
                    bekzhan++;
                    properties.setProperty("beka", String.valueOf(bekzhan));

                    try(FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\Public\\PlutoBot\\counter.properties")){
                        properties.store(fileOutputStream, "File is modified");
                    }catch (IOException e){
                        System.out.println("Error: " + e.getMessage());
                    }

                    if (bekzhan == 1) {
                        sendMessage(chatId, "Bekzhan kinul " + bekzhan + " raz");
                    }else{
                        sendMessage(chatId, "Bekzhan kinul " + bekzhan + " raza");
                    }
                    break;
                case "@Nea006 kidala":

                    int sultan = Integer.parseInt(properties.getProperty("sultan", "0"));
                    sultan++;
                    properties.setProperty("sultan", String.valueOf(sultan));

                    try(FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\Public\\PlutoBot\\counter.properties")){
                        properties.store(fileOutputStream, "File is modified");
                    }catch (IOException e){
                        System.out.println("Error: " + e.getMessage());
                    }

                    if (sultan == 1) {
                        sendMessage(chatId, "Sultan kinul " + sultan + " raz");
                    }else{
                        sendMessage(chatId, "Sultan kinul " + sultan + " raza");
                    }
                    break;
                case "@treqter8888 kidala":

                    int emir = Integer.parseInt(properties.getProperty("emir", "0"));
                    emir++;
                    properties.setProperty("emir", String.valueOf(emir));

                    try(FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\Public\\PlutoBot\\counter.properties")){
                        properties.store(fileOutputStream, "File is modified");
                    }catch (IOException e){
                        System.out.println("Error: " + e.getMessage());
                    }

                    if (emir == 1) {
                        sendMessage(chatId, "Emir kinul " + emir + " raz");
                    }else{
                        sendMessage(chatId, "Emir kinul " + emir + " raza");
                    }
                    break;
                case "@nickname13579 kidala":

                    int nurdos = Integer.parseInt(properties.getProperty("nurdos", "0"));
                    nurdos++;
                    properties.setProperty("nurdos", String.valueOf(nurdos));

                    try(FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\Public\\PlutoBot\\counter.properties")){
                        properties.store(fileOutputStream, "File is modified");
                    }catch (IOException e){
                        System.out.println("Error: " + e.getMessage());
                    }

                    if (nurdos == 1) {
                        sendMessage(chatId, "Nurdos kinul " + nurdos + " raz");
                    }else{
                        sendMessage(chatId, "Nurdos kinul " + nurdos + " raza");
                    }
                    break;
                case "/kidalaStat":
                    sendMessage(chatId, "Sultan: " + properties.getProperty("sultan") + "\n" +
                            "Nurdos: " + properties.getProperty("nurdos") + "\n" +
                            "Emir: " + properties.getProperty("emir") + "\n" +
                            "Bekzhan: " + properties.getProperty("beka"));

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
