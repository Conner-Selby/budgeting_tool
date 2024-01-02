package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import Tools.*;

class IncomeTaxGUI implements ActionListener {
    protected JPanel panel;
    protected JTable table;
    protected DefaultTableModel tableModel;

    protected JButton newTaxRowButton;
    protected JButton removeTaxRowButton;
    protected JTextField incomeTextField;
    protected JTextField lowerTextField;
    protected JTextField upperTextField;
    protected JTextField percentageTextField;
    protected JLabel errorMessage;

    protected final Vector<String> columnNames;

    IncomeTaxGUI(String incomeFilePath, String taxFilePath) {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        columnNames = new Vector<>(3);

        columnNames.add("Lower Limit");
        columnNames.add("Upper Limit");
        columnNames.add("Tax Rate");

        JLabel label;
        GridBagConstraints c;

        //Constructing the GUI components and layout
        newTaxRowButton = new JButton("Add Tax Band");
        removeTaxRowButton = new JButton("Remove Tax Band");
        incomeTextField = new JTextField();
        lowerTextField = new JTextField();
        upperTextField = new JTextField();
        percentageTextField = new JTextField();
        errorMessage = new JLabel(" ");

        String income = CSVReadWrite.readCSV(incomeFilePath);
        if (income != null) {
            Utils.checkStringPositiveInteger(income, true);
            incomeTextField.setText(income);
        }

        Vector<Vector<String>> fileData = CSVReadWrite.readCSV(taxFilePath, columnNames);

        // -- Row 0
        //Gross Annual Income label
        c = Utils.createGridBagConstraints(0, 0, 5, 1, 1f, 0f);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 40, 0, 40);  //Padding
        label = new JLabel("Gross Annual Income (Pre-Tax)");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.BOTTOM);
        panel.add(label, c);

        // -- Row 1
        //Gross Annual Income textField (Integer)
        c = Utils.createGridBagConstraints(0, 1, 5, 1, 1f, 0f);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 40, 0, 40);  //Padding
        incomeTextField.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(incomeTextField, c);

        // -- Row 2
        //Tax Bands section title
        c = Utils.createGridBagConstraints(0, 2, 5, 1, 1f, 0f);
        c.insets = new Insets(20, 0, 5, 0);  //Padding
        label = new JLabel("Tax Bands");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.BOTTOM);
        panel.add(label, c);

        // -- Row 3
        //Lower textField (Integer)
        c = Utils.createGridBagConstraints(0, 3, 1, 1, 7f, 0f);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 40, 0, 0);  //Padding
        lowerTextField.setHorizontalAlignment(JTextField.RIGHT);
        panel.add(lowerTextField, c);
        //Lower/Upper separator label
        c = Utils.createGridBagConstraints(1, 3, 1, 1, 0f, 0f);
        label = new JLabel(" to ");
        panel.add(label, c);
        //Upper textField (Integer)
        c = Utils.createGridBagConstraints(2, 3, 1, 1, 7f, 0f);
        c.fill = GridBagConstraints.HORIZONTAL;
        upperTextField.setHorizontalAlignment(JTextField.RIGHT);
        panel.add(upperTextField, c);
        //Percentage textField (Integer)->[0-100]
        c.gridx = 3;
        c.weightx = 3f;
        percentageTextField.setHorizontalAlignment(JTextField.RIGHT);
        panel.add(percentageTextField, c);
        //Percentage label
        c = Utils.createGridBagConstraints(4, 3, 1, 1, 0f, 0f);
        c.insets = new Insets(0, 0, 0, 40);  //Padding
        label = new JLabel("%");
        panel.add(label, c);

        // -- Row 4
        //New Tax Band button
        c = Utils.createGridBagConstraints(0, 4, 5, 1, 1f, 0f);
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 40, 0, 40);  //Padding
        newTaxRowButton.addActionListener(this);
        panel.add(newTaxRowButton, c);

        // -- Row 5
        //Tax table
        c = Utils.createGridBagConstraints(0, 5, 5, 1, 1f, 1f);
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 40, 0, 40); //Padding
        if (fileData != null) {
            Utils.check2DVectorPositiveInteger(fileData, true);
            errorMessage.setText("Saved data was retrieved successfully!");
        } else {
            errorMessage.setText("\"" + taxFilePath + "\" could not be found...");
        }
        tableModel = new DefaultTableModel(fileData, columnNames);
        table = new JTable(tableModel) {
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
            }
        };
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        for (int col = 0; col < table.getColumnModel().getColumnCount(); col++) {
            table.getColumnModel().getColumn(col).setPreferredWidth(65);
            table.getColumnModel().getColumn(col).setCellRenderer(cellRenderer);
        }
        table.setGridColor(Color.lightGray);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        panel.add(new JScrollPane(table), c);

        // -- Row 6
        //Remove Tax Band button
        c = Utils.createGridBagConstraints(0, 6, 5, 1, 1f, 0f);
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 40, 5, 40); //Padding
        removeTaxRowButton.addActionListener(this);
        panel.add(removeTaxRowButton, c);

        // -- Row 7
        //Error label
        c = Utils.createGridBagConstraints(0, 7, 5, 1, 1f, 0f);
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 45, 5, 45); //Padding
        errorMessage.setHorizontalAlignment(SwingConstants.CENTER);
        errorMessage.setVerticalAlignment(SwingConstants.TOP);
        errorMessage.setFont(new Font("Serif", Font.PLAIN, 12));
        panel.add(errorMessage, c);
    }

    public void actionPerformed(ActionEvent e) {
        /*
        Triggers when either button is pressed. Identifies which button has been pressed and calls the appropriate function accordingly.
        */
        if (e.getSource() == newTaxRowButton) {
            queryNewTaxRow();
        } else if (e.getSource() == removeTaxRowButton) {
            queryRemoveTaxRow();
        }
    }

    JPanel getPanel() {
        /*
        Returns the object panel
        */
        return panel;
    }

    private void queryNewTaxRow() {
        /*
        Checks all necessary fields are provided prior to inserting a new table row. If a field is invalid; error message state is changed.
        */
        Vector<String> row = new Vector<>(3);

        String lower = lowerTextField.getText();
        String upper = upperTextField.getText();
        String per = percentageTextField.getText();
        row.add(lower);
        row.add(upper);
        row.add(per);

        if (Utils.checkVectorPositiveInteger(row, false)) {
            int lowerInt = Integer.parseInt(lower);
            int upperInt = Integer.parseInt(upper);
            int perInt = Integer.parseInt(per);
            if (100 < perInt) {
                errorMessage.setText("Table Entry Error: Percentage invalid! (Valid: 0 - 100)");
            } else if (lowerInt >= upperInt) {
                errorMessage.setText("Table Entry Error: Upper must be greater than Lower!");
            } else {
                lowerTextField.setText("");
                upperTextField.setText("");
                percentageTextField.setText("");
                insertNewTaxRow(row);
                errorMessage.setText(" ");
            }
        } else {
            errorMessage.setText("Table Entry Error: Values inserted must be positive integers!");
        }
    }

    private void insertNewTaxRow(Vector<String> row) {
        /*
        Inserts a new table row. Rows are inserted in descending order by the 'lower' field.
        */
        int valueToInsert = Integer.parseInt(row.getFirst());
        int valueInRow;
        for (int rowIndex = 0; rowIndex < tableModel.getRowCount(); rowIndex++) {
            valueInRow = Integer.parseInt(tableModel.getValueAt(rowIndex, 0).toString());
            if (valueInRow > valueToInsert) {
                tableModel.insertRow(rowIndex, row);
                return;
            }
        }
        tableModel.addRow(row);
    }

    private void queryRemoveTaxRow() {
        /*
        Removes the selected table item. If no item is selected; error message state is changed.
        */
        int selected = table.getSelectedRow();
        if (selected == -1) {
            errorMessage.setText("Table Removal Error: Select a row in the table to remove.");
        } else {
            errorMessage.setText(" ");
            tableModel.removeRow(selected);
        }
    }

    protected String getIncomeData() {
        /*
        Returns the Income field as a String
        */
        return incomeTextField.getText();
    }

    protected Vector<Vector<String>> getTaxData() {
        /*
        Returns the Tax table data as a Vector<Vector<String>>
        */
        int rowCount = tableModel.getRowCount();
        int colCount = tableModel.getColumnCount();
        Vector<Vector<String>> data = new Vector<>(tableModel.getRowCount());
        Vector<String> newRow;
        String value;

        for (int row = 0; row < rowCount; row++) {
            newRow = new Vector<>(colCount);
            for (int col = 0; col < colCount; col++) {
                value = tableModel.getValueAt(row, col).toString();
                newRow.add(value);
            }
            data.add(newRow);
        }
        return data;
    }
}