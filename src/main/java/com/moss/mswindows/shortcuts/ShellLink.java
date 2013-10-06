/**
 * Copyright (C) 2013, Moss Computing Inc.
 *
 * This file is part of ms-windows-shortcuts.
 *
 * ms-windows-shortcuts is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * ms-windows-shortcuts is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ms-windows-shortcuts; see the file COPYING.  If not, write to the
 * Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 *
 * Linking this library statically or dynamically with other modules is
 * making a combined work based on this library.  Thus, the terms and
 * conditions of the GNU General Public License cover the whole
 * combination.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce an
 * executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under
 * terms of your choice, provided that you also meet, for each linked
 * independent module, the terms and conditions of the license of that
 * module.  An independent module is a module which is not derived from
 * or based on this library.  If you modify this library, you may extend
 * this exception to your version of the library, but you are not
 * obligated to do so.  If you do not wish to do so, delete this
 * exception statement from your version.
 */
package com.moss.mswindows.shortcuts;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.moss.mswindows.shortcuts.extradata.ExtraData;
import com.moss.mswindows.shortcuts.extradata.ExtraDataBlock;
import com.moss.mswindows.shortcuts.extradata.IconEnvironmentDataBlock;
import com.moss.mswindows.shortcuts.header.LinkFlag;
import com.moss.mswindows.shortcuts.header.ShellLinkHeader;
import com.moss.mswindows.shortcuts.linkinfo.LinkInfo;
import com.moss.mswindows.shortcuts.linktargetidlist.LinkTargetIdList;
import com.moss.mswindows.shortcuts.util.BinUtil;

public class ShellLink {
	private ShellLinkHeader header = new ShellLinkHeader();
	private LinkTargetIdList linkTargetidList;
	private LinkInfo linkInfo;

	/**
	NAME_STRING: An optional structure that specifies a description of the shortcut that is displayed
	to end users to identify the purpose of the shell link. This structure MUST be present if the
	HasName flag is set.
	*/
	private StringDataString name = new StringDataString();
	
	/**
	RELATIVE_PATH: An optional structure that specifies the location of the link target relative to the
	file that contains the shell link. When specified, this string SHOULD be used when resolving the link.
	This structure MUST be present if the HasRelativePath flag is set.
	*/
	private StringDataString relativePath = new StringDataString();
	
	/**
	WORKING_DIR: An optional structure that specifies the file system path of the working directory
	to be used when activating the link target. This structure MUST be present if the HasWorkingDir
	flag is set.
	*/
	private StringDataString workingDir = new StringDataString();
	
	/**
	COMMAND_LINE_ARGUMENTS: An optional structure that stores the command-line arguments
	that should be specified when activating the link target. This structure MUST be present if the
	HasArguments flag is set.
	*/
	private StringDataString commandLineArguments= new StringDataString();
	
	/**
	ICON_LOCATION: An optional structure that specifies the location of the icon to be used when
	displaying a shell link item in an icon view. This structure MUST be present if the HasIconLocation
	flag is set.
	*/
	private StringDataString iconLocation = new StringDataString();
	
	private ExtraData extraData = new ExtraData();
	
	
	public ShellLink() {

		this.header.flags.add(LinkFlag.IS_UNICODE);
		header().setFileSize(0);
		header().setIconIndex(0);
		header().setShowCommand(0);
	}
	
	public ShellLink(byte[] data) {
		read(data);
	}
	
	public ShellLink(File f) throws IOException {
		read(f);
	}
	
	public ShellLinkHeader header() {
		return header;
	}
	
	public ExtraData extraData() {
		return extraData;
	}
	
