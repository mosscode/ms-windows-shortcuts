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
package com.moss.mswindows.shortcuts.linkinfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.moss.mswindows.shortcuts.Part;
import com.moss.mswindows.shortcuts.impl.UnsignedIntPart;

public class LinkInfoHeader {
	/**
	 * LinkInfoHeaderSize (4 bytes): A 32-bit, unsigned integer that specifies the size, in bytes, of
   the LinkInfo header section, which includes all specified offsets. This value MUST be defined as
   shown in the following table, and it MUST be less than LinkInfoSize.<1>
     Value                          Meaning
     0x0000001C                     Offsets to the optional fields are not specified.
     0x00000024 ≤ value             Offsets to the optional fields are specified.
	 */
	protected UnsignedIntPart linkInfoHeaderSize = new UnsignedIntPart(null);
	
	/**
	 * LinkInfoFlags (4 bytes): Flags that specify whether the VolumeID, LocalBasePath,
   LocalBasePathUnicode, and CommonNetworkRelativeLink fields are present in this
   structure.
	 */
	protected LinkInfoFlagsPart linkInfoFlags = new LinkInfoFlagsPart(linkInfoHeaderSize);
	
	/**
	 * VolumeIDOffset (4 bytes): A 32-bit, unsigned integer that specifies the location of the
  VolumeID field. If the VolumeIDAndLocalBasePath flag is set, this value is an offset, in
  bytes, from the start of the LinkInfo structure; otherwise, this value MUST be zero.
	 */
	
	protected UnsignedIntPart volumeIdOffset = new UnsignedIntPart(linkInfoFlags);
	
	/**
	 * LocalBasePathOffset (4 bytes): A 32-bit, unsigned integer that specifies the location of the
  LocalBasePath field. If the VolumeIDAndLocalBasePath flag is set, this value is an offset,
  in bytes, from the start of the LinkInfo structure; otherwise, this value MUST be zero.
	 */
	protected UnsignedIntPart localBasePathOffset = new UnsignedIntPart(volumeIdOffset);
	
	/**
	CommonNetworkRelativeLinkOffset (4 bytes): A 32-bit, unsigned integer that specifies the
	  location of the CommonNetworkRelativeLink field. If the
	  CommonNetworkRelativeLinkAndPathSuffix flag is set, this value is an offset, in bytes,
	  from the start of the LinkInfo structure; otherwise, this value MUST be zero.
	 */
	protected UnsignedIntPart commonNetworkRelativeLinkOffset = new UnsignedIntPart(localBasePathOffset);
	
	/**
	CommonPathSuffixOffset (4 bytes): A 32-bit, unsigned integer that specifies the location of
	  the CommonPathSuffix field. This value is an offset, in bytes, from the start of the LinkInfo
	  structure.
	 */
	protected UnsignedIntPart commonPathSuffixOffset = new UnsignedIntPart(commonNetworkRelativeLinkOffset);
	
	/*###############################################################################
	 *# OPTIONAL FIELDS (present if the  LinkInfoHeaderSize is greater than or equal to 0x00000024)
	 *###############################################################################*/
	
	/**
		LocalBasePathOffsetUnicode (4 bytes): An optional, 32-bit, unsigned integer that specifies
		the location of the LocalBasePathUnicode field. If the VolumeIDAndLocalBasePath flag is
		set, this value is an offset, in bytes, from the start of the LinkInfo structure; otherwise, this
		value MUST be zero. This field can be present only if the value of the LinkInfoHeaderSize
		field is greater than or equal to 0x00000024.
	 */
	protected UnsignedIntPart localBasePathOffsetUnicode = new UnsignedIntPart(null);
	
	/**
	CommonPathSuffixOffsetUnicode (4 bytes): An optional, 32-bit, unsigned integer that
	  specifies the location of the CommonPathSuffixUnicode field. This value is an offset, in
	  bytes, from the start of the LinkInfo structure. This field can be present only if the value of the
	  LinkInfoHeaderSize field is greater than or equal to 0x00000024
      */
	protected UnsignedIntPart commonPathSuffixOffsetUnicode = new UnsignedIntPart(localBasePathOffsetUnicode);
	
