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
package com.moss.mswindows.shortcuts.extradata;

import java.io.IOException;
import java.io.OutputStream;

import com.moss.mswindows.shortcuts.impl.NullTerminatedStringPart;

public class IconEnvironmentDataBlock extends ExtraDataBlock {
	private static final String UNICODE_CHARSET = "UTF-16LE";
	
	private final static int 
			ANSI_STRING_SECTION_LENGTH_IN_BYTES = 260, 
			UNICODE_STRING_SECTION_LENGTH_IN_BYTES = 520; 
	
	private String value;
	
	public IconEnvironmentDataBlock() {
	}

	public IconEnvironmentDataBlock(String value) {
		super();
		this.value = value;
	}

	@Override
	public ExtraDataBlockTypeSignature signature() {
		return ExtraDataBlockTypeSignature.ICON_ENVIRONMENT_PROPS;
	}
	
	@Override
	public void read(byte[] data, int idx, int size) {
		
		StringBuilder value = new StringBuilder();
		{
			int x=0;
			int c = data[idx];
			while(c!=0){
				value.append((char)c);
				x++;
				c = data[idx + x];
			}
		}
		
		this.value = value.toString();
		System.out.println("Read " + value);
	}
	
	@Override
	public int write(OutputStream out) throws IOException {
		System.out.println("Writing icon: " + value);
		
		byte[] ansiBytes = value.getBytes("UTF-8");
		byte[] unicodeBytes = value.getBytes(UNICODE_CHARSET);
		
		if(ansiBytes.length>=ANSI_STRING_SECTION_LENGTH_IN_BYTES){
			throw new RuntimeException("PATH TOO LONG");
		}
		
		if(unicodeBytes.length>=UNICODE_STRING_SECTION_LENGTH_IN_BYTES){
			throw new RuntimeException("PATH TOO LONG");
		}
		
		byte[] value = new byte[
		                        ANSI_STRING_SECTION_LENGTH_IN_BYTES 
		                        + 
		                        UNICODE_STRING_SECTION_LENGTH_IN_BYTES
		                       ];
		
		
		for(int x=0;x<ansiBytes.length;x++){
			value[x] = ansiBytes[x];
		}
		value[ansiBytes.length] = 0;
		
		value[ANSI_STRING_SECTION_LENGTH_IN_BYTES]=0;
		
//		for(int x=0;x<ansiBytes.length;x++){
//			value[x+ ANSI_STRING_SECTION_LENGTH_IN_BYTES] = ansiBytes[x];
//		}
//		value[ansiBytes.length + ANSI_STRING_SECTION_LENGTH_IN_BYTES] = 0;
//		
		
		out.write(value);
		
		return value.length;
	}
	
	@Override
	public void print(StringBuilder text, String prefix) {
		String pad = prefix + "    ";
		text.append(prefix + getClass().getSimpleName() + "{\n");
		text.append(pad + value + "\n");
		text.append(prefix + "}\n");
	}
}
