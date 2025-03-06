package com.hallmanagementsys.hallmanagement.util;

import javafx.scene.control.*;
import javafx.scene.layout.Region;

public class Css {

    public static final String bootStrapStyleSheet = "org/kordamp/bootstrapfx/bootstrapfx.css";

    /**
     * Applies one or more CSS classes to a JavaFX node.
     *
     * @param node    The JavaFX node (e.g., Button, TableView, Label).
     * @param classes The CSS classes to apply.
     */
    private static void applyStyles(Region node, String... classes) {
        node.getStyleClass().addAll(classes);
    }

    /**
     * Styles a Button with a primary button style.
     */
    public static void stylePrimaryButton(Button button) {
        applyStyles(button, "btn", "btn-primary");
    }

    /**
     * Styles a Button with a secondary button style.
     */
    public static void styleSecondaryButton(Button button) {
        applyStyles(button, "btn", "btn-secondary");
    }

    // ------------------------ LABEL STYLES ------------------------

    /**
     * Styles a Label as a main heading.
     */
    public static void styleHeadingLabel(Label label) {
        applyStyles(label, "h1");
    }

    /**
     * Styles a Label as a subheading.
     */
    public static void styleSubheadingLabel(Label label) {
        applyStyles(label, "h3");
    }

    /**
     * Styles a Label as a small secondary text.
     */
    public static void styleSmallLabel(Label label) {
        applyStyles(label, "small");
    }

    /**
     * Styles a Label for important information.
     */
    public static void styleInfoLabel(Label label) {
        applyStyles(label, "text-info");
    }

    /**
     * Styles a Label for success messages.
     */
    public static void styleSuccessLabel(Label label) {
        applyStyles(label, "text-success");
    }

    /**
     * Styles a Label for warning messages.
     */
    public static void styleWarningLabel(Label label) {
        applyStyles(label, "text-warning");
    }

    /**
     * Styles a Label for error messages.
     */
    public static void styleDangerLabel(Label label) {
        applyStyles(label, "text-danger");
    }

    /**
     * Styles a Label to be muted (less prominent).
     */
    public static void styleMutedLabel(Label label) {
        applyStyles(label, "text-muted");
    }

    // ------------------------ TABLE STYLES ------------------------

    /**
     * Styles a TableView with a basic table style.
     */
    public static void styleTableView(TableView<?> tableView) {
        applyStyles(tableView, "table");
    }

    /**
     * Styles a bordered TableView.
     */
    public static void styleBorderedTableView(TableView<?> tableView) {
        applyStyles(tableView, "table", "table-bordered");
    }

    /**
     * Styles a TableView with striped rows.
     */
    public static void styleStripedTableView(TableView<?> tableView) {
        applyStyles(tableView, "table", "table-striped");
    }

    /**
     * Styles a TableView with a hover effect.
     */
    public static void styleHoverTableView(TableView<?> tableView) {
        applyStyles(tableView, "table", "table-hover");
    }

    // ------------------------ FORM ELEMENTS ------------------------

    /**
     * Styles a TextField with a form-control style.
     */
    public static void styleTextField(TextField textField) {
        applyStyles(textField, "form-control");
    }

    /**
     * Styles a ComboBox with a form-control style.
     */
    public static void styleComboBox(ComboBox<?> comboBox) {
        applyStyles(comboBox, "form-control");
    }

    // ------------------------ CHOICEBOX STYLES ------------------------

    /**
     * Styles a ChoiceBox with a form-control style.
     */
    public static void styleChoiceBox(ChoiceBox<?> choiceBox) {
        applyStyles(choiceBox, "form-control");
    }

    /**
     * Styles a ChoiceBox with a bordered look.
     */
    public static void styleBorderedChoiceBox(ChoiceBox<?> choiceBox) {
        applyStyles(choiceBox, "choicebox-bordered");
    }

    /**
     * Styles a ChoiceBox with a hover effect.
     */
    public static void styleHoverChoiceBox(ChoiceBox<?> choiceBox) {
        applyStyles(choiceBox, "choicebox-hover");
    }

    // ------------------------ UTILITY METHODS ------------------------

    /**
     * Removes specific styles from a node.
     */
    public static void removeStyles(Region node, String... classes) {
        node.getStyleClass().removeAll(classes);
    }

    /**
     * Clears all styles from a node.
     */
    public static void clearStyles(Region node) {
        node.getStyleClass().clear();
    }
}

