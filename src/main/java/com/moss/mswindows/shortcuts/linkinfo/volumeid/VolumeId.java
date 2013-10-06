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
package com.moss.mswindows.shortcuts.linkinfo.volumeid;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.moss.mswindows.shortcuts.Part;
import com.moss.mswindows.shortcuts.impl.IndexedField;
import com.moss.mswindows.shortcuts.impl.NullTerminatedStringPart;
import com.moss.mswindows.shortcuts.impl.UnsignedIntPart;
import com.moss.mswindows.shortcuts.util.BinUtil;

/**
 * The VolumeID structure specifies information about the volume that a link target was on when the
 * link was created. This information is useful for resolving the link if the file is not found in its original 
 * location.
 */
public class VolumeId implements IndexedField {
	
	/**
	 * VolumeIDSize (4 bytes): A 32-bit, unsigned integer that specifies the size, in bytes, of this
	 * structure. This value MUST be greater than 0x00000010. All offsets specified in this structure
	 * MUST be less than this value, and all strings contained in this structure MUST fit within the
	 * extent defined by this size.
	 */
	private UnsignedIntPart volumeIdSize = new UnsignedIntPart(null);
	
	/**
	 * DriveType (4 bytes): A 32-bit, unsigned integer that specifies the type of drive the link target
	 * is stored on. This value MUST be one of the following:
	 */
	private DriveTypePart driveType = new DriveTypePart(volumeIdSize);
	
	/**
	 * DriveSerialNumber (4 bytes): A 32-bit, unsigned integer that specifies the drive serial
	 * number of the volume the link target is stored on.
	 */
	private UnsignedIntPart driveSerialNumber = new UnsignedIntPart(driveType);
	
	/**
	 * VolumeLabelOffset (4 bytes): A 32-bit, unsigned integer that specifies the location of a string
	 * that contains the volume label of the drive that the link target is stored on. This value is an
	 * offset, in bytes, from the start of the VolumeID structure to a NULL-terminated string of ANSI
	 * characters. The volume label string is located in the Data field of this structure.
	 * If the value of this field is 0x00000014, it MUST be ignored, and the value of the
	 * VolumeLabelOffsetUnicode field MUST be used to locate the volume label string.
	 */
	private UnsignedIntPart volumeLabelOffset = new UnsignedIntPart(driveSerialNumber);
	
	/**
	 * VolumeLabelOffsetUnicode (4 bytes): An optional, 32-bit, unsigned integer that specifies the
	 * location of a string that contains the volume label of the drive that the link target is stored on.
	 * This value is an offset, in bytes, from the start of the VolumeID structure to a NULL-
	 * terminated string of Unicode characters. The volume label string is located in the Data field of
	 * this structure.
	 * If the value of the VolumeLabelOffset field is not 0x00000014, this field MUST be ignored,
	 * and the value of the VolumeLabelOffset field MUST be used to locate the volume label
	 * string.
	 */
	private UnsignedIntPart volumeLabelOffsetUnicode = new UnsignedIntPart(null);
	
	
	/**
	 * Data (variable): A buffer of data that contains the volume label of the drive as a string in
	 * either ANSI or Unicode characters, as specified by preceding fields.
	 */
	
	private NullTerminatedStringPart data = new NullTerminatedStringPart(null);
	
	private boolean hasValue = false;
	
	public void read(byte[] data, int idx) {
		hasValue = true;
		System.out.println("Reading VolumeId from " + Integer.toHexString(idx));
		int bytesRead = 0;
		Part p = volumeIdSize;
		while(p!=null){
			int l = p.read(data, idx + bytesRead);
			System.out.println("Read " + l + " bytes from field " + p.getClass().getSimpleName() + ", value=" + p);
			bytesRead += l;
			p = p.next();
		}
		
		if(volumeLabelOffset.getValue()==0x00000014){
			bytesRead += volumeLabelOffsetUnicode.read(data, idx + bytesRead);
			
			System.out.println("Read " + bytesRead + " up to this point.  Reading unicode data using offset " + volumeLabelOffsetUnicode.getValue());
			bytesRead += this.data.read(data, idx + volumeLabelOffsetUnicode.getValue());
		}else{
			System.out.println("Read " + bytesRead + " up to this point.  Reading data using offset " + volumeLabelOffset.getValue());
			bytesRead += this.data.read(data, idx + volumeLabelOffset.getValue());
		}
		
		
		if(bytesRead!=volumeIdSize.getValue()){
			throw new RuntimeException("Inconsistent: read " + bytesRead + ", but size was specified as " + volumeIdSize.getValue());
		}
		
		System.out.println("READ " + bytesRead + " VOLUME ID BYTES: " + BinUtil.hexDump(BinUtil.subarray(data, idx, bytesRead)));
//		return bytesRead;
	}
	
	private void assertLength(ByteArrayOutputStream b, int size, Object value){
		byte[] data = b.toByteArray();
		if(data.length!=size){
			throw new RuntimeException(" wrote " + data.length + " vs " + size + " reported.  Field was " + value);
		}
	}
	
	public int write(OutputStream out) throws IOException {
		if(!hasValue){
			throw new RuntimeException("I can't be written: I have no value.");
		}
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		
		int dataLength = 0;
		Part p = driveType;
		
		while(p!=null){
			int l = p.write(b);
			dataLength += l;
			System.out.println("Wrote " + l + " bytes from field " + p.getClass().getSimpleName() + ", value=" + p);
			
			assertLength(b, dataLength, p);
//			{
//				byte[] data = b.toByteArray();
//				if(data.length!=dataLength){
//					throw new RuntimeException(" wrote " + data.length + " vs " + dataLength + " reported.  Field was " + p);
//				}
//			}
			
			p = p.next();
		}
		
		if(volumeLabelOffset.getValue()==0x00000014){
			dataLength += volumeLabelOffsetUnicode.write(b);
		}
		assertLength(b, dataLength, p);

//		{
//			byte[] data = b.toByteArray();
//			if(data.length!=dataLength){
//				throw new RuntimeException(" wrote " + data.length + " vs " + dataLength + " reported.  Field was " + p);
//			}
//		}
		
		dataLength += this.data.write(b);
		
		assertLength(b, dataLength, p);

		byte[] data = b.toByteArray();
		
//		if(data.length!=dataLength){
//			throw new RuntimeException(" wrote " + data.length + " vs " + dataLength + " reported.  Field was " + p);
//		}
		
		int bytesWritten = 0;
		
		volumeIdSize.setValue(data.length + 4);
		bytesWritten += volumeIdSize.write(out);
		
		out.write(data);
		bytesWritten += data.length;
		
		System.out.println("WROTE " + bytesWritten + " VOLUME ID BYTES: " + BinUtil.hexDump(data));
		
		return bytesWritten;
	}
	
	public boolean hasValue() {
		return hasValue;
	}
	
	public void print(StringBuilder text, String prefix){
		String pad = prefix + "    ";
		text.append(prefix + getClass().getSimpleName() + "{\n");
		text.append(pad + "volumeIdSize:" + volumeIdSize.getValue() + "\n");
		text.append(pad + "driveType:" + driveType.getValue() + "\n");
		text.append(pad + "driveSerialNumber:" + driveSerialNumber.getValue() + "\n");
		text.append(pad + "volumeLabelOffset:" + volumeLabelOffset.getValue() + "\n");
		
		text.append(pad + "volumeLabelOffsetUnicode:" + volumeLabelOffsetUnicode.getValue() + "\n");
		text.append(pad + "data:" + data + "\n");
		text.append(prefix + "}\n");
	}
}
