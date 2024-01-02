package Tools;

import java.awt.*;
import java.util.Vector;

public class Utils {
// -- Note -- Many overloaded functions here are not used, were implemented purely for demonstration and learning purposes.
    public static String formatString(String value) {
        /*
        Returns the string value with appropriate commas, and ensures two decimal places for consistency.
        */
        StringBuilder valueString = new StringBuilder(value);
        int index = value.indexOf('.');

        if (index == -1) {
            valueString.append(".00");
        } else {
            int decimalCount = valueString.length() - (index + 1);
            while (decimalCount < 2) {
                valueString.append("0");
                decimalCount++;
            }
        }
        index = valueString.indexOf(".");
        while (index > 3) {
            index -= 3;
            valueString.insert(index, ',');
        }

        return valueString.toString();
    }

    public static String formatIntoString (double value) {
        /*
        Returns a formatted string version of the given number.
        */
        double roundedValue = Math.round(value * 100.0) / 100.0;
        return formatString(Double.toString(roundedValue));
    }

    public static String formatIntoString (float value) { return formatIntoString((double) value); }

    public static String formatIntoString (int value) { return formatIntoString((double) value); }

    public static GridBagConstraints createGridBagConstraints(int gridX, int gridY, int gridWidth, int gridHeight, float weightX, float weightY) { //Template for a new generic GridBagConstraint
        /*
        Returns a new GridBagConstraints object with the most common parameters set (Could have been made more sophisticated, but it served its purpose and reduced the bloat of the GUI classes)
        */
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = gridX;            //Sets starting column (From left)
        constraints.gridy = gridY;            //Sets starting row (From top)
        constraints.gridwidth = gridWidth;    //Sets number of columns wide
        constraints.gridheight = gridHeight;  //Sets number of rows high
        constraints.weightx = weightX;        //Determines distribution of extra horizontal space
        constraints.weighty = weightY;        //Determines distribution of extra vertical space

        return constraints;
    }

    public static boolean check2DVectorPositiveFloat(Vector<Vector<String>> data, int columnIndexCheck, boolean raiseError) {
        /*
        Returns true if all specified values at the given index in 2D Vector are valid positive integers, otherwise false (If raiseError = True, false -> error).
        */
        String value;
        for (Vector<String> row : data) {
            value = row.get(columnIndexCheck);
            if (!(checkStringPositiveFloat(value, raiseError))) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkStringPositiveFloat(String value, boolean raiseError) {
        /*
        Returns true if the given string is a valid positive float, otherwise false (If raiseError = True, false -> error).
        */
        float valueAsFloat = -1;
        try {
            valueAsFloat = Float.parseFloat(value);
        } catch (NumberFormatException e) {
            if (raiseError) {
                throw e;
            } else {
                return false;
            }
        }
        if (valueAsFloat >= 0) {
            return true;
        } else {
            if (raiseError) {
                throw new NumberFormatException("Table Entry Error: Values inserted must be positive floats! (" + value + " is invalid)");
            } else {
                return false;
            }
        }
    }

    public static boolean check2DVectorPositiveInteger(Vector<Vector<String>> data, int[] columnIndexesCheck, boolean raiseError) {
        /*
        Returns true if all specified values in 2D Vector are valid positive integers, otherwise false (If raiseError = True, false -> error).
        */
        String value;
        for (Vector<String> row : data) {
            for (int columnIndex : columnIndexesCheck) {
                value = row.get(columnIndex);
                if (!(checkStringPositiveInteger(value, raiseError))) {
                    return false;
                }

            }
        }
        return true;
    }

    public static boolean check2DVectorPositiveInteger(Vector<Vector<String>> data, int columnIndexCheck, boolean raiseError) {
        /*
        Returns true if all specified values at index in 2D Vector are valid positive integers, otherwise false (If raiseError = True, false -> error).
        */
        String value;
        for (Vector<String> row : data) {
            value = row.get(columnIndexCheck);
            if (!(checkStringPositiveInteger(value, raiseError))) {
                return false;
            }

        }
        return true;
    }

    public static boolean check2DVectorPositiveInteger(Vector<Vector<String>> data, boolean raiseError) {
        /*
        Returns true if all values in 2D Vector are valid positive integers, otherwise false (If raiseError = True, false -> error).
        */
        for (Vector<String> row : data) {
            for (String value : row) {
                if (!(checkStringPositiveInteger(value, raiseError))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkVectorPositiveInteger(Vector<String> row, boolean raiseError) {
        /*
        Returns true if all values in vector are valid positive integers, otherwise false (If raiseError = True, false -> error).
        */
        for (String value : row) {
            if (!(checkStringPositiveInteger(value, raiseError))) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkVectorPositiveInteger(Vector<String> row, int[] columnIndexesCheck, boolean raiseError) {
        /*
        Returns true if all specified values in vector are valid positive integers, otherwise false (If raiseError = True, false -> error).
        */
        String value;
        for (int index : columnIndexesCheck) {
            value = row.get(index);
            if (!(checkStringPositiveInteger(value, raiseError))) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkVectorPositiveInteger(Vector<String> row, int columnIndexCheck, boolean raiseError) {
        /*
        Returns true if specified column value in vector is a valid positive integer, otherwise false (If raiseError = True, false -> error).
        */
        String value;
        value = row.get(columnIndexCheck);
        return checkStringPositiveInteger(value, raiseError);
    }

    public static boolean checkStringPositiveInteger(String value, boolean raiseError) {
        /*
        Returns true if the given string is a valid positive integer, otherwise false (If raiseError = True, false -> error).
        */
        int valueAsInt = -1;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            if (raiseError) {
                throw e;
            } else {
                return false;
            }
        }
        if (valueAsInt >= 0) {
            return true;
        } else {
            if (raiseError) {
                throw new NumberFormatException("Table Entry Error: Value must be a positive integer! (" + value + " is invalid)");
            } else {
                return false;
            }
        }
    }

    private static void checkIndexInsideBounds(Vector<String> columnNames, int[] columnIndexesCheck) {
        /*
        Will throw an error (Regardless of raiseError state) if any of the entered column indexes are outside the table dimension
        */
        int expectedColumnCount = columnNames.size();
        for (int index : columnIndexesCheck) {
            if (index < 0 || index >= expectedColumnCount) {
                throw new IndexOutOfBoundsException("Column index provided (" + index + ") is not within column bounds (0 - " + (expectedColumnCount - 1) + ")");
            }
        }
    }

    private static void checkIndexInsideBounds(Vector<String> columnNames, int columnIndexCheck) {
        /*
        Will always throw an error (Regardless of raiseError state) if the entered column index is outside the table dimension
        */
        int expectedColumnCount = columnNames.size();
        if (columnIndexCheck < 0 || columnIndexCheck >= expectedColumnCount) {
            throw new IndexOutOfBoundsException("Column index provided (" + columnIndexCheck + ") is not within column bounds (0 - " + (expectedColumnCount - 1) + ")");
        }
    }
}

