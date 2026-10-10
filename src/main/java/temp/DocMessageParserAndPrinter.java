package temp;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DocMessageParserAndPrinter {

    public static void printGridFromDoc(String url) throws Exception {
        List<String[]> rows = readTableRows(url);
        if (rows.isEmpty()) {
            return;
        }
        char[][] grid = buildGrid(rows);
        printGrid(grid);
    }

    private static List<String[]> readTableRows(String url) throws Exception {
        List<String[]> rows = new ArrayList<>();
        try (InputStream in = openStream(url);
             XWPFDocument doc = new XWPFDocument(in)) {

            for (XWPFTable table : doc.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    List<XWPFTableCell> cells = row.getTableCells();
                    if (cells.size() < 3) continue;

                    String xRaw = cells.get(0).getText().trim();
                    String charRaw = cells.get(1).getText();
                    String yRaw = cells.get(2).getText().trim();

                    if (!isInteger(xRaw) || !isInteger(yRaw) || charRaw.isEmpty()) {
                        continue;
                    }
                    rows.add(new String[]{xRaw, charRaw, yRaw});
                }
            }
        }
        return rows;
    }

    private static char[][] buildGrid(List<String[]> rows) {
        int maxX = 0, maxY = 0;
        for (String[] r : rows) {
            int x = Integer.parseInt(r[0]);
            int y = Integer.parseInt(r[2]);
            if (x > maxX) maxX = x;
            if (y > maxY) maxY = y;
        }
        char[][] grid = new char[maxY + 1][maxX + 1];
        for (char[] row : grid) Arrays.fill(row, ' ');

        for (String[] r : rows) {
            int x = Integer.parseInt(r[0]);
            int y = Integer.parseInt(r[2]);
            grid[maxY - y][x] = r[1].charAt(0);
        }
        return grid;
    }

    private static void printGrid(char[][] grid) {
        for (char[] row : grid) {
            System.out.println(new String(row));
        }
    }

    private static InputStream openStream(String url) throws IOException {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            try {
                return URI.create(url).toURL().openStream();
            } catch (IllegalArgumentException e) {
                throw new IOException("Invalid URL: " + url, e);
            }
        }
        return new FileInputStream(url);
    }

    private static boolean isInteger(String s) {
        if (s.isEmpty()) return false;
        int start = (s.charAt(0) == '-') ? 1 : 0;
        if (start == s.length()) return false;
        for (int i = start; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) return false;
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        String url = args.length > 0
                ? args[0]
                : "C:\\Users\\abhis\\Downloads\\temp.docx";
        printGridFromDoc(url);
    }
}
