package com.belk.car.app.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.belk.car.app.exceptions.SftpConnectionException;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * @author AFUTXD3
 * 
 */
public class SFtpUtil {
	private static transient final Log log = LogFactory.getLog(SFtpUtil.class);
	private static final String HOST_KEY_CHECKING = "StrictHostKeyChecking";

	public static Session openSftpSession(String host, String username,
			String password, int port, String hostKey)
			throws SftpConnectionException {
		// Connect and logon to SFTP Server
		JSch jsch = new JSch();
		Session session;
		try {
			session = jsch.getSession(username, host, port);

			session.setPassword(password);
			session.setConfig(HOST_KEY_CHECKING, hostKey);
			session.connect();
			// check if connect was successful
			if (session.isConnected()) {
				if (log.isDebugEnabled()) {
					log.debug("Connected to sucessfully to server :" + host);
				}
			} else {
				log.debug("Connection Failed" + host);
			}
		} catch (JSchException e) {
			log.error("Connection Failed" + host + " Error:" + e.getMessage(),
					e);
			throw new SftpConnectionException(e.getMessage(), e);
		} catch (Exception e) {
			log.error("Connection Failed" + host + " Error:" + e.getMessage(),
					e);
			throw new SftpConnectionException(e.getMessage(), e);
		}
		{

			return session;
		}
	}

	public static void sendDataFiles(ChannelSftp channelSftp,
			Collection<File> files, String destinationFolder) throws SftpConnectionException {
		if (channelSftp == null) {
			log.debug("SFtp Connection is not established or invalid.  Please call openConnection before calling this method!!");
			return;
		}
		if (files == null) {
			log.debug("No files to send!!!"); 
			return;
		}
		
		log.info("Destination Folder: " + destinationFolder);
		
		List<File> filesToProcess = new ArrayList<File>(files);
		InputStream iputFile = null;
		for (File file : filesToProcess) {
			try {
				if (file.isDirectory()) {
					filesToProcess.addAll(Arrays.asList(file.listFiles()));
					log.info("CES directory:" + file.getName()+"filesToProcess.size()"+filesToProcess.size());
					continue;
				}
				
				iputFile = new FileInputStream(file);
				try {
					channelSftp.put(iputFile, destinationFolder+file.getName());
				} catch (Exception ex) {
					log.error("SFTP:  FAILED for file " + file.getName(), ex);
				}
				if (log.isInfoEnabled())
					log.info("SFTP:  SUCCESS for file: " + file.getName());
			} catch (FileNotFoundException ex) {
				Logger.getLogger(SFtpUtil.class.getName()).log(Level.SEVERE,
						null, ex);
			} finally {
				try {
					if (iputFile != null)
						iputFile.close();
				} catch (IOException ex) {
					Logger.getLogger(SFtpUtil.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			}
		}

	}

	public static void closeSftpConnection(Session session,
			ChannelSftp sftpChannel) {
		try {
			sftpChannel.disconnect();
			session.disconnect();
		} catch (Exception e) {
			Logger.getLogger(SFtpUtil.class.getName()).log(Level.SEVERE, null,
					e);
		}

	}


}
