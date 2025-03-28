package io.project.plutobot.Service;

import io.project.plutobot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot{

    private final BotConfig config;
    private static final String HELP_TEXT = "Help yourself yourself";

    public TelegramBot(BotConfig config){
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "get started"));
        listOfCommands.add(new BotCommand("/bekzhan", "mark bekzhan"));
        listOfCommands.add(new BotCommand("/emir", "mark emir"));
        listOfCommands.add(new BotCommand("/sultan", "mark sultan"));
        listOfCommands.add(new BotCommand("/nurdos", "mark nurdos"));
        listOfCommands.add(new BotCommand("/kidalastat", "show statistic"));
        listOfCommands.add(new BotCommand("/mydata", "get your data stored"));
        listOfCommands.add(new BotCommand("/deletedata", "delete my data"));
        listOfCommands.add(new BotCommand("/help", "how to use this bot"));
        listOfCommands.add(new BotCommand("/settings", "set your prefences"));

        try{
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        }catch (TelegramApiException e){
            log.error("Error setting bot's command list: " + e.getMessage());
        }
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

            Properties properties = loadProperties();

            switch (messageText){
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getFrom().getFirstName());
                    break;
                case "/bekzhan":
                    try {
                        int countBekzhan = incrementValue(properties, "bekzhan");

                        sendMessage(chatId, "Количество киданий Бекжана: " + countBekzhan);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "/sultan":
                    try {
                        int countSultan = incrementValue(properties, "sultan");

                        sendMessage(chatId, "Количество киданий Султана: " + countSultan);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "/emir":
                    try {
                        int countEmir = incrementValue(properties, "emir");

                        sendMessage(chatId, "Количество киданий Эмира: " + countEmir);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "/nurdos":
                    try {
                        int countNurdos = incrementValue(properties, "nurdos");

                        sendMessage(chatId, "Количество киданий Нурдоса: " + countNurdos);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "/kidalastat":
                    sendMessage(chatId, "Султан: " + properties.getProperty("sultan") + "\n" +
                            "Нурдос: " + properties.getProperty("nurdos") + "\n" +
                            "Эмир: " + properties.getProperty("emir") + "\n" +
                            "Бекжан: " + properties.getProperty("bekzhan"));
                    break;
                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    break;

                default:
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

    private Properties loadProperties(){
        Properties properties = new Properties();
        try(FileInputStream fileInputStream = new FileInputStream("C:\\Users\\Public\\PlutoBot\\counter.properties")){
            properties.load(fileInputStream);

        } catch (IOException e) {
            log.error("Couldn't read file: {}", e.getMessage());
        }

        return properties;
    }

    private int incrementValue(Properties properties, String key) throws FileNotFoundException {
        int count = Integer.parseInt(properties.getProperty(key, "0")) + 1;
        properties.setProperty(key, String.valueOf(count));

        try(FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\Public\\PlutoBot\\counter.properties")){
            properties.store(fileOutputStream, "file is updated");
        }catch (IOException e) {
            log.error("Couldn't overwrite file: " + e.getMessage());
        }

        return count;
    }
}
