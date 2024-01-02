package GUI;

import java.awt.*;
import java.util.Vector;
import javax.swing.*;

public class RootGUI {
    protected JFrame frame;
    protected JTabbedPane tabbedPane;

    IncomeTaxGUI incomeTaxGUI;
    ExpensesGUI expensesGUI;
    SummaryGUI summaryGUI;

    final String incomeFilePath;
    final String taxFilePath;
    final String expensesFilePath;

    public RootGUI() {
        incomeFilePath = "data\\Income.csv";
        taxFilePath = "data\\TaxBands.csv";
        expensesFilePath = "data\\Expenses.csv";
        incomeTaxGUI = new IncomeTaxGUI(incomeFilePath, taxFilePath);
        expensesGUI = new ExpensesGUI(expensesFilePath);
        summaryGUI = new SummaryGUI(incomeFilePath, taxFilePath, expensesFilePath);
        generateFrame();
    }

    private void generateFrame() {
        //Creating all relevant tab objects, then generating the frame.
        frame = new JFrame();
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Income & Tax", null, incomeTaxGUI.getPanel(),
                "Add annual income & update tax bands here...");

        tabbedPane.addTab("Expenses", null, expensesGUI.getPanel(),
                "Account for monthly expense here...");

        tabbedPane.addTab("Report", null, summaryGUI.getPanel(),
                "View full report here...");

        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 2) {
                String income = incomeTaxGUI.getIncomeData();
                Vector<Vector<String>> taxTable = incomeTaxGUI.getTaxData();
                Vector<Vector<String>> expensesTable = expensesGUI.getExpensesData();
                summaryGUI.updateSummary(income, taxTable, expensesTable);
            }
        });

        frame.add(tabbedPane);
        frame.setTitle("Budgeting Tool");
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);

        //Checking screen size, if screen width is below 1400 px: Maximise the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (screenSize.getWidth() < 1400) {
            frame.setSize(500, 550);
            frame.setLocationRelativeTo(null);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        }
        else {
            frame.setSize(500, 550);
            frame.setLocationRelativeTo(null);
        }
        frame.add(tabbedPane);
        frame.setVisible(true);
    }
}