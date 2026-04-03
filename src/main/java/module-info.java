module com.example.courseworkitfu {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires lombok;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires jbcrypt;

    opens com.example.courseworkitfu to javafx.fxml;
    exports com.example.courseworkitfu;
    opens com.example.courseworkitfu.fxControllers to javafx.fxml;
    exports com.example.courseworkitfu.fxControllers;
    exports com.example.courseworkitfu.model;
    exports com.example.courseworkitfu.hibernateOperations;
    opens com.example.courseworkitfu.hibernateOperations to javafx.fxml;
    opens com.example.courseworkitfu.model to javafx.fxml, org.hibernate.orm.core;
}