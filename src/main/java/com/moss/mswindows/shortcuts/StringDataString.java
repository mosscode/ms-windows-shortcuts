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

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.moss.mswindows.shortcuts.impl.ShortPart;
import com.moss.mswindows.shortcuts.util.BinUtil;

/**
 * A string from the "string data" section
 */
public class StringDataString {
	private static final String CHARSET = "UTF-16LE";
	
	private ShortPart length = new ShortPart(null);
	private StringBuilder value;
	
	public StringDataString() {
	}
	
	public StringDataString(String value){
		this.value = new StringBuilder(value);
	}

	public int read(byte[] data, final int idx) {
		int numRead = length.read(data, idx);
		
		System.out.println(BinUtil.getShort(data, idx));
		
		value = new StringBuilder();
		
		System.out.println("Reading " + getClass().getSimpleName() + " of size " + length.getValue() + " from " + idx + "(" + Integer.toHexString(idx) + ")");
		
		byte[] textData = BinUtil.subarray(data, idx + numRead, length.getValue()*2);
		numRead+=textData.length;
		System.out.println("Got " + textData.length + " bytes out ");
		try {
			String v= new String(textData, CHARSET);
			value.append(v);
			System.out.println("String value " + v);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		
//		for(int x=0;x<length.getValue();x++){
//			int c = data[idx + x + numRead];
//			System.out.println( x + " is " + c + "(" + ((char)c) + ")");
//			value.append((char)c);
//		}
		
		System.out.println("Read " + value);
//		int numRead = 0;
//		int c = data[idx];
//		while(c!=0){
//			value.append((char)c);
//			numRead ++;
//			c = data[idx + numRead];
//		}

		return numRead;
	}

	public int write(OutputStream out) throws IOException {
		int numWritten = 0;
		
		if(value.length()>Short.MAX_VALUE){
			throw new RuntimeException("String too long: must be smaller than " + Short.MAX_VALUE);
		}
		length.setValue((short)value.length());
		
		// WRITE LENGTH
		numWritten += length.write(out);
		
		// WRITE STRING DATA
		byte[] bytes = value.toString().getBytes(CHARSET);
		out.write(bytes);
		numWritten += bytes.length;
		
		System.out.println("Wrote a " + length.getValue() + " length (" + bytes.length + " bytes) string from "+ getClass().getSimpleName() + ": " + value);
		return numWritten;
	}
	@Override
	public String toString() {
		return value==null?null:value.toString();
	}
}