	public void print(StringBuilder text, String prefix){
		String pad = prefix + "    ";
		text.append(prefix + getClass().getSimpleName() + "{\n");
		text.append(pad + "linkInfoHeaderSize:" + linkInfoHeaderSize.getValue() + "\n");
		text.append(pad + "linkInfoFlags:(");
		printList(text, linkInfoFlags.getFlags());
		text.append( ")\n");
		text.append(pad + "volumeIdOffset:" + volumeIdOffset.getValue() + "\n");
		text.append(pad + "localBasePathOffset:" + localBasePathOffset.getValue() + "\n");
		
		text.append(pad + "commonNetworkRelativeLinkOffset:" + commonNetworkRelativeLinkOffset.getValue() + "\n");
		text.append(pad + "commonPathSuffixOffset:" + commonPathSuffixOffset.getValue() + "\n");
		text.append(pad + "localBasePathOffsetUnicode:" + localBasePathOffsetUnicode.getValue() + "\n");
		text.append(pad + "commonPathSuffixOffsetUnicode:" + commonPathSuffixOffsetUnicode.getValue() + "\n");
		text.append(prefix + "}\n");
	}
	private <T> void printList(StringBuilder text, T[] list){
		if(list!=null){
			
			for(int x=0;x<list.length;x++){
				T next = list[x];
				if(x>0){
					text.append(",");
				}
				text.append(next);
			}
		}
	}
	public int read(byte[] data, final int idx) {
		
		int bytesRead = 0;
		Part p = linkInfoHeaderSize;
		while(p!=null){
			bytesRead += p.read(data, idx + bytesRead);
			p = p.next();
		}
		
		if(linkInfoHeaderSize.getValue() >= 0x00000024){
			p = localBasePathOffsetUnicode;
			while(p!=null){
				bytesRead += p.read(data, idx + bytesRead);
				p = p.next();
			}
		}
		
		return bytesRead;
	}
	
	public int size(){
		int size = 6 * 4;
		if(localBasePathOffsetUnicode.getValue()!=null){
			size+=4;
		}
		
		if(commonPathSuffixOffsetUnicode.getValue()!=null){
			size+=4;
		}
		return size;
	}
	
	public int write(OutputStream out) throws IOException {
		
		int headerLength = 0;
		byte[] bodyData;
		{
			
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			{
				Part p = linkInfoFlags;
				
				while(p!=null){
					int l = p.write(b);
					System.out.println("Wrote header field: " + l + " (" + p + ")");
					headerLength += l;
					p = p.next();
				}
			}
			if(localBasePathOffsetUnicode.getValue()!=null){
				int l = localBasePathOffsetUnicode.write(b);
				headerLength += l;

				System.out.println("Wrote " + l + " (" + localBasePathOffsetUnicode + ")");
			}
			
			if(commonPathSuffixOffsetUnicode.getValue()!=null){
				int l = commonPathSuffixOffsetUnicode.write(b);
				headerLength += l;
				System.out.println("Wrote " + l + " (" + commonPathSuffixOffsetUnicode + ")");
			}
			
			bodyData = b.toByteArray();
			if(headerLength!=bodyData.length){
				throw new RuntimeException(headerLength + " != " + bodyData.length);
			}
			
			headerLength += 8; // 4 bytes for the header size field, another 4 for the while LinkInfo size field
			if(headerLength!=0x0000001C && !(headerLength>=0x00000024)){
				throw new RuntimeException("Somehow we calculated an invalid header length: " + headerLength + " (" + Long.toHexString(headerLength) + ")");
			}
			
			linkInfoHeaderSize.setValue(headerLength);
		}
		
		
		int bytesWritten = 0;
		bytesWritten += linkInfoHeaderSize.write(out);
		out.write(bodyData);
		bytesWritten += bodyData.length;
		
		return bytesWritten;
	}
	
}