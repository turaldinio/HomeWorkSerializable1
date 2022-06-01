import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        //       saveGame("C:\\Game\\savegames\\saveGame1.dat", new GameProgress(90, 9, 12, 2.4));
        //     saveGame("C:\\Game\\savegames\\saveGame2.dat", new GameProgress(110, 18, 121, 1.4));
        //   saveGame("C:\\Game\\savegames\\saveGame3.dat", new GameProgress(34, 3, 4, 3.5));

        //      List<String> gameProgressList = new ArrayList<>();

        //     gameProgressList.add("C:\\Game\\savegames\\saveGame1.dat");
        //    gameProgressList.add("C:\\Game\\savegames\\saveGame2.dat");
        //   gameProgressList.add("C:\\Game\\savegames\\saveGame3.dat");

        // zipFiles("C:\\Game\\savegames\\zip.zip", gameProgressList);

        //  openZip("C:\\Game\\savegames\\zip.zip", "C:\\Game\\savegames");

        System.out.println(openProgress("C:\\Game\\savegames\\saveGame1.dat"));

    }

    public static void saveGame(String filePath, GameProgress gameProgress) {
        try (
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath))
        ) {
            objectOutputStream.writeObject(gameProgress);

            objectOutputStream.flush();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void zipFiles(String filePath, List<String> filesForZip) {

        try (
                ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(filePath))) {
            for (String s : filesForZip) {

                File file = new File(s);

                try (FileInputStream inputStream = new FileInputStream(file)) {
                    ZipEntry zipEntry = new ZipEntry(file.getName());

                    zipOutputStream.putNextEntry(zipEntry);

                    byte[] array = new byte[inputStream.available()];
                    inputStream.read(array);

                    zipOutputStream.write(array);

                    zipOutputStream.closeEntry();

                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        deleteOtherFiles("C:\\Game\\savegames");
    }

    public static void deleteOtherFiles(String filePath) {
        boolean checkDeletedOtherFiles = true;
        File file = new File(filePath);
        for (File iterator : file.listFiles()) {
            if (!iterator.getName().endsWith(".zip")) {
                checkDeletedOtherFiles = checkDeletedOtherFiles & iterator.delete();
            }
        }
        if (checkDeletedOtherFiles) {
            System.out.println("Лишние файлы успешно удалены");
        }
    }

    public static void openZip(String filePath, String directoryName) {
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(filePath))
        ) {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {

                try (FileOutputStream outputStream = new FileOutputStream(directoryName + "\\" + zipEntry.getName())) {
                    while (zipInputStream.available() > 0) {
                        outputStream.write(zipInputStream.read());
                    }
                }


                zipEntry = zipInputStream.getNextEntry();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static GameProgress openProgress(String directoryName) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(directoryName))) {
            return (GameProgress) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
