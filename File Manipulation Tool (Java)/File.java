import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class FileManipulationTool {
    public static void main(String[] args) {
        try {
            listRootDrives();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Method to list all root drives in the system
    public static void listRootDrives() throws IOException {
        Scanner sc = new Scanner(System.in);

        while (true) {
            // Get the root directories
            File[] roots = File.listRoots();
            System.out.println("Root Drives:");
            // Display the root directories
            for (int i = 0; i < roots.length; i++) {
                System.out.println((i + 1) + ". " + roots[i]);
            }
            System.out.print("Enter drive no or 'exit': ");
            String userInput = sc.nextLine().trim().toLowerCase();
            if (userInput.equals("exit")) {
                System.out.println("Thanks for using this file manipualtion tool");
                break;
            } else {
                try {
                    int selection = Integer.parseInt(userInput);
                    // Validate user input and list folders and files for the selected root drive
                    if (selection >= 1 && selection <= roots.length) {
                        listFoldersAndFiles(roots[selection - 1].toString(), null);
                    } else {
                        System.out.println("Invalid input, please try again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input,please try again.");
                }
            }
        }
    }

    // Method to list all folders and files in the specified directory
    public static void listFoldersAndFiles(String directoryPath, String parentDirectoryPath) throws IOException {
        List<String> folders = new ArrayList<>();
        List<String> files = new ArrayList<>();

        // Read the contents of the directory
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directoryPath))) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    folders.add(entry.getFileName().toString());
                } else if (Files.isRegularFile(entry)) {
                    files.add(entry.getFileName().toString());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading this directory: " + e.getMessage());
            return;
        }

        // Sort folders and files alphabetically
        Collections.sort(folders);
        Collections.sort(files);

        // Display the current directory
        System.out.println("Current Directory: " + directoryPath);

        // Display folders
        System.out.println("Folders:");
        if (folders.isEmpty()) {
            System.out.println("No folders in this directory.");
        } else {
            for (int i = 0; i < folders.size(); i++) {
                System.out.println((i + 1) + ". " + folders.get(i));
            }
        }

        // Display files
        System.out.println("Files:");
        if (files.isEmpty()) {
            System.out.println("No files in this directory.");
        } else {
            for (int i = 0; i < files.size(); i++) {
                System.out.println((i + folders.size() + 1) + ". " + files.get(i));
            }
        }

        // Prompt for user input
        System.out.print("Enter folder number, 'prev' to go back, or 'exit': ");
        Scanner sc = new Scanner(System.in);
        while (true) {
            String userInput = sc.nextLine().trim().toLowerCase();
            if (userInput.equals("exit")) {
                System.out.println("Thanks for using this file manipualtion tool");
                break;
            } else if (userInput.equals("prev")) {
                // Go back to the parent directory if available
                if (parentDirectoryPath != null) {
                    listFoldersAndFiles(parentDirectoryPath, Paths.get(parentDirectoryPath).getParent() == null ? null : Paths.get(parentDirectoryPath).getParent().toString());
                } else {
                    System.out.println("Already in the root directory");
                }
                break;
            } else {
                try {
                    int selection = Integer.parseInt(userInput);
                    // Handle folder selection
                    if (selection >= 1 && selection <= folders.size()) {
                        listFoldersAndFiles(Paths.get(directoryPath, folders.get(selection - 1)).toString(), directoryPath);
                        break;
                    // Handle file selection
                    } else if (selection > folders.size() && selection <= folders.size() + files.size()) {
                        handleFileOptions(Paths.get(directoryPath, files.get(selection - folders.size() - 1)).toString());
                        break;
                    } else {
                        System.out.println("Invalid input, please try again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input, please try again.");
                }
            }
        }
    }

    // Method to handle file operations like copy, move, delete, and concatenate
    public static void handleFileOptions(String filePath) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Options for file '" + filePath + "':\n1. Copy\n2. Move\n3. Delete\n4. Concatenate");
        System.out.print("Enter option number or 'prev': ");

        while (true) {
            String userInput = sc.nextLine().trim().toLowerCase();
            if (userInput.equals("prev")) {
                return;
            } else {
                try {
                    int option = Integer.parseInt(userInput);
                    switch (option) {
                        case 1:
                            System.out.print("Enter destination directory for copy: ");
                            copyFile(filePath, sc.nextLine().trim());
                            break;
                        case 2:
                            System.out.print("Enter destination directory for move: ");
                            moveFile(filePath, sc.nextLine().trim());
                            break;
                        case 3:
                            deleteFile(filePath);
                            break;
                        case 4:
                            System.out.print("Enter extension to filter files (e.g., txt): ");
                            concatenateFilesWithExtension(filePath, sc.nextLine().trim().toLowerCase());
                            break;
                        default:
                            System.out.println("Invalid option, please try again.");
                    }
                    return;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid option, please try again.");
                }
            }
        }
    }

    // Method to copy a file from source to destination
    public static void copyFile(String sourcePath, String destinationPath) throws IOException {
        Path source = Paths.get(sourcePath);
        Path destination = Paths.get(destinationPath, source.getFileName().toString());
        try {
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied successfully to: " + destination);
        } catch (IOException e) {
            System.err.println("Error copying file: " + e.getMessage());
        }
    }

    // Method to move a file from source to destination
    public static void moveFile(String sourcePath, String destinationPath) throws IOException {
        Path source = Paths.get(sourcePath);
        Path destination = Paths.get(destinationPath, source.getFileName().toString());
        try {
            Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File moved successfully to: " + destination);
        } catch (IOException e) {
            System.err.println("Error moving file: " + e.getMessage());
        }
    }

    // Method to delete a specified file
    public static void deleteFile(String filePath) throws IOException {
        try {
            Files.delete(Paths.get(filePath));
            System.out.println("File deleted successfully: " + filePath);
        } catch (IOException e) {
            System.err.println("Error deleting file: " + e.getMessage());
        }
    }

    // Method to concatenate files with a specific extension into one file
    public static void concatenateFilesWithExtension(String sourcePath, String extension) throws IOException {
        Path sourceDir = Paths.get(sourcePath).getParent();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(sourceDir, path -> path.toString().endsWith("." + extension))) {
            List<Path> filesToConcatenate = new ArrayList<>();
            stream.forEach(filesToConcatenate::add);
            if (filesToConcatenate.isEmpty()) {
                System.out.println("No files with extension '" + extension + "' found.");
                return;
            }
            Path outputFilePath = sourceDir.resolve("concatenated." + extension);
            try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath, StandardOpenOption.CREATE)) {
                for (Path file : filesToConcatenate) {
                    Files.lines(file).forEach(line -> {
                        try {
                            writer.write(line);
                            writer.newLine();
                        } catch (IOException e) {
                            System.err.println("Error writing to file: " + e.getMessage());
                        }
                    });
                }
                System.out.println("Files with extension '" + extension + "' concatenated successfully to: " + outputFilePath);
            }
        } catch (IOException e) {
            System.err.println("Error concatenating files: " + e.getMessage());
        }
    }
}
