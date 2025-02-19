package KATA.learn;

import KATA.learn.config.AppConfig;
import KATA.learn.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        // Создаём контекст Spring
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        // Получаем бины
        UserService userService = context.getBean(UserService.class);

        // Запуск логики взаимодействия с API
        userService.performActions();

        // Закрываем контекст
        context.close();
    }
}