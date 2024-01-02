package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.Vector;

import Tools.*;

class ExpensesGUI implements ActionListener {
    protected JPanel panel;
    protected JTable table;
    protected DefaultTableModel tableModel;

    protected JButton newExpenseRowButton;
    protected JButton removeExpenseRowButton;
    protected JTextField labelTextField;
    protected JTextField costTextField;
    protected JLabel errorMessage;

    protected final Vector<String> columnNames;


    ExpensesGUI(String filePath) {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        columnNames = new Vector<>(3);

        columnNames.add("Label");
        columnNames.add("Cost");

        JLabel label;
        GridBagConstraints c;

        //Constructing the GUI components and layout
        newExpenseRowButton = new JButton("Add Expense");
        removeExpenseRowButton = new JButton("Remove Expense");
        labelTextField = new JTextField();
        costTextField = new JTextField();
        errorMessage = new JLabel(" ");

        Vector<Vector<String>> fileData = CSVReadWrite.readCSV(filePath, columnNames);

        // -- Row 0
        //Expense item (Label) label
        c = Utils.createGridBagConstraints(0, 0, 1, 1, 0f, 0f);
        label = new JLabel("Label:");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 40, 0, 5);  //Padding
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(label, c);
        //Expense item (Label) textField (String)
        c = Utils.createGridBagConstraints(1, 0, 3, 1, 5f, 0f);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 0, 0, 40);  //Padding
        labelTextField.setHorizontalAlignment(JTextField.LEFT);
        panel.add(labelTextField, c);

        // -- Row 1
        //Expense item (Cost) label
        c = Utils.createGridBagConstraints(0, 1, 1, 1, 0f, 0f);
        label = new JLabel("Cost (£/Month):");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 40, 0, 5);  //Padding
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(label, c);
        //Expense item (Cost) textField (Float)
        c = Utils.createGridBagConstraints(1, 1, 3, 1, 5f, 0f);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 0, 0, 40);  //Padding
        costTextField.setHorizontalAlignment(JTextField.LEFT);
        panel.add(costTextField, c);

        // -- Row 2
        //New Expense button
        c = Utils.createGridBagConstraints(0, 2, 4, 1, 1f, 0f);
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 40, 0, 40);  //Padding
        newExpenseRowButton.addActionListener(this);
        panel.add(newExpenseRowButton, c);

        // -- Row 3
        //Expense table
        c = Utils.createGridBagConstraints(0, 3, 4, 1, 1f, 1f);
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 40, 0, 40); //Padding
        if (fileData != null) {
            Utils.check2DVectorPositiveFloat(fileData, 1, true);
            errorMessage.setText("Saved data was retrieved successfully!");
        } else {
            errorMessage.setText("\"" + filePath + "\" could not be found...");
        }
        tableModel = new DefaultTableModel(fileData, columnNames);
        table = new JTable(tableModel) {
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
            }
        };
        DefaultTableCellRenderer cellRenderer;
        for (int col = 0; col < table.getColumnModel().getColumnCount(); col++) {
            cellRenderer = new DefaultTableCellRenderer();
            if (col == 0) {
                cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            } else {
                cellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
            }
            table.getColumnModel().getColumn(col).setPreferredWidth(65);
            table.getColumnModel().getColumn(col).setCellRenderer(cellRenderer);
        }
        table.setGridColor(Color.lightGray);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        panel.add(new JScrollPane(table), c);

        // -- Row 4
        //Remove Expense button
        c = Utils.createGridBagConstraints(0, 4, 4, 1, 1f, 0f);
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 40, 5, 40); //Padding
        removeExpenseRowButton.addActionListener(this);
        panel.add(removeExpenseRowButton, c);

        // -- Row 5
        //Error label
        c = Utils.createGridBagConstraints(0, 5, 4, 1, 1f, 0f);
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
        Triggers when either button is pressed. Identifies what has been pressed and acts accordingly.
        */
        if (e.getSource() == newExpenseRowButton) {
            queryNewExpenseRow();
        } else if (e.getSource() == removeExpenseRowButton) {
            queryRemoveExpenseRow();
        }
    }

    JPanel getPanel() {
        /*
        Returns the object panel
        */
        return panel;
    }

    private void queryNewExpenseRow() {
        /*
        Checks all textFields are of valid types and format, if so: adds a new row. Else: error message state is changed.
        */
        Vector<String> row = new Vector<>(3);

        String label = labelTextField.getText();
        String cost = costTextField.getText();
        row.add(label);
        row.add(cost);

        if (Utils.checkStringPositiveFloat(cost, false)) {
            if (Objects.equals(label, "")) {
                errorMessage.setText("Table Entry Error: Label must not be blank!");
            } else {
                labelTextField.setText("");
                costTextField.setText("");
                insertNewExpenseRow(row);
                errorMessage.setText(" ");
            }

        } else {
            errorMessage.setText("Table Entry Error: Cost must be a positive float!");
        }
    }

    private void queryRemoveExpenseRow() {
        /*
        Checks a table row has been selected, if so: removes the selected row. Else: error message state is changed.
        */
        int selected = table.getSelectedRow();
        if (selected == -1) {
            errorMessage.setText("Table Removal Error: Select a row in the table to remove.");
        } else {
            errorMessage.setText(" ");
            tableModel.removeRow(selected);
        }
    }

    private void insertNewExpenseRow(Vector<String> row) {
        /*
        Inserts the given row into the expense table, the row will be added in descending order according to the Cost column.
        */
        float valueToInsert = Float.parseFloat(row.getLast());
        float valueInRow;
        for (int rowIndex = 0; rowIndex < tableModel.getRowCount(); rowIndex++) {
            valueInRow = Float.parseFloat(tableModel.getValueAt(rowIndex, 1).toString());
            if (valueInRow <= valueToInsert) {
                tableModel.insertRow(rowIndex, row);
                return;
            }
        }
        tableModel.addRow(row);
    }

    protected Vector<Vector<String>> getExpensesData() {
        /*
        Returns the Expenses table data as a Vector<Vector<String>>
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