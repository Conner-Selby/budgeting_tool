package Tools;

import java.io.*;
import java.util.Arrays;
import java.util.Vector;

public class CSVReadWrite {
    final static String COMMA_DELIMITER = ",";

    public static Vector<Vector<String>> readCSV(String filePath, Vector<String> columnNames) {
        /*
        Returns a data vector from a given file. Returns null if file not found, throws error if file is found but dimensions are invalid.
        */
        Vector<Vector<String>> vector2D;
        Vector<String> rowVector;
        String rowAsString;
        int rowCount = 0;
        int expectedColumnCount = columnNames.size();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) { //Check if the file exists, if so count the rows of data
            while ((br.readLine()) != null) {
                rowCount++;
            }
        } catch (IOException e) {
            return null;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) { //Re-read the file, having now constructed a 2D vector the correct size for the data.
            // Note: While this wasn't required when using vectors it would've been using Arrays in order to avoid constant Array re-writing.
            vector2D = new Vector<>(rowCount);
            while ((rowAsString = br.readLine()) != null) {
                rowVector = new Vector<>(expectedColumnCount);
                rowVector.addAll(Arrays.asList(rowAsString.split(COMMA_DELIMITER)));
                if (rowVector.size() != expectedColumnCount) {
                    throw new IndexOutOfBoundsException("Error loading from \"" + filePath + "\": CSV table column dimension (" + rowVector.size() + ") does not match expected dimension (" + expectedColumnCount + ") Attempted parsing: " + rowAsString);
                }
                vector2D.add(rowVector);
            }
        } catch (IOException e) { //If file is not found, return null
            return null;
        }
        return vector2D;
    }

    public static String readCSV(String filePath) {
        /*
        Returns the first line of data from a given file. Returns null if file not found.
        */
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) { //Check if the file exists, if so return the first row as a String.
            return br.readLine();
        } catch (IOException e) { //If file is not found, return null
            return null;
        }
    }

    public static boolean writeCSV(String filePath, Vector<Vector<String>> data) {
        /*
        Returns true if successful, otherwise false. Creates a temporary file and writes all given data to it. If writing is successful then the file will be renamed to the given filePath name, replacing an existing file if necessary.
         */
        File f = new File(filePath);
        File temp = new File(filePath + "temp.csv"); //Define temporary file (To attempt data population).
        Vector<String> row;
        try { //Attempt to populate the temp file with the given data
            if (temp.isFile()) { //If the requested temp filename already exists; delete it. Once it is a known free namespace; create the temp file.
                if (!temp.delete()) {
                    return false;
                }
            }
            if (temp.createNewFile()) {
                FileWriter writer = new FileWriter(temp);
                for (int r = 0; r < data.size(); r++) {
                    row = data.get(r);
                    StringBuilder rowString = new StringBuilder();
                    for (int c = 0; c < row.size(); c++) {
                        if (c == 0) {
                            rowString.append(row.get(c));
                        } else {
                            rowString.append(",");
                            rowString.append(row.get(c));
                        }
                    }
                    if (r != 0) {
                        writer.write('\r');
                    }
                    writer.write(rowString.toString());
                }
                writer.close();
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
        if (f.isFile()) { //If the requested filename already exists; delete it. Once it is a known free namespace; rename the successfully created temp -> requested name
            if (f.delete()) {
                return temp.renameTo(f);
            } else {
                return false;
            }
        } else {
            return temp.renameTo(f);
        }
    }

    public static boolean writeCSV(String filePath, String data) {
        /*
        Returns true if successful, otherwise false. Creates a temporary file and writes all given data to it. If writing is successful then the file will be renamed to the given filePath name, replacing an existing file if necessary.
         */
        File f = new File(filePath);
        File temp = new File(filePath + "temp.csv"); //Define temporary file (To attempt data population).

        try { //Attempt to populate the temp file with the given data.
            if (temp.isFile()) { //If the requested temp filename already exists; delete it. Once it is a known free namespace; create the temp file.
                if (!temp.delete()) {
                    return false;
                }
            }
            if (temp.createNewFile()) {
                FileWriter writer = new FileWriter(temp);
                writer.write(data);
                writer.close();
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
        if (f.isFile()) { //If the requested filename already exists; delete it. Once it is a known free namespace; rename the successfully created temp -> requested name.
            if (f.delete()) {
                return temp.renameTo(f);
            } else {
                return false;
            }
        } else {
            return temp.renameTo(f);
        }
    }
}