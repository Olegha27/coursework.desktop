package com.example.courseworkitfu;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    public static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("foodApp");

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource("/com/example/courseworkitfu/auth/login-form.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
        stage.setTitle("Coursework IT Food");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}