package libraryManagement.utils;

import libraryManagement.models.Book;
import libraryManagement.models.Member;

import java.io.*;
import java.util.List;

public class DataExporter {

    public static void exportBooksToCSV(List<Book> books, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Book ID,Title,Author,Genre,Available Copies");
            writer.newLine();

            for (Book book : books) {
                writer.write(String.format("%d,%s,%s,%s,%d",
                        book.getBookId(),
                        escapeCSV(book.getTitle()),
                        escapeCSV(book.getAuthor()),
                        escapeCSV(book.getGenre()),
                        book.getAvailableCopies()));
                writer.newLine();
            }

            writer.flush();
            LogTracker.log("Books exported to " + filePath);
        } catch (IOException e) {
            LogTracker.log("Error exporting books: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void exportMembersToCSV(List<Member> members, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Member ID,Name,Email,Phone,Points");
            writer.newLine();

            for (Member member : members) {
                writer.write(String.format("%d,%s,%s,%s,%d",
                        member.getMemberId(),
                        escapeCSV(member.getName()),
                        escapeCSV(member.getEmail()),
                        escapeCSV(member.getPhone()),
                        member.getPoints()));
                writer.newLine();
            }

            writer.flush();
            LogTracker.log("Members exported to " + filePath);
        } catch (IOException e) {
            LogTracker.log("Error exporting members: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String escapeCSV(String value) {
        if (value == null) return "";
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
