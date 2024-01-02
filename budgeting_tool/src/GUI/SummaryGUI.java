package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.Vector;

import Tools.CSVReadWrite;
import Tools.Utils;

class SummaryGUI implements ActionListener {
    protected JPanel panel;
    protected JTable table;
    protected DefaultTableModel tableModel;

    protected JButton updateFilesButton;
    protected JLabel monthlySpendingLabel;
    protected JLabel annualSpendingLabel;
    protected JLabel errorMessage;

    protected String incomeData;
    protected Vector<Vector<String>> taxData;
    protected Vector<Vector<String>> expensesData;

    final String incomeFilePath;
    final String taxFilePath;
    final String expensesFilePath;
    final Vector<String> columnNames;

    SummaryGUI(String iFilePath, String tFilePath, String eFilePath) {
        incomeFilePath = iFilePath;
        taxFilePath = tFilePath;
        expensesFilePath = eFilePath;

        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        columnNames = new Vector<>(2);

        columnNames.add("Label");
        columnNames.add("Monthly Value");
        columnNames.add("Annual Value");


        JLabel label;
        GridBagConstraints c;

        //Constructing the GUI components and layout
        monthlySpendingLabel = new JLabel(" ");
        annualSpendingLabel = new JLabel(" ");
        updateFilesButton = new JButton("Save All Tables");
        errorMessage = new JLabel(" Error: Summary update unsuccessful..."); //Default value in the event that the update function doesn't trigger correctly.

        // -- Row 0
        //Summary heading label
        c = Utils.createGridBagConstraints(0, 0, 4, 1, 1f, 0f);
        label = new JLabel("Summary");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 40, 0, 40);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 20));
        panel.add(label, c);

        // -- Row 1
        //Summary table
        c = Utils.createGridBagConstraints(0, 1, 4, 1, 1f, 1f);
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 40, 0, 40);
        tableModel = new DefaultTableModel(null, columnNames);
        table = new JTable(tableModel) {
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
            }
        };
        table.setFocusable(false);
        table.setRowSelectionAllowed(false);
        panel.add(new JScrollPane(table), c);

        // -- Row 2
        //Discretionary Income section label
        c = Utils.createGridBagConstraints(0, 2, 4, 1, 1f, 0f);
        label = new JLabel("- Discretionary Income -");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(20, 40, 0, 40);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 16));
        panel.add(label, c);

        // -- Row 3
        //Monthly Discretionary Income label
        c = Utils.createGridBagConstraints(0, 3, 2, 1, 1f, 0f);
        label = new JLabel("Per Month:  £");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 40, 0, 3);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(label, c);
        //Monthly Discretionary Income value label
        c = Utils.createGridBagConstraints(2, 3, 2, 1, 1f, 0f);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 0, 0, 40);
        monthlySpendingLabel.setHorizontalAlignment(SwingConstants.LEFT);
        panel.add(monthlySpendingLabel, c);

        // -- Row 4
        //Yearly Discretionary Income label
        c = Utils.createGridBagConstraints(0, 4, 2, 1, 1f, 0f);
        label = new JLabel("Per Year:  £");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 40, 0, 3);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(label, c);
        //Yearly Discretionary Income value label
        c = Utils.createGridBagConstraints(2, 4, 2, 1, 1f, 0f);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 0, 0, 40);
        annualSpendingLabel.setHorizontalAlignment(SwingConstants.LEFT);
        panel.add(annualSpendingLabel, c);

        // -- Row 5
        //Update button
        c = Utils.createGridBagConstraints(0, 5, 4, 1, 1f, 0f);
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 40, 0, 40);
        updateFilesButton.addActionListener(this);
        panel.add(updateFilesButton, c);

        // -- Row 6
        //Error label
        c = Utils.createGridBagConstraints(0, 6, 4, 1, 1f, 0f);
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 45, 5, 45);
        errorMessage.setHorizontalAlignment(SwingConstants.CENTER);
        errorMessage.setVerticalAlignment(SwingConstants.TOP);
        errorMessage.setFont(new Font("Serif", Font.PLAIN, 12));
        panel.add(errorMessage, c);
    }

    public void actionPerformed(ActionEvent e) {
        /*
        Triggers when the 'updateFiles' button is pressed. First checks all tables and fields are valid, if they are; attempt to systematically update all CSV files.
        */
        if (!Utils.checkStringPositiveInteger(incomeData, false)) {
            errorMessage.setText("Saving Failed: Income field is invalid");
        } else if (!Utils.check2DVectorPositiveInteger(taxData, false)) {
            errorMessage.setText("Saving Failed: Tax table is invalid");
        } else if (!Utils.check2DVectorPositiveFloat(expensesData, 1, false)) {
            errorMessage.setText("Saving Failed: Expenses table is invalid");
        } else {
            if (!CSVReadWrite.writeCSV(incomeFilePath, incomeData)) {
                errorMessage.setText("Error: " + incomeFilePath + " writing failed...");
            } else if (!CSVReadWrite.writeCSV(taxFilePath, taxData)) {
                errorMessage.setText("Error: " + taxFilePath + " writing failed...");
            } else if (!CSVReadWrite.writeCSV(expensesFilePath, expensesData)) {
                errorMessage.setText("Error: " + expensesFilePath + " writing failed...");
            } else {
                errorMessage.setText("Saving successful!");
            }
        }
    }

    JPanel getPanel() {
        /*
        Returns the object panel
        */
        return panel;
    }

    protected void updateSummary(String income, Vector<Vector<String>> tax, Vector<Vector<String>> expenses) {
        /*
        When a user opens the summary page: Update the Summary page elements given all entered data in the Income, Tax and Expenses sections.
         */
        Vector<Vector<String>> summaryData = new Vector<>(expenses.size() + 2);
        Vector<String> row;
        final int originalIncome;
        double annualSpendingAllowance;

        incomeData = income;
        taxData = tax;
        expensesData = expenses;

        //Assuming nothing goes wrong, this message will remain unchanged.
        String originalErrorMessage = "Report generation successful!";

        errorMessage.setText(originalErrorMessage);

        //Check the income field is a valid integer, if not set income value to zero and issue a warning.
        if (Utils.checkStringPositiveInteger(income, false)) {
            originalIncome = Integer.parseInt(income);
            annualSpendingAllowance = originalIncome;

            row = new Vector<>(3);
            row.add("Gross Income");
            row.add(Utils.formatIntoString((double) originalIncome / 12));
            row.add(Utils.formatString(income));
            summaryData.add(row);
        } else {
            originalIncome = 0;
            annualSpendingAllowance = 0;
            errorMessage.setText("Warning(s): Income is not valid / blank");
        }

        //Check if a tax table has been provided, if so; calculate the income tax that needs to be applied for the given income.
        if (!tax.isEmpty()) {
            double annualTax = 0;
            int lower;
            int upper;
            int per;
            int toTax;
            for (Vector<String> taxRow : tax) {
                lower = Integer.parseInt(taxRow.getFirst());
                upper = Integer.parseInt(taxRow.get(1));
                per = Integer.parseInt(taxRow.getLast());

                if (originalIncome >= lower) {
                    toTax = originalIncome - lower;
                    if (originalIncome > upper) {
                        toTax -= originalIncome - upper;
                    }
                    annualTax += (toTax * ((double)per/100));
                }
            }
            annualSpendingAllowance -= annualTax;
            row = new Vector<>(3);
            row.add("Income Tax");
            if (annualTax == 0) {
                row.add("0.00");
                row.add("0.00");
            } else {
                row.add("(" + Utils.formatIntoString(annualTax / 12) + ")");
                row.add("(" + Utils.formatIntoString(annualTax) + ")");
            }
            summaryData.add(row);
        } else {
            if (Objects.equals(errorMessage.getText(), originalErrorMessage)) {
                errorMessage.setText("Warning(s): Tax table is blank");
            } else {
                errorMessage.setText(errorMessage.getText() + ", Tax table is blank");
            }
        }

        //Check if an expenses table has been provided, if so; calculate the annual cost for each item and add it to the summary table.
        if (!expenses.isEmpty()) {
            float monthlyValue;
            double annualValue;
            for (Vector<String>r : expenses) {
                monthlyValue = Float.parseFloat(r.getLast());
                annualValue = monthlyValue * 12;
                annualSpendingAllowance -= annualValue;
                row = new Vector<>(3);
                row.add(r.getFirst());
                row.add("(" + Utils.formatString(r.getLast()) + ")");
                row.add("(" + Utils.formatIntoString(annualValue) + ")");
                summaryData.add(row);
            }
        } else {
            if (Objects.equals(errorMessage.getText(), originalErrorMessage)) {
                errorMessage.setText("Warning(s): Expense table is blank");
            } else {
                errorMessage.setText(errorMessage.getText() + ", Expense table is blank");
            }
        }
        double monthlySpending = (annualSpendingAllowance / 12);
        if (annualSpendingAllowance < 0) {
            monthlySpendingLabel.setText("(" + Utils.formatIntoString(Math.abs(monthlySpending)) + ")");
            monthlySpendingLabel.setForeground(Color.RED);
            annualSpendingLabel.setText("(" + Utils.formatIntoString(Math.abs(annualSpendingAllowance)) + ")");
            annualSpendingLabel.setForeground(Color.RED);

        } else {
            monthlySpendingLabel.setText(Utils.formatIntoString(monthlySpending));
            monthlySpendingLabel.setForeground(Color.BLACK);
            annualSpendingLabel.setText(Utils.formatIntoString(annualSpendingAllowance));
            annualSpendingLabel.setForeground(Color.BLACK);
        }

        //Update and correctly format the table & tableModel.
        tableModel.setDataVector(summaryData, columnNames);
        DefaultTableCellRenderer cellRenderer;
        for (int col = 0; col < table.getColumnModel().getColumnCount(); col++) {
            cellRenderer = new DefaultTableCellRenderer();
            if (col == 0) {
                table.getColumnModel().getColumn(col).setPreferredWidth(95);
                cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            } else {
                table.getColumnModel().getColumn(col).setMinWidth(80);
                cellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
            }
            table.getColumnModel().getColumn(col).setCellRenderer(cellRenderer);
        }
        table.setGridColor(Color.lightGray);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        table.updateUI();
    }
}