	public void read(File f) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] b = new byte[1024*1024];
		InputStream in = new FileInputStream(f);
		for(int x=in.read(b);x!=-1;x=in.read(b)){
			out.write(b, 0, x);
		}
		in.close();
		out.close();
		
		read(out.toByteArray());
	}
	public void read(byte[] data) {
		int numRead = header.read(data, 0);
		if(header.flags.has(LinkFlag.HAS_LINK_TARGET_ID_LIST)){
			linkTargetidList = new LinkTargetIdList();
			numRead += linkTargetidList.read(data, numRead);
		}
		
		if(header.flags.has(LinkFlag.HAS_LINK_INFO)){
			linkInfo = new LinkInfo();
			int l = linkInfo.read(data, numRead);
			numRead += l;
			System.out.println("Read " + l + " link info bytes");
		}
		
		if(header.flags.has(LinkFlag.HAS_NAME)){
			numRead += name.read(data, numRead);
		}
		if(header.flags.has(LinkFlag.HAS_RELATIVE_PATH)){
			numRead += relativePath.read(data, numRead);
		}
		if(header.flags.has(LinkFlag.HAS_WORKING_DIR)){
			numRead += workingDir.read(data, numRead);
		}
		if(header.flags.has(LinkFlag.HAS_ARGUMENTS)){
			numRead += commandLineArguments.read(data, numRead);
		}
		if(header.flags.has(LinkFlag.HAS_ICON_LOCATION)){
			numRead += iconLocation.read(data, numRead);
		}
		
		extraData.read(data, numRead);
	}

	public byte[] write(){
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			write(out);
			return out.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void write(OutputStream out) throws IOException {
		int numWritten = 0;
		
		numWritten += header.write(out);
		if(header.flags.has(LinkFlag.HAS_LINK_TARGET_ID_LIST)){
			numWritten += linkTargetidList.write(out);
		}
		if(header.flags.has(LinkFlag.HAS_LINK_INFO)){
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			int l = linkInfo.write(b);
			byte[] data = b.toByteArray();
			if(l!=data.length){
				throw new RuntimeException("Inconsistent: " + l + " vs " + data.length);
			}
			
			out.write(data);
			numWritten += data.length;
			
			System.out.println("Wrote " + data.length + " link info bytes");
//			numWritten += linkInfo.write(out);
		}
		
		if(header.flags.has(LinkFlag.HAS_NAME)){
			numWritten += name.write(out);
		}
		if(header.flags.has(LinkFlag.HAS_RELATIVE_PATH)){
			numWritten += relativePath.write(out);
		}
		if(header.flags.has(LinkFlag.HAS_WORKING_DIR)){
			numWritten += workingDir.write(out);
		}
		if(header.flags.has(LinkFlag.HAS_ARGUMENTS)){
			numWritten += commandLineArguments.write(out);
		}
		if(header.flags.has(LinkFlag.HAS_ICON_LOCATION)){
			numWritten += iconLocation.write(out);
		}
		
		numWritten +=  extraData.write(out);
	}
	
	public void setCommandLineArguments(String commandLineArguments) {
		this.header.flags.add(LinkFlag.HAS_ARGUMENTS);
		this.commandLineArguments = new StringDataString(commandLineArguments);
		System.out.println("Arguments: " + commandLineArguments);
	}
	
	public String getCommandLineArguments(){
		String args = null;
		
		if(this.header.flags.has(LinkFlag.HAS_ARGUMENTS)){
			if(this.commandLineArguments!=null){
				args = this.commandLineArguments.toString();
			}
		}
		
		return args;
	}
	
	public void setRelativePath(StringDataString relativePath) {
		this.header.flags.add(LinkFlag.HAS_RELATIVE_PATH);
		this.relativePath = relativePath;
	}
	public void setWorkingDir(String workingDir) {
		this.header.flags.add(LinkFlag.HAS_WORKING_DIR);
		this.workingDir = new StringDataString(workingDir);
	}
	
	public LinkInfo linkInfo() {
		return linkInfo;
	}
	
	public void setIconWithEnv(String pathWithEnvVars){
		this.header.flags.add(LinkFlag.HAS_EXP_ICON);
		extraData().add(new IconEnvironmentDataBlock(pathWithEnvVars));
	}
	
	public void setIcon(String pathToIco){
		this.header.flags.add(LinkFlag.HAS_ICON_LOCATION);
		this.iconLocation = new StringDataString(pathToIco);
	}

	public void setLinkTarget(File target){
		setLinkTarget(target.getAbsolutePath());
	}
	
	public void write(File dest) throws IOException {
		FileOutputStream out = new FileOutputStream(dest);
		write(out);
		out.close();
		System.out.println("Wrote to " + dest.getAbsolutePath());
	}
	
	public void setLinkTarget(String target){
		System.out.println("Setting link target to " + target);
		byte[] data = ItemidGrabber.getShellItemId(target);//getLinkData(location);
		System.out.println("Got link data of " + data.length + " bytes");
		byte[] full = new byte[data.length + 4];
		{
			BinUtil.setShort(full, 0, (short)data.length);
			for(int x=0;x<data.length;x++){
				full[x+2] = data[x];
			}
		}
		full[data.length-1 + 3] = 0;
		full[data.length-1 + 4] = 0;
		
		LinkTargetIdList l = new LinkTargetIdList();
		l.read(full, 0);
		
		setLinkTargetidList(l);
	}
	
	public void setLinkTargetidList(LinkTargetIdList linkTargetidList) {
		this.header.flags.add(LinkFlag.HAS_LINK_TARGET_ID_LIST);
		this.linkTargetidList = linkTargetidList;
	}
	public LinkTargetIdList getLinkTargetidList() {
		return linkTargetidList;
	}
	@Override
	public String toString() {
		StringBuilder text = new StringBuilder();
		text.append(getClass().getSimpleName() + "{ \n");
		header.print(text, "    ");
		if(linkTargetidList!=null){
			linkTargetidList.print(text, "    ");
		}
		
		if(linkInfo!=null){
			linkInfo.print(text, "    ");
		}
		
		text.append("    name: " + name + "\n");
		text.append("    relativePath: " + relativePath + "\n");
		text.append("    workingDir: " + workingDir + "\n");
		text.append("    commandLineArguments: " + commandLineArguments + "\n");
		text.append("    iconLocation: " + iconLocation + "\n");
		
		for(ExtraDataBlock next : extraData.blocks()){
			next.print(text, "    ");
		}
		
		text.append("}\n");
		return text.toString();
	}
}
