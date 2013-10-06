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
import java.io.PrintStream;

public class ItemidGrabber {
	public final static String GRABBER_EXE_RESOURCE_PATH = "/com/moss/mswindows/shortcuts/ms-shell-itemid-grabber.exe";
	public static File exeTemp;

	public static byte[] getShellItemId(File target){
		return getShellItemId(target.getAbsolutePath());
	}
	public static byte[] getShellItemId(String target){
		try{
			
			try {
				if(exeTemp==null){
					File f = File.createTempFile("ms-shell-itemid-grabber", ".exe");
					f.deleteOnExit();
					
					byte[] b = new byte[1024*1024];
					InputStream in = ItemidGrabber.class.getResourceAsStream(GRABBER_EXE_RESOURCE_PATH);
					OutputStream out = new FileOutputStream(f);
					for(int x=in.read(b); x!=-1; x=in.read(b)){
						out.write(b, 0, x);
					}
					in.close();
					out.close();
					exeTemp = f;
				}
			} catch (Throwable e) {
				throw new RuntimeException("Error copying grabber from resources.", e);
			}
			
			File resultPath = File.createTempFile("shortcut-data", ".dat");
			if(!resultPath.exists() && !resultPath.createNewFile()){
				throw new IOException("Could not create temp file");
			}
			resultPath.deleteOnExit();
			
			Process p = Runtime.getRuntime().exec(new String[]{
					exeTemp.getAbsolutePath(), 
					target, 
					resultPath.getAbsolutePath()
			});
			
			new StreamDumper(p.getInputStream(), System.out).start();
			new StreamDumper(p.getErrorStream(), System.err).start();
			
			int response = p.waitFor();
			if(response!=0){
				throw new RuntimeException("Process returned " + response);
			}
			
			byte[] data;
			{
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				FileInputStream in = new FileInputStream(resultPath);
				byte[] b = new byte[1024*1024];
				for(int x = in.read(b);x!=-1;x=in.read(b)){
					out.write(b, 0, x);
				}
				in.close();
				out.close();
				data = out.toByteArray();
			}
			
			return data;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

	private static class StreamDumper extends Thread {
		final InputStream in;
		final PrintStream out;
		public StreamDumper(InputStream in, PrintStream out) {
			super();
			this.in = in;
			this.out = out;
		}
		
		@Override
		public void run() {
			try {
				byte[] b = new byte[1024];
				for(int x=in.read(b);x!=-1;x = in.read(b)){
					out.write(b, 0, x);
				}
				in.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
	}
	
}
