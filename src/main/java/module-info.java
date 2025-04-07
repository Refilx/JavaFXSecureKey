module br.com.javafxsecurekey {
    requires javafx.fxml;
    requires java.sql;
    requires jasypt;
    requires net.sf.jasperreports.core;
    requires commons.email;
    requires javax.mail;
    requires com.jfoenix;
    requires javafx.controls;
    requires java.desktop;


    opens br.com.javafxsecurekey.view.screens to javafx.fxml;
    opens br.com.javafxsecurekey.model.util to javafx.fxml;
    exports br.com.javafxsecurekey.controller;
    opens br.com.javafxsecurekey.controller to javafx.fxml;
    exports br.com.javafxsecurekey.model.aplication;
    exports br.com.javafxsecurekey.model.domain;
    exports br.com.javafxsecurekey.model.dao;
    opens br.com.javafxsecurekey.model.aplication to javafx.fxml;
    opens br.com.javafxsecurekey.model.validator to javafx.fxml;
}