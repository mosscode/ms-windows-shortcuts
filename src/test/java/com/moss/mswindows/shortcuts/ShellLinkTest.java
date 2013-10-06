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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import junit.framework.TestCase;
import bmsi.util.JavaDiff;

import com.moss.mswindows.shortcuts.ShellLink;
import com.moss.mswindows.shortcuts.util.BinUtil;

public class ShellLinkTest extends TestCase {
	public void testReading() throws Exception {
		
		ShellLink lnk = new ShellLink();
		try {
			lnk.read(sampleA());
		}finally{
			System.out.println(lnk);
		}
		
		
	}
	
	
	public void testRoundTrip() throws Exception {
		runTests(sampleA());
	}
	private byte[] sampleA(){
		try {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			
			copyAndClose(getClass().getResourceAsStream("/com/moss/mswindows/shortcuts/eclipse-cdt.lnk"), bytes);
			
			return (bytes.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private  static void copyAndClose(InputStream in, OutputStream out) throws IOException {
		byte[] b = new byte[1024*1014];
		for(int x=in.read(b);x!=-1;x=in.read(b)){
			out.write(b, 0, x);
		}
		in.close();
		out.close();
	}
	

	private static  void runTests(byte[] data) throws Exception {
		ShellLink a = new ShellLink(data);
		String aText = a.toString();
		
		byte[] out = a.write();
		
		ShellLink b = new ShellLink();
		try {
			b.read(out);
		} catch (Exception e) {
			System.out.println("READ SO FAR: " + b.toString());
			throw new RuntimeException(e);
		}
		String bText = b.toString();
		
		
		if(!aText.equals(bText)){
			System.out.println(aText);
			String diff  = new JavaDiff().unifiedDiff(aText, bText);
			System.out.println("DIFFERENCES:\n" + diff);
			fail("Strings not equal:\n" + diff);
		}else{
			System.out.println("TEXT FOR A:\n" + aText);
			System.out.println("TEXT FOR B:\n" + bText);
		}
		
		compare(data, out);
	}

	private static void compare(byte[] a, byte[] b){
		if(a.length!=b.length){
			System.out.println("different lengths a = " + a.length + ", b = " + b.length);
		}
		for(int x=0;x<a.length;x++){
			if(x>=a.length){
				throw new RuntimeException("Prematurely reached the end of a at byte " + x);
			}
			if(x>=b.length){
				throw new RuntimeException("Prematurely reached the end of b at byte " + x);
			}
			
			int valA = a[x];
			int valB = b[x];
			
			if(valA != valB){
				throw new RuntimeException("Not equal at byte " + print(x) + ": expected " + printVals(a, x) + " but was " + printVals(b, x));
			}
		}
	}
	private static String printVals(byte[] data, int pos){
		int x = data[pos];
		
		StringBuilder text = new StringBuilder(x + " (");
		
		text.append("0x" + Integer.toHexString(x));
		text.append(", ");
		
		text.append("short " + BinUtil.getShort(data, pos));
		text.append(", ");
		
		text.append("int " + BinUtil.getInt(data, pos));
		text.append(", ");

		text.append("char " + ((char)x));

		text.append(")");
		
		return text.toString();
	}
	
	private static String print(int x){
		return x + " (0x" + Integer.toHexString(x) + ")";
	}
}
