package com.example.courseworkitfu.fxControllers.tabs.users;

import com.example.courseworkitfu.HelloApplication;
import com.example.courseworkitfu.fxControllers.users.CreateUserForm;
import com.example.courseworkitfu.fxControllers.users.EditUserForm;
import com.example.courseworkitfu.hibernateOperations.CustomOperations;
import com.example.courseworkitfu.model.User;
import com.example.courseworkitfu.session.Session;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UsersTabController implements Initializable {

    public TextField userSearchField;

    public TableView<UserTableParameters> userTable;
    public TableColumn<UserTableParameters, Number> idColumn;
    public TableColumn<UserTableParameters, String> usernameColumn;
    public TableColumn<UserTableParameters, String> emailColumn;
    public TableColumn<UserTableParameters, String> phoneColumn;
    public TableColumn<UserTableParameters, String> dateCreatedColumn;
    public TableColumn<UserTableParameters, String> roleColumn;
    public TableColumn<UserTableParameters, String> activeColumn;

    public ListView<User> userList;

    private CustomOperations customOperations;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        customOperations = new CustomOperations(HelloApplication.emf);

        initUserTable();
        initInteractions();
        refreshUsers();
    }

    private void initInteractions() {
        if (userList != null) {
            userList.getSelectionModel().selectedItemProperty().addListener((obs, oldUser, newUser) -> {
                if (newUser != null) {
                    syncTableSelection(newUser);
                }
            });

            userList.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2
                        && Session.getCurrentUser() != null
                        && Session.getCurrentUser().isAdmin()) {

                    User selected = userList.getSelectionModel().getSelectedItem();
                    if (selected != null) {
                        openUserForEdit(selected);
                    }
                }
            });
        }

        if (userTable != null) {
            userTable.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2
                        && Session.getCurrentUser() != null
                        && Session.getCurrentUser().isAdmin()) {

                    UserTableParameters selectedRow = userTable.getSelectionModel().getSelectedItem();
                    if (selectedRow != null) {
                        for (User user : customOperations.getAllRecords(User.class)) {
                            if (user.getId() == selectedRow.getId()) {
                                openUserForEdit(user);
                                break;
                            }
                        }
                    }
                }
            });
        }

        if (userSearchField != null) {
            userSearchField.setOnAction(event -> searchUsers());
        }
    }

    private void initUserTable() {
        if (idColumn != null) {
            idColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()));
        }
        if (usernameColumn != null) {
            usernameColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUsername()));
        }
        if (emailColumn != null) {
            emailColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmail()));
        }
        if (phoneColumn != null) {
            phoneColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPhone()));
        }
        if (dateCreatedColumn != null) {
            dateCreatedColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDateCreated()));
        }
        if (roleColumn != null) {
            roleColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getRole()));
        }
        if (activeColumn != null) {
            activeColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getActive()));
        }
    }

    private void loadUserList() {
        if (userList == null) return;
        userList.getItems().clear();
        userList.getItems().addAll(customOperations.getAllRecords(User.class));
    }

    private void loadUserTable() {
        if (userTable == null) return;
        userTable.getItems().clear();

        for (User user : customOperations.getAllRecords(User.class)) {
            String dateCreated = user.getDateCreated() == null ? "" : user.getDateCreated().toString();
            String email = user.getEmail() == null ? "" : user.getEmail();
            String phone = user.getPhoneNum() == null ? "" : user.getPhoneNum();
            String role = user.getClass().getSimpleName();
            String active = user.isActive() ? "Yes" : "No";

            userTable.getItems().add(new UserTableParameters(
                    user.getId(),
                    user.getUsername(),
                    email,
                    phone,
                    dateCreated,
                    role,
                    active
            ));
        }
    }

    private void syncTableSelection(User selectedUser) {
        if (selectedUser == null || userTable == null) return;

        for (UserTableParameters row : userTable.getItems()) {
            if (row.getId() == selectedUser.getId()) {
                userTable.getSelectionModel().select(row);
                userTable.scrollTo(row);
                break;
            }
        }
    }

    public void refreshUsers() {
        loadUserList();
        loadUserTable();
    }

    public void searchUsers() {
        if (userTable == null) return;

        String query = userSearchField == null ? "" : userSearchField.getText();
        if (query == null) query = "";

        String search = query.trim().toLowerCase();

        userTable.getItems().clear();
        if (userList != null) {
            userList.getItems().clear();
        }

        for (User user : customOperations.getAllRecords(User.class)) {
            String username = user.getUsername() == null ? "" : user.getUsername().toLowerCase();
            String email = user.getEmail() == null ? "" : user.getEmail().toLowerCase();
            String phone = user.getPhoneNum() == null ? "" : user.getPhoneNum().toLowerCase();
            String role = user.getClass().getSimpleName() == null ? "" : user.getClass().getSimpleName().toLowerCase();

            boolean matches =
                    username.equals(search) ||
                            email.equals(search) ||
                            phone.equals(search) ||
                            role.equals(search);

            if (matches) {
                String dateCreated = user.getDateCreated() == null ? "" : user.getDateCreated().toString();
                String active = user.isActive() ? "Yes" : "No";

                UserTableParameters row = new UserTableParameters(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail() == null ? "" : user.getEmail(),
                        user.getPhoneNum() == null ? "" : user.getPhoneNum(),
                        dateCreated,
                        user.getClass().getSimpleName(),
                        active
                );

                userTable.getItems().add(row);

                if (userList != null) {
                    userList.getItems().add(user);
                }
            }
        }
    }

    public void resetUserSearch() {
        if (userSearchField != null) {
            userSearchField.clear();
        }
        refreshUsers();
    }

    public void loadRegForm() {
        User currentUser = Session.getCurrentUser();

        if (!isDesktopAccessAllowed(currentUser)) {
            alert("Access denied",
                    "Customers and drivers are not allowed to use the desktop application.\n" +
                            "Please use the web application.");
            javafx.application.Platform.exit();
            return;
        }

        if (currentUser == null || !currentUser.isAdmin()) {
            alert("Access denied", "Only admin can create users.");
            return;
        }

        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource("/com/example/courseworkitfu/tabs/users/create-user-form.fxml")
            );
            Parent parent = loader.load();

            CreateUserForm controller = loader.getController();
            controller.setData(true, null);

            stage.setScene(new Scene(parent));
            stage.setTitle("Create User");
            stage.showAndWait();

            refreshUsers();
        } catch (Exception e) {
            e.printStackTrace();
            alert("Error", "Failed to open create user form.");
        }
    }

    public void deleteUser() {
        User currentUser = Session.getCurrentUser();

        if (!isDesktopAccessAllowed(currentUser)) {
            alert("Access denied",
                    "Customers and drivers are not allowed to use the desktop application.\n" +
                            "Please use the web application.");
            javafx.application.Platform.exit();
            return;
        }

        if (currentUser == null || !currentUser.isAdmin()) {
            alert("Access denied", "Only admin can delete users.");
            return;
        }

        User user = userList == null ? null : userList.getSelectionModel().getSelectedItem();
        if (user != null) {
            customOperations.delete(user.getId(), User.class);
            refreshUsers();
        }
    }

    private void openUserForEdit(User user) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource("/com/example/courseworkitfu/tabs/users/edit-user-form.fxml")
            );
            Parent parent = loader.load();

            EditUserForm controller = loader.getController();
            controller.setUser(user);

            Scene scene = new Scene(parent);
            stage.setTitle("Edit User");
            stage.setScene(scene);
            stage.showAndWait();

            refreshUsers();
        } catch (Exception e) {
            e.printStackTrace();
            alert("Error", "Failed to open edit user form.");
        }
    }

    private boolean isDesktopAccessAllowed(User user) {
        if (user == null) return false;
        return user.isAdmin() || user.getClass().getSimpleName().equalsIgnoreCase("Restaurant");
    }

    private void alert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}