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
import com.moss.mswindows.shortcuts.impl.IndexedFieldEntry;
import com.moss.mswindows.shortcuts.impl.IndexedFieldsSection;
import com.moss.mswindows.shortcuts.impl.NullTerminatedStringField;
import com.moss.mswindows.shortcuts.impl.NullTerminatedStringPart;
import com.moss.mswindows.shortcuts.impl.UnsignedIntPart;
import com.moss.mswindows.shortcuts.impl.IndexedFieldEntry.EmptyValueMode;
import com.moss.mswindows.shortcuts.linkinfo.commonnetworkrelativelink.CommonNetworkRelativeLink;
import com.moss.mswindows.shortcuts.linkinfo.volumeid.VolumeId;


public class LinkInfo {
	/**
	 * LinkInfoSize (4 bytes): A 32-bit, unsigned integer that specifies the size, in bytes, of the
   		LinkInfo structure. All offsets specified in this structure MUST be less than this value, and all
   		strings contained in this structure MUST fit within the extent defined by this size.
	 */
	private UnsignedIntPart linkInfoSize = new UnsignedIntPart(null);

	
	
	private LinkInfoHeader header = new LinkInfoHeader();
	
	/**
	VolumeID (variable): An optional VolumeID structure (section 2.3.1) that specifies
	  information about the volume that the link target was on when the link was created. This field
	  is present if the VolumeIDAndLocalBasePath flag is set.
	  */
	private VolumeId volumeId = new VolumeId();
	
	/**
	LocalBasePath (variable): An optional, NULL–terminated, ANSI string that is used to
	  construct the full path to the link item or link target by appending the string in the
	  CommonPathSuffix field. This field is present if the VolumeIDAndLocalBasePath flag is
	  set.
	  */
	private NullTerminatedStringField localBasePath = new NullTerminatedStringField();
	
	
	/**
	CommonNetworkRelativeLink (variable): An optional CommonNetworkRelativeLink structure
	  (section 2.3.2) that specifies information about the network location where the link target is
	  stored.
	  */
	
	private CommonNetworkRelativeLink commonNetworkRelativeLink = new CommonNetworkRelativeLink();
	
	/**
	CommonPathSuffix (variable): A NULL–terminated, ANSI string that is used to construct the
	  full path to the link item or link target by being appended to the string in the LocalBasePath
	  field.
	  */
	private NullTerminatedStringField commonPathSuffix = new NullTerminatedStringField();
	
	
	/**
	LocalBasePathUnicode (variable): An optional, NULL–terminated, Unicode string that is
	  used to construct the full path to the link item or link target by appending the string in the
	  CommonPathSuffixUnicode field. This field can be present only if the
	  VolumeIDAndLocalBasePath flag is set and the value of the LinkInfoHeaderSize field is
	  greater than or equal to 0x00000024.
	  */
	private NullTerminatedStringField localBasePathUnicode = new NullTerminatedStringField();
	
	/**
	CommonPathSuffixUnicode (variable): An optional, NULL–terminated, Unicode string that is
	  used to construct the full path to the link item or link target by being appended to the string in
	  the LocalBasePathUnicode field. This field can be present only if the value of the
	  LinkInfoHeaderSize field is greater than or equal to 0x00000024.
	  */
	private NullTerminatedStringField commonPathSuffixUnicode = new NullTerminatedStringField();
	
	
	
	IndexedFieldsSection data = new IndexedFieldsSection(
			new IndexedFieldEntry<VolumeId>(					header.volumeIdOffset, 					volumeId, 					EmptyValueMode.ZERO_OFFSET),
			new IndexedFieldEntry<NullTerminatedStringField>(	header.localBasePathOffset, 			localBasePath, 				EmptyValueMode.ZERO_OFFSET),
			new IndexedFieldEntry<NullTerminatedStringField>(	header.localBasePathOffsetUnicode, 		localBasePathUnicode),
			new IndexedFieldEntry<CommonNetworkRelativeLink>(	header.commonNetworkRelativeLinkOffset, commonNetworkRelativeLink,	EmptyValueMode.ZERO_OFFSET),
			new IndexedFieldEntry<NullTerminatedStringField>(	header.commonPathSuffixOffset, 			commonPathSuffix),
			new IndexedFieldEntry<NullTerminatedStringField>(	header.commonPathSuffixOffsetUnicode, 	commonPathSuffixUnicode)
	);
	
	public void print(StringBuilder text, String prefix){
		String pad = prefix + "    ";
		text.append(prefix + getClass().getSimpleName() + "{\n");
		text.append(pad + "linkInfoSize:" + linkInfoSize.getValue() + "\n");
		
		header.print(text, prefix + "    ");
		
//		text.append(pad + "volumeId:" + volumeId.toString() + "\n");
		volumeId.print(text, prefix + "    ");
		text.append(pad + "localBasePath:" + localBasePath + "\n");
		text.append(pad + "commonNetworkRelativeLink:" + commonNetworkRelativeLink + "\n");
		text.append(pad + "commonPathSuffix:" + commonPathSuffix + "\n");
		text.append(pad + "localBasePathUnicode:" + localBasePathUnicode + "\n");
		text.append(pad + "commonPathSuffixUnicode:" + commonPathSuffixUnicode + "\n");
		text.append(prefix + "}\n");
	}
	
	
	public int read(byte[] data, final int idx) {
		System.out.println("Reading link info from 0x" + Integer.toHexString(idx));
		int bytesRead = 0;
		
		bytesRead += linkInfoSize.read(data, idx);
		System.out.println("Link Info says its size is " + linkInfoSize.getValue());
		
		bytesRead += header.read(data, idx + bytesRead);
		
		this.data.read(data, idx);
		
		System.out.println("READ INDEXED DATA: " + this.data.toString());

		return linkInfoSize.getValue();
	}

	public int write(OutputStream out) throws IOException {
		// RENDER TO BUFFERS SO WE CAN GET THE FINAL SIZES
		ByteArrayOutputStream dataBytes = new ByteArrayOutputStream();
		int dataSize = data.write(dataBytes);
		
		ByteArrayOutputStream headerBytes = new ByteArrayOutputStream();
		
		// adjust offsets by header size, since they are expressed as an offset from the start of the LinkInfo
		int pad = header.size() + 4;
		
		data.offsetIndexes(pad);
		
		int headerSize = header.write(headerBytes);
		if(headerSize != header.size()){
			throw new RuntimeException(" Inconsistent: " + headerSize + " vs " + header.size());
		}
		
		System.out.println("WROTE INDEXED DATA: " + data.toString());
		
		linkInfoSize.setValue(dataSize + headerSize + 4);
		System.out.println("Writing LinkInfo size " + linkInfoSize.getValue());
		
		// WRITE TO THE STREAM
		int numWritten = 0;
		
		numWritten += linkInfoSize.write(out);
		
		out.write(headerBytes.toByteArray());
		numWritten += headerSize;
		
		out.write(dataBytes.toByteArray());
		numWritten += dataSize;
		
		return numWritten;
	}
}
