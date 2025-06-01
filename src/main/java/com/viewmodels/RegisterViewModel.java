package com.viewmodels;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @authors Clement Luo
 * @date June 1, 2025
 * @edited June 1, 2025
 * @since 1.0
 */
public class RegisterViewModel {
    private final StringProperty fullname = new SimpleStringProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty confirmPassword = new SimpleStringProperty();
    private final StringProperty dob = new SimpleStringProperty();
}
