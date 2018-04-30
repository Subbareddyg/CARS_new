package com.belk.car.app.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;

import oracle.net.aso.i;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;

import com.belk.car.app.Constants;
import com.belk.car.app.model.Config;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.impl.QuartzJobManagerImpl;
import com.belk.car.util.DateUtils;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

/**
 * 
 * @author Yogesh.Vedak
 *
 */
public class BMIFileGenerationUtil {

	private static String NEW_LINE = "\r\n";
	private static transient final Log log = LogFactory.getLog(BMIFileGenerationUtil.class);


	/*
	 * construct record of format:
	 * S|OBJECT_ATTRIBUTE|SKU|{BELK_UPC}|ATTR NAME|{ATTR_VALUE}
	 */       
	public static String getObjectAttributeImportRecord(String type, String belkSkuUpc, String atrName, String atrVal) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constants.ELEMENT_S+"|"+Constants.ELEMENT_OBJ_ATTR+"|"); 
		sb.append(type);
		sb.append("|");
		sb.append(belkSkuUpc);
		sb.append("|");
		sb.append(atrName);
		sb.append("|");
		sb.append(atrVal);  
		//sb.append(NEW_LINE);
		return sb.toString();
	}

	public static void writeToBMIFile(String recordAsString,File bmiFile) throws IOException{
		if(log.isDebugEnabled()){
			log.debug(bmiFile+"----------**-----Text is about to be written into file --**------------\n"+recordAsString);
		}
		BufferedWriter bw = null;
		FileWriter fw = null;
		try
		{
			fw = new FileWriter(bmiFile,true);
			bw = new BufferedWriter(fw);
			bw.write(recordAsString);     
		}
		catch (IOException ioe) 
		{
			System.out.println(ioe);
			throw ioe;
		} 
		finally
		{
			try
			{
				/*  if (fw != null)
		          {
		        	 fw.flush();
		        	 fw.close();
		          }  */
				if (bw != null)
				{
					bw.flush();
					bw.close();
				}
			}catch (IOException iex) {
				if(log.isDebugEnabled()){
					log.debug("Error Occurred while writing following record in to the \""+bmiFile.getName()+"\" file:");
					log.debug("Record:\""+recordAsString+"\"");
				}
				iex.printStackTrace();
				throw iex;

			}
		}
		if(log.isDebugEnabled()){
			log.debug("----------**-----Text is written into file successfully-----**------------");
		}

	}

	public static void ftpFile(File fileToExport,Config ftpHost,Config ftpUserId,Config ftpPassword,Config ftpRemoteDir) throws IOException{
		if(ftpHost != null && StringUtils.isNotBlank(ftpHost.getValue())) {
			FTPClient ftp = FtpUtil.openConnection(ftpHost.getValue(), ftpUserId.getValue(), ftpPassword.getValue());
			FtpUtil.setTransferModeToAscii(ftp);
			FtpUtil.sendDataFiles(ftp, Arrays.asList(fileToExport), ftpRemoteDir.getValue());
			FtpUtil.closeConnection(ftp);
		}

	}
	
	/**
	 * Method to sFtp the file to the remote directory specified.
	 * 
	 * @param fileToExport
	 * @param ftpHost
	 * @param ftpUserId
	 * @param ftpPassword
	 * @param ftpRemoteDir
	 */
	public static void sFtpFile(File fileToExport,Config ftpHost,Config ftpUserId,Config ftpPassword,Config ftpRemoteDir){
	    if(ftpHost != null && StringUtils.isNotBlank(ftpHost.getValue()) 
	            && ftpUserId!=null && StringUtils.isNotBlank(ftpUserId.getValue()) && 
	            ftpPassword!=null && StringUtils.isNotBlank(ftpPassword.getValue()) && ftpRemoteDir!=null && StringUtils.isNotBlank(ftpRemoteDir.getValue())) {
            try  {
                Session session =SFtpUtil.openSftpSession(ftpHost.getValue(), ftpUserId.getValue(), ftpPassword.getValue(), 22, "no");
                Channel channel = session.openChannel("sftp");
                channel.connect();
                ChannelSftp  sftpChannel = (ChannelSftp) channel;
                SFtpUtil.sendDataFiles(sftpChannel, Arrays.asList(fileToExport), ftpRemoteDir.getValue());
                SFtpUtil.closeSftpConnection(session,sftpChannel);
            }catch(Exception e) {
                log.error(e.getMessage(),e);
            }
	    }
	}

	public static File getFile(String exportFileName,String exportDirPath) throws IOException{
		File dir = new File(exportDirPath);
		if (!dir.exists())
			dir.mkdirs();

		// Create a File object
		String fileName = exportFileName;
		String filePath = exportDirPath + fileName;
		File file = new File(filePath);

		if (!file.exists()) {
			// To Create file on disk (if it doesn't exist)
			file.createNewFile();
		}
		return file;
	}

	/**
	 * This method takes current dir path and file name from arguments and creates the file at new location specified at 'destinationDir' argument by appending current time stamp to the name.
	 * @param filePath
	 * @param fileName
	 * @param destinationDir
	 * @param copyFile
	 * @return
	 */
	public static boolean processCompletedFile(String filePath, String fileName, String destinationDir, boolean copyFile,boolean appendTimeStamp) {

		log.debug("File Path: "+filePath+" fileName: "+fileName+" destDir: "+destinationDir);
		// The File to be moved
		File currentFile = new File(filePath + fileName);
		StringBuffer sb = new StringBuffer();
		String destFileName = sb.append(filePath).append(destinationDir).append("/").append(appendTimeStamp?DateUtils.formatDate(new Date(), "MM_dd_yyyy_HH_mm_ss").concat("_"):"")
				.append(fileName).toString();

		if (log.isDebugEnabled()){
			log.debug((copyFile ? "Copying" : "Moving") + " file from: " + currentFile + " to " + destFileName);
		}

		//Directory to be created if it doesn't exist
		File destDir = new File(filePath + destinationDir);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		if (copyFile) {
			try {
				FileUtils.copyFile(currentFile, new File(destFileName));
			} catch (IOException ioex) {
				log.info("Exception in copying file");
				ioex.printStackTrace();
			}
		} else {
			File destFile = new File(destFileName);
			boolean ren = currentFile.renameTo(destFile);
			if (!ren){
				log.info("Could not rename file: " + currentFile.getAbsolutePath() + " to " + destFile.getAbsolutePath());
			}
		}

		return true;
	}

	public class FileSession {
		File file;
		BufferedWriter writer;
		public FileSession (String directory, String fileName) throws IOException {
			File f = new File(directory);
			if (!f.exists()) {
				f.mkdirs();
			}
			String absolutePath = directory+File.separator+fileName;
			file = new File(absolutePath);
			writer = new BufferedWriter(new FileWriter(absolutePath));
		}
		public FileSession(File file) throws IOException {
			this.file = file;
			new BufferedWriter(new FileWriter(file));
		}

		public void write(String s) throws IOException {
			write(s, true);
		}

		public void write(String s, boolean newLine) throws IOException {
			writer.write(s);
			if(newLine) {
				writer.newLine();
			}
		}

		public void close() throws IOException {
			writer.flush();
			writer.close();
		}

		public File getFile() {
			return file;
		}
		
		public void flush()throws IOException{
		    writer.flush();
		}
	}
	public static FileSession getFileSession(File file) throws IOException {
		return new BMIFileGenerationUtil().new FileSession(file);
	}

	public static FileSession getFileSession(String directory, String fileName) throws IOException {
		return new BMIFileGenerationUtil().new FileSession(directory, fileName);
	}

	public static BufferedWriter getWriter(String directory, String fileName) throws IOException {
		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return new BufferedWriter(new FileWriter(directory+File.separator+fileName));
	}

	public static void writeToBMIFile(BufferedWriter writer, String record) throws IOException{
		writeToBMIFile(writer, record, true);
	}

	public static void writeToBMIFile(BufferedWriter writer, String record, boolean newLine) throws IOException{
		writer.write(record);
		if(newLine) {
			writer.newLine();
		}
	}

	public static void closeFile(BufferedWriter writer) throws IOException {
		writer.flush();
		writer.close();
	}	
}
