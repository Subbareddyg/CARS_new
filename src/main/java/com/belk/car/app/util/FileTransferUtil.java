package com.belk.car.app.util;

import java.io.File;
import java.util.Map;
import java.util.logging.Logger;

import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.transport.IgnoreHostKeyVerification;

public class FileTransferUtil {
	private static Logger logger = Logger.getLogger("FileTransferUtil.class");

	/*
	 * ssh connect to the server
	 */
	public static SftpClient sshConnect(String hostName, String userName, String passwd) {
		return sshConnect(hostName, userName, passwd, false);
	}
	
	/*
	 * ssh connect to the server
	 */
	public static SftpClient sshConnect(String hostName, String userName, String passwd, boolean ignoreHostKey)
			{
		SshClient ssh = null;
		logger.info("sshConnect() starts...");
		logger.info("HostName- "+hostName+"; userName- "+userName);
		try {
			ssh = new SshClient();		
			if(ignoreHostKey) {
				ssh.connect(hostName, 22, new  IgnoreHostKeyVerification());
			} else {
				ssh.connect(hostName, 22);
			}
			PasswordAuthenticationClient passwordAuthenticationClient = new PasswordAuthenticationClient();
			passwordAuthenticationClient.setUsername(userName);
			passwordAuthenticationClient.setPassword(passwd);
			int result = ssh.authenticate(passwordAuthenticationClient);
			if(result != AuthenticationProtocolState.COMPLETE){
			    logger.info("Login to " + hostName + ":" + "22" +"User Name:"+userName+" Password:"+passwd+" "+" failed");
			}
			System.out.println("successfully connected..");
			return ssh.openSftpClient();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param hostName server to connect to
	 * @param userName
	 * @param password
	 * @param remoteDir the directory to ftp the file to
	 * @param file the file to ftp
	 */
	public static boolean transferFiles(String hostName, String userName, String passwd, String remoteDir, String fileName) {
		return transferFiles(hostName, userName, passwd, remoteDir, fileName, false);
	}
	
	/**
	 * @param hostName server to connect to
	 * @param userName
	 * @param password
	 * @param remoteDir the directory to ftp the file to
	 * @param file the file to ftp
	 */
	public static boolean transferFiles(String hostName, String userName, String passwd, String remoteDir, String fileName, boolean ignoreHostKey) {
		try {
			logger.info("file Name:"+fileName);
			SftpClient sftpClient = sshConnect(hostName, userName, passwd, ignoreHostKey);
			sftpClient.cd(remoteDir);
			sftpClient.put(new File(fileName).getAbsolutePath());
			sftpClient.chmod(0777, remoteDir+"/"+new File(fileName).getName());
			logger.info("Successfully transferred file: "+fileName+ " to "+userName+"@"+hostName+":"+remoteDir);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param hostName server to connect to
	 * @param userName
	 * @param password
	 * @param remoteDir the directory to ftp the file to
	 * @param file the file to ftp
	 */
	public static boolean transferFiles(Map<String, String> authInfo) {
		return transferFiles(authInfo, false);
	}	
	
	/**
	 * @param hostName server to connect to
	 * @param userName
	 * @param password
	 * @param remoteDir the directory to ftp the file to
	 * @param file the file to ftp
	 */
	public static boolean transferFiles(Map<String, String> authInfo, boolean ignoreHostKey) {
		try {
			String hostName = authInfo.get("host");
			String userName = authInfo.get("user");
			String passwd = authInfo.get("password");
			String remoteDir = authInfo.get("remoteDir");
			String file = authInfo.get("file");
			File f = new File(file);
			
			SftpClient sftpClient = sshConnect(hostName, userName, passwd, ignoreHostKey);
			sftpClient.cd(remoteDir);
			sftpClient.put(f.getAbsolutePath());
			sftpClient.chmod(0777, remoteDir+"/"+f.getName());
			System.out.println("Successfully transferred file: "+f.getAbsolutePath()+ " to "+userName+"@"+hostName+":"+remoteDir);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param hostName server to connect to
	 * @param userName
	 * @param password
	 * @param remoteDir the directory to ftp the file to
	 * @param file the file to ftp
	 */
	public static boolean transferDirectory(String hostName, String userName, String passwd, String remoteDir, String dirToTransfer, boolean commit) {
		return transferDirectory(hostName, userName, passwd, remoteDir, dirToTransfer, commit, false);
	}
	
	/**
	 * @param hostName server to connect to
	 * @param userName
	 * @param password
	 * @param remoteDir the directory to ftp the file to
	 * @param file the file to ftp
	 */
	public static boolean transferDirectory(String hostName, String userName, String passwd, String remoteDir, String dirToTransfer, boolean commit, boolean ignoreHostKey) {
		try {
			SftpClient sftpClient = sshConnect(hostName, userName, passwd, ignoreHostKey);
			sftpClient.cd(remoteDir);
			sftpClient.copyLocalDirectory(dirToTransfer, remoteDir, true, false, commit, null);
			System.out.println("Successfully transferred directory: "+dirToTransfer+ " to "+userName+"@"+hostName+":"+remoteDir);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * @param hostName server to connect to
	 * @param userName
	 * @param password
	 * @param remoteDir the directory to ftp the file to
	 * @param file the file to ftp
	 */
	public static boolean removeFileOrDirectory(String hostName, String userName, String passwd, String fileOrDirToRemove, boolean forceRemove, boolean recursiveRemove, boolean ignoreHostKey) {
		try {
			SftpClient sftpClient = sshConnect(hostName, userName, passwd, ignoreHostKey);
			//sftpClient class determines whether the file supplied is a directory or file
			sftpClient.rm(fileOrDirToRemove, forceRemove, recursiveRemove);
			System.out.println("Successfully removed directory: "+fileOrDirToRemove+ " for the server: "+userName+"@"+hostName);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean transferFiles(String hostName, String userName, String passwd, String remoteDir, File file, boolean ignoreHostKey) {
		try {
			logger.info("file Name:"+file.getName());
			SftpClient sftpClient = sshConnect(hostName, userName, passwd, ignoreHostKey);
			sftpClient.cd(remoteDir);
			sftpClient.put(file.getAbsolutePath());
			sftpClient.chmod(0777, remoteDir+"/"+file.getName());
			logger.info("Successfully transferred file: "+file.getName()+ " to "+userName+"@"+hostName+":"+remoteDir);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		transferFiles("belkecaral50", "afuvxv3", "somepassword", "/home/afuvxv3", "./test.txt", true);
	}
}
