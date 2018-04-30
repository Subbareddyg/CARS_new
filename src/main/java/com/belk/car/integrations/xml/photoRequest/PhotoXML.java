/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.integrations.xml.photoRequest;

import com.belk.car.integrations.xml.xstream.CarsEnumTypeConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("photo")
public class PhotoXML 
{	
	public PhotoXML() {}
	public PhotoXML(Type type) { this.type = type; }
	
	
	@XStreamAlias("file") 
	private FileXML fileXML;
	public FileXML getFileXML() { return this.fileXML; }
	public void setFileXML(FileXML fileXML) { this.fileXML = fileXML; }
	
	private String instructions;
	public String getInstructions() { return this.instructions; }
	public void setInstructions(String instructions) { this.instructions = instructions; }
	
	@XStreamAsAttribute
	@XStreamConverter(CarsEnumTypeConverter.class)
	protected Type type;
	public static enum Type { Sample };

	public static class FileXML 
	{
		public FileXML() {}
		public FileXML(String location, String path, String namePrefix, String nameSuffix, String nameExtension) {
			this(location, path, new NameXML(namePrefix, nameSuffix, nameExtension));
		}
		public FileXML(String location, String path, NameXML nameXML) {
			this.location = location; 
			this.path = path; 
			this.nameXML = nameXML;
		}
		
		
		String location;
		public String getLocation() { return this.location; }
		public void setLocation(String location) { this.location = location; }
		
		String path;
		public String getPath() { return this.path; }
		public void setPath(String path) { this.path = path; }
		
		@XStreamAlias("name")
		public NameXML nameXML;
		public NameXML getNameXML() { return this.nameXML; }
		public void setNameXML(NameXML nameXML) { this.nameXML = nameXML; }
		

		public static class NameXML 
		{
			public String prefix;
			public String suffix;
			public String extension;

			public NameXML() {}
			public NameXML( String prefix, String suffix, String extension ) { this.prefix = prefix; this.suffix = suffix; this.extension = extension; }

			public String getFullName() { return	((prefix != null) ? prefix : "") + 
								((suffix != null) ? suffix : "") +
								((extension != null) ? extension : ""); }
			
			public String toString() { return new StringBuffer("Photo.File.Name[").append("prefix:").append(prefix).append(", suffix:").append(suffix).append(", extension:").append(extension).append(" ]").toString();}
		}

		@Override
		public String toString() { return new StringBuffer("Photo.File[").append("name:").append(nameXML).append(" ]").toString();}
	}

	@Override
	public String toString() { return new StringBuffer("Photo[").append(" file:").append(fileXML).append("instructions:").append(instructions).append("type:").append(type).append(" ]").toString();}
}

	