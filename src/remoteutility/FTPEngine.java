package remoteutility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;

public class FTPEngine {

    private static FTPClient ftpClient;

    public FTPEngine(String hostname, int portno, String username, String password) throws IOException {

        ftpClient = new FTPClient();
        ftpClient.connect(hostname, portno);
        ftpClient.login(username, password);
    }

//    public boolean getFile(String remotePathWithFilename, String localPathWithFilename) throws FileNotFoundException, IOException {
//        System.out.println("Processing the Data");
//        File localfile = new File(localPathWithFilename);
//
//        if (!localfile.getParentFile().exists()) {
//
//            Files.createDirectory(Paths.get(localfile.getParent()));
//        }
//        InputStream fis = ftpClient.retrieveFileStream(remotePathWithFilename);
//        FileOutputStream fos = new FileOutputStream(localfile);
//
//        byte[] bytesArray = new byte[5120];
//        int bytesRead = -1;
//        while ((bytesRead = fis.read(bytesArray)) != -1) {
//            fos.write(bytesArray, 0, bytesRead);
//        }
//        
//
//        fis.close();
//        fos.close();
//        boolean status = ftpClient.completePendingCommand();
////        System.out.println("Println: " + status);
//
//        return status;
//
//    }
    public boolean getFile(String remotePathWithFilename, String localPathWithFilename) throws FileNotFoundException, IOException {
        System.out.println("Processing the Data");
        File localfile = new File(localPathWithFilename);
        System.out.println("Local Path: " + localfile.getParentFile());
        if (!localfile.getParentFile().exists()) {
            System.out.println("Create Directory Called");
            Files.createDirectory(Paths.get(localfile.getParent()));
        }

        boolean status = false;

        status = ftpClient.retrieveFile(remotePathWithFilename, new FileOutputStream(localPathWithFilename));

        System.out.println("FTP File Download Process Completed");
        return status;

    }

    public boolean checkFileExists(String remoteName) throws IOException {
        if (ftpClient.retrieveFileStream(remoteName) != null) {
            return true;
        }

        return false;

    }

    public String getRemoteModTimeStamp(String fileName) throws IOException {
        String timeStamp = ftpClient.getModificationTime(fileName);

        return timeStamp.substring(timeStamp.indexOf(" ") + 1, timeStamp.length()).trim();
    }

//    public static void main(String[] args) {
//
//        FTPEngine ftpobj = null;
//
//        try {
//            //        try {
//
//            ftpobj = new FTPEngine("mdc1vr1002", 21, "scripts", "st@rs1");
//            System.out.println(ftpClient.retrieveFileStream("rfilePath"));
////            boolean success = ftpobj.getFile("/data/aggregator/test_preview/aggregator_output/site/category.csv", "E:/category12.csv");
////            System.out.println("Time : " + ftpobj.getRemoteModTimeStamp("/data/aggregator/test_preview/aggregator_output/site/category1212.csv"));
//        } catch (IOException ex) {
//            Logger.getLogger(FTPEngine.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
////            if (!success) {
////                System.out.println("Ftp to download the file unsuccessful.");
////            } else {
////                System.out.println("Ftp to download the file successful.");
////            }
////        } catch (IOException ex) {
////            Logger.getLogger(FTPEngine.class.getName()).log(Level.SEVERE, null, ex);
////        } finally {
////            try {
////
////                if (ftpobj.ftpClient.isConnected()) {
////                    ftpobj.ftpClient.logout();
////                    ftpobj.ftpClient.disconnect();
////                }
////            } catch (IOException ex) {
////                ex.printStackTrace();
////            }
////        }
//    }
}
