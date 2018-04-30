package com.belk.car.app.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FtpUtil {

	private static transient final Log log = LogFactory.getLog(FtpUtil.class);

	
	/**
	 * 
	 * @param server
	 * @param username
	 * @param password
	 * @return
	 * @throws IOException
	 */
	public static FTPClient openConnection(String server, String username, String password) throws IOException {
		// Connect and logon to FTP Server
		FTPClient ftp = new FTPClient();

		//connect to the server
		ftp.connect(server);

		//check if connect was successful
	    if(FTPReply.isPositiveCompletion(ftp.getReplyCode())){
	    	log.debug("Connected Success");
	    	
	    	//Login to the server
		    ftp.login(username, password);
			if (log.isDebugEnabled()) {
				log.debug("Connected to " + server + ".");
				log.debug(ftp.getReplyString());
			}
        }else {
        	log.debug("Connection Failed");
            ftp.disconnect();
        }

		return ftp ;
	}
	/**
	 * This method checks the for FTP connection.
	 * */
	public static boolean checkFTPConnection(String server, String username, String password) throws IOException {
		// Connect and logon to FTP Server
		FTPClient ftp = new FTPClient();
		//connect to the server
		ftp.connect(server);
		//check if connect was successful
	    if(FTPReply.isPositiveCompletion(ftp.getReplyCode())){
		    return ftp.login(username, password);
        }else {
        	log.debug("Connection Failed");
        	return false;
        }
	}
	
	public static boolean checkFTPPath(String target, String targetUser,
			String targetPasswd, String destinationFolder) throws IOException {
		FTPClient ftp2 = new FTPClient();

		ftp2.connect(target);
		ftp2.login(targetUser, targetPasswd);
		
		Boolean test = ftp2.changeWorkingDirectory(destinationFolder);
		return test;
	}
	
	/**
	 * 
	 * @param ftp
	 * @throws IOException
	 */
	public static void closeConnection(FTPClient ftp) throws IOException {
		// Logout from the FTP Server and disconnect
		if (ftp != null) {
			ftp.logout();
			ftp.disconnect();
		}
		
	}
	
	/**
	 * 
	 * @param ftp
	 * @throws IOException
	 */
	public static void setTransferModeToAscii(FTPClient ftp) throws IOException {
		if (ftp != null)
			ftp.setFileTransferMode(FTPClient.ASCII_FILE_TYPE);
	}

	/**
	 * 
	 * @param ftp
	 * @throws IOException
	 */
	public static void setTransferModeToBinary(FTPClient ftp) throws IOException {
		if (ftp != null)
			ftp.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
	}

	/**
	 * 
	 * @param ftp
	 * @param folder
	 * @param destinationFolder
	 */
	public static void getDataFiles(FTPClient ftp, String folder, String destinationFolder, boolean deleteAfterGet) {
		getDataFiles(ftp, folder, destinationFolder, null, null, deleteAfterGet) ;
	}
	
	/**
	 * Method to compare the extension of files at FTP
	 * @return image files FTPFile[]
	 * @author afusy45
	 * @param FTPFile[] files
	 */
	public static FTPFile[] getImageFilesFromFTP(FTPFile[] files){
		 FTPFile[] imageFiles = new FTPFile[files.length];
		 List<String> extensionOfFiles = new ArrayList<String>();
			extensionOfFiles.add("jpg");
			extensionOfFiles.add("jpeg");
			extensionOfFiles.add("jpe");
			extensionOfFiles.add("jfif");
			extensionOfFiles.add("bmp");
			extensionOfFiles.add("dib");
			extensionOfFiles.add("tif");
			extensionOfFiles.add("tiff");
			extensionOfFiles.add("psd");
			extensionOfFiles.add("gif");
			extensionOfFiles.add("png"); 
			extensionOfFiles.add("xml");
			int j=0;
			for (int i = 0; i < files.length; i++) {
				String ext = files[i].getName().substring(files[i].getName().lastIndexOf('.')+1,files[i].getName().length());
				if(extensionOfFiles.contains(ext.toLowerCase())){
					imageFiles[j] = files[i];
					j++;
				}
			}
		return imageFiles;
	}

	/**
	 * Method to compare the extension of files at FTP
	 * @return image files FTPFile[]
	 * @author afusy45
	 * @param FTPFile[] files
	 */
	public static List<File> getImageFilesFromDirectory(File[] files){
		// File[] imageFiles = null;
		 List<File> imageFiles = new ArrayList<File>();
		 List<String> extensionOfFiles = new ArrayList<String>();
			extensionOfFiles.add("jpg");
			extensionOfFiles.add("jpeg");
			extensionOfFiles.add("jpe");
			extensionOfFiles.add("jfif");
			extensionOfFiles.add("bmp");
			extensionOfFiles.add("dib");
			extensionOfFiles.add("tif");
			extensionOfFiles.add("tiff");
			extensionOfFiles.add("psd");
			extensionOfFiles.add("gif");
			extensionOfFiles.add("png"); 
			int j=0;
			log.info("files.length"+files.length);
			for (int i = 0; i < files.length; i++) {
				String ext = files[i].getName().substring(files[i].getName().lastIndexOf('.')+1,files[i].getName().length());
				
				if(extensionOfFiles.contains(ext.toLowerCase())){
					imageFiles.add(files[i]);
					j++;
				}
			}
			log.info("in ftputil-"+imageFiles.size());
		return imageFiles;
	}
	
	/**
	 * 
	 * @param ftp
	 * @param folder
	 * @param destinationFolder
	 * @param start
	 * @param end
	 */
	public static void getDataFiles(FTPClient ftp, String folder, String destinationFolder, Calendar start, Calendar end, boolean deleteAfterGet) {
		try {

			//Check FTPClient
			if (ftp == null) {
				log.debug("Ftp Connection is not established or invalid.  Please call openConnection before calling this method!!") ;
				return ;
			}

			// List the files in the directory
			ftp.changeWorkingDirectory(folder);
			FTPFile[] files = ftp.listFiles();
			if (log.isDebugEnabled()) 
				log.debug("Number of files in dir: " + files.length);
			DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
			for (int i = 0; i < files.length; i++) {
				Date fileDate = files[i].getTimestamp().getTime();
				boolean downloadFile = true ;
				if (start != null && end != null) {
					downloadFile = (fileDate.compareTo(start.getTime()) >= 0 && fileDate.compareTo(end.getTime()) <= 0) ;
				} 
				if (downloadFile) {
					// Download a file from the FTP Server
					if (log.isDebugEnabled()) { 
						log.debug(df.format(files[i].getTimestamp().getTime()));
						log.debug("\t" + files[i].getName());
					}
					File file = new File(destinationFolder + File.separator + files[i].getName());
					FileOutputStream fos = new FileOutputStream(file);
					ftp.retrieveFile(files[i].getName(), fos);
					fos.close();
					file.setLastModified(fileDate.getTime());
					
					if (deleteAfterGet) {
						if (!ftp.deleteFile(files[i].getName())) {
							log.error("Unable to delete remote file " + files[i].getName() + ". ftp.deleteFile() returned false.");
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param ftp
	 * @param folder
	 * @param destinationFolder
	 * @param start
	 * @param end
	 */
	public static void sendDataFiles(FTPClient ftp, String folder, String destinationFolder) {
		sendDataFiles(ftp, Arrays.asList(new File(folder)), destinationFolder);
	}
	public static void sendDataFiles(FTPClient ftp, Collection<File> files, String destinationFolder ) {
		try {
			//Check FTPClient
			if (ftp == null) {
				log.debug("Ftp Connection is not established or invalid.  Please call openConnection before calling this method!!");
				return;
			}
			if (files == null) {
				log.debug("No files to send!!!");
				return;
			}

			if (destinationFolder != null) {
				log.debug("Destination Folder: " + destinationFolder);
				log.debug("Current Working Directory (Before): " + ftp.printWorkingDirectory());
				//ftp.cwd(destinationFolder) ;
				ftp.changeWorkingDirectory(destinationFolder);
				log.debug("Current Working Directory (After): " + ftp.pwd());
			}
			
			List<File> filesToProcess = new ArrayList<File>(files);
			for (int i = 0; i < filesToProcess.size(); i++) {
				FileInputStream fis = null;
				try {
					File f = filesToProcess.get(i);
					if (f.isDirectory()) {
						filesToProcess.addAll(Arrays.asList(f.listFiles()));
						continue;
					}

					fis = new FileInputStream(f);
					if (fis == null) {
						log.debug("Null input stream!!") ;
					}
					try {
						ftp.storeFile(f.getName(), fis);
					} catch (Exception ex) {
						log.error("FTP:  FAILED for file " + f.getName());
					}
					if (log.isInfoEnabled())
						log.info("FTP:  SUCCESS for file " + f.getName());
				} catch (FileNotFoundException ex) {
					Logger.getLogger(FtpUtil.class.getName()).log(Level.SEVERE, null, ex);
				} finally {
					try {
						if (fis != null)
							fis.close();
					} catch (IOException ex) {
						Logger.getLogger(FtpUtil.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			}
		}
		catch (IOException ex) {
			Logger.getLogger(FtpUtil.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
		
	/**
	 * Added for VIP
	 * Check whether image file exists on vendor FTP
	 * @param serverAddress
	 * @param userId
	 * @param password
	 * @param remoteDirectory
	 * @param remotefile
	 * @param localImageName
	 * @param localDirectory
	 * @return FTP failed reasons - dirNotFound,imageNotFound,unableToConnectFTP
	 */
	public static String downloadFTPImage(String serverAddress, String userId,
			String password, String remoteDirectory, String remotefile,
			String localImageName, String localDirectory) {
		String ftpResult="";
		FTPClient ftpClient;
		int port = 21;
		try {
			// connect to ftp
			ftpClient = connect(serverAddress, port, userId, password);
			// enter passive mode
			ftpClient.enterLocalPassiveMode();
			// Check directory exists
			if (remoteDirectory != null) {
				if (!checkDirectoryExists(ftpClient, remoteDirectory)) {
					log.error("Directory doesn't exist");
					ftpResult="dirNotFound";
				}
			}
			// Check whether image exists on server
			boolean imageExists = checkImageExists(ftpClient, remotefile);
			//download image on local dir
			if (imageExists) {
				// get output stream
				OutputStream output;
				output = new FileOutputStream(localDirectory + "/" + localImageName);
				// get the file from the remote system
				ftpClient.retrieveFile(remotefile, output);
				// close output stream
				output.close();
				ftpResult="success";
				log.info("image "+ remotefile+" successfully downloaded from FTP site " +serverAddress);
			} else {
				log.error("image not found on FTP" + remotefile);
				ftpResult="imageNotFound";
			}
		   //close connection
		  closeConnection(ftpClient);	
		} catch (Exception ex) {
			log.error("Error occured while downloading the image from ftp site"
					+ serverAddress);
			ftpResult="unableToConnectFTP";
		}
		return ftpResult;
	}

	/**
	 * Added for VIP
	 * Determines whether a directory exists or not
	 * 
	 * @param ftp
	 * @param dirPath
	 * @return true if exists, false otherwise
	 * @throws IOException
	 * thrown if any I/O error occurred.
	 */
	public static boolean checkDirectoryExists(FTPClient ftp, String dirPath)
			throws IOException {
		ftp.changeWorkingDirectory(dirPath);
		int returnCode = ftp.getReplyCode();
		if (returnCode == 550) {
			return false;
		}
		return true;
	}

	/**
	 * Added for VIP
	 * Determines whether a file exists or not
	 * 
	 * @param ftp
	 * @param filePath
	 * @return true if exists, false otherwise
	 * @throws IOException
	 *             thrown if any I/O error occurred.
	 */
	public static boolean checkImageExists(FTPClient ftp, String remotefile)
			throws IOException {
		FTPFile[] ftpFiles = ftp.listFiles();
		boolean fileExists = false;
		if (ftpFiles != null && ftpFiles.length > 0) {
			// loop through the files
			for (FTPFile file : ftpFiles) {
				if (!file.isFile()) {
					continue;
				}
				if (file.getName().equals(remotefile)) {
					fileExists = true;
					log.info("File is " + file.getName());
					break;
				}
			}
		}
		return fileExists;

	}
	
	/**
	 * Added for VIP
	 * Connects to a remote FTP server
	 * 
	 * @return
	 */
	public static FTPClient connect(String hostname, int port, String username,
			String password) throws SocketException, IOException {
		FTPClient ftpClient = new FTPClient();
		ftpClient.connect(hostname, port);
		int returnCode = ftpClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(returnCode)) {
			throw new IOException("Could not connect"+hostname);
		}
		boolean loggedIn = ftpClient.login(username, password);
		if (!loggedIn) {
			throw new IOException("Could not login"+hostname);
		}
		log.info("Connected and logged in.");

		return ftpClient;
	}

	public static void main(String[] args) {
		try {
			File log4JFile = FileUtils.toFile(ClassLoader.getSystemResource("log4j.xml"));
			File hibFile = FileUtils.toFile(ClassLoader.getSystemResource("hibernate.cfg.xml"));
			FTPClient ftpClient;
			ftpClient = FtpUtil.openConnection("162.27.10.164", "BelkMeta", "BelkJMS88");
			FtpUtil.setTransferModeToAscii(ftpClient);
			Collection<File> filesToSend = new HashSet<File>(Arrays.asList(log4JFile, hibFile));
			FtpUtil.sendDataFiles(ftpClient, filesToSend, "/BelkMeta/to_rrd");
		}
		catch (IOException ex) {
			Logger.getLogger(FtpUtil.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}