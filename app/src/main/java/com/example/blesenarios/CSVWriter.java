package com.example.blesenarios;
//http://codesfor.in/how-to-export-sqlite-database-to-a-csv-file/
import java.io.PrintWriter;
import java.io.Writer;


class CSVWriter {

    private PrintWriter pw;
    private char separator;
    private char escapechar;
    private String lineEnd;
    private char quotechar;

    private static final char DEFAULT_SEPARATOR = ',';
    private static final char NO_QUOTE_CHARACTER = '\u0000';
    private static final char NO_ESCAPE_CHARACTER = '\u0000';
    private static final String DEFAULT_LINE_END = "\n";
    private static final char DEFAULT_QUOTE_CHARACTER = '"';
    private static final char DEFAULT_ESCAPE_CHARACTER = '"';

    CSVWriter(Writer writer) {
        this(writer, DEFAULT_SEPARATOR, DEFAULT_QUOTE_CHARACTER,
                DEFAULT_ESCAPE_CHARACTER, DEFAULT_LINE_END);
    }

    private CSVWriter(Writer writer, char separator, char quotechar, char escapechar, String lineEnd) {
        this.pw = new PrintWriter(writer);
        this.separator = separator;
        this.quotechar = quotechar;
        this.escapechar = escapechar;
        this.lineEnd = lineEnd;
    }

    void writeNext(String[] nextLine) {

        if (nextLine == null)
            return;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nextLine.length; i++) {

            if (i != 0) {
                sb.append(separator);
            }

            String nextElement = nextLine[i];
            if (nextElement == null)
                continue;
            if (quotechar != NO_QUOTE_CHARACTER)
                sb.append(quotechar);
            for (int j = 0; j < nextElement.length(); j++) {
                char nextChar = nextElement.charAt(j);
                if (escapechar != NO_ESCAPE_CHARACTER && nextChar == quotechar) {
                    sb.append(escapechar).append(nextChar);
                } else if (escapechar != NO_ESCAPE_CHARACTER && nextChar == escapechar) {
                    sb.append(escapechar).append(nextChar);
                } else {
                    sb.append(nextChar);
                }
            }
            if (quotechar != NO_QUOTE_CHARACTER)
                sb.append(quotechar);
        }

        sb.append(lineEnd);
        pw.write(sb.toString());

    }

    void close() {
        pw.flush();
        pw.close();
    }



}
