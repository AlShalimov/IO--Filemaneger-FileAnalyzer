package com.shalimov.filemanager;

import java.io.*;

public class FileManager {


    public static void main(String[] args) {
        System.out.println(countFiles("C:\\Windows"));
    }

    public static int countFiles(String path) {
        File filePath = new File(path);
        int count;
        count = fileSaver(filePath);
        return count;
    }

    public static int countDirs(String path) {
        int count = 0;
        File dir = new File(path);
        for (File element : dir.listFiles()) {
            if (element.isDirectory()) {
                count++;
                count += countDirs(element.toString());
            }
        }
        return count;
    }

    public static void copy(String from, String to) {
        File pathFrom = new File(from);
        File pathTo = new File(to);
        copyFileContent(pathFrom, pathTo);
    }


    public static void move(String from, String to) {
        File pathFrom = new File(from);
        File pathTo = new File(to, pathFrom.getName());
        pathFrom.renameTo(pathTo);
    }

    public static void clean(File file) {
        File[] list = file.listFiles();
        if (list == null) {
            throw new NullPointerException("File is not exist");
        }
        for (File files : list) {
            if (files.isDirectory()) {
                File newDir = new File(file, files.getName());
                clean(newDir);
            }
            files.delete();
        }
        file.delete();
    }

    private static void copyFileContent(File from, File to) {
        if (from.isDirectory()) {
            if (!to.exists()) {
                to.mkdir();
            }
            String[] files = from.list();
            if (files != null) {
                for (String file : files) {
                    copyFileContent(new File(from, file), new File(to, file));
                }
            }
        } else {
            try (FileInputStream fileInputStream = new FileInputStream(from);
                 FileOutputStream fileOutputStream = new FileOutputStream(to)) {
                int length;
                byte[] buffer = new byte[1024];
                while ((length = fileInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, length);
                }
            } catch (IOException exception) {
                throw new RuntimeException("Cant read fileContent", exception);
            }
        }
    }

    private static int fileSaver(File path) {
        int count = 0;
        File[] files = path.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    count += fileSaver(file);
                } else if (file.isFile()) {
                    count++;
                }
            }
        }
        return count;
    }
}
