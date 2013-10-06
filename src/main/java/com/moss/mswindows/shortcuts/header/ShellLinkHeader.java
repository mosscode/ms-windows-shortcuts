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
package com.moss.mswindows.shortcuts.header;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import com.moss.mswindows.shortcuts.Part;
import com.moss.mswindows.shortcuts.impl.BinaryPart;
import com.moss.mswindows.shortcuts.impl.ShortPart;
import com.moss.mswindows.shortcuts.impl.SignedIntPart;
import com.moss.mswindows.shortcuts.impl.UnsignedIntPart;
import com.moss.mswindows.shortcuts.util.BinUtil;

public class ShellLinkHeader {
	private SizePart headerSize = new SizePart();
	private LinkClassPart linkClass = new LinkClassPart(headerSize);
	public FlagsPart flags = new FlagsPart(linkClass);
	private FileAttributesPart fileAttributes = new FileAttributesPart(flags);
	private TimestampPart creationTime = new TimestampPart(fileAttributes);
	private TimestampPart accessTime = new TimestampPart(creationTime);
	private TimestampPart writeTime = new TimestampPart(accessTime);
	private UnsignedIntPart fileSize = new UnsignedIntPart(writeTime);
	private SignedIntPart iconIndex = new SignedIntPart(fileSize);
	private ShowCommandPart showCommand = new ShowCommandPart(iconIndex);
	private ShortPart hotKey = new ShortPart(showCommand);
	private BinaryPart reserved1 = new BinaryPart(2, hotKey);
	private BinaryPart reserved2 = new BinaryPart(4, reserved1);
	private BinaryPart reserved3 = new BinaryPart(4, reserved2);
	
	public int read(byte[] data, int idx) {
		
		int bytesRead = 0;
		Part p = headerSize;
		while(p!=null){
			bytesRead += p.read(data, idx + bytesRead);
			p = p.next();
		}
		
		return bytesRead;
	}
	
	public int write(OutputStream out) throws IOException {
		int bytesWritten = 0;
		
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		
		Part p = linkClass;
		
		for(int x=0;p!=null;x++){
//		while(p!=null){
			System.out.println("Writing part " + x);
			p.write(b);
			p = p.next();
		} 
		
		byte[] data = b.toByteArray();
		
		headerSize.setValue(data.length + 4);
		
		bytesWritten += headerSize.write(out);
		out.write(data);
		bytesWritten += data.length;
		
		return bytesWritten;
	}
	
	public void setFileSize(Integer size) {
		this.fileSize.setValue(size);
	}
	
	public void setIconIndex(Integer iconIndex) {
		this.iconIndex.setValue(iconIndex);
	}
	
	public void setShowCommand(Integer showCommand) {
		this.showCommand.setValue(showCommand);
	}
	
	public void print(StringBuilder text, String prefix){
		String pad = prefix + "    ";
		text.append(prefix + getClass().getSimpleName() + "{\n");
		text.append(pad + "size:" + headerSize.getValue() + "\n");
		text.append(pad + "class:" + linkClass + "\n");
		text.append(pad + "flags:(");
		printList(text, flags.getFlags());
		text.append(")\n");
		text.append(pad + "attributes:(");
		printList(text, fileAttributes.getFlags());
		text.append(")\n");
		

		text.append(pad + "creationTime:" + print(creationTime) + "\n");
		text.append(pad + "accessTime:" + print(accessTime) + "\n");
		text.append(pad + "writeTime:" + print(writeTime) + "\n");
		text.append(pad + "fileSize:" + fileSize.getValue() + "\n");
		

		text.append(pad + "showCommand:" + showCommand.getValue() + "\n");
		text.append(pad + "hotKey:" + hotKey.getValue() + "\n");
		text.append(pad + "reserved1:" + BinUtil.hexDump(reserved1.getValue()) + "\n");
		text.append(pad + "reserved2:" + BinUtil.hexDump(reserved2.getValue()) + "\n");
		text.append(pad + "reserved3:" + BinUtil.hexDump(reserved3.getValue()) + "\n");
		
		text.append(prefix + "}\n");
	}
	
	private String print(TimestampPart v){
		StringBuilder text = new StringBuilder();
		text.append(creationTime.getValue());
		text.append(" (");
		text.append(new Date(v.getValue()));
		text.append(")");
		
		return text.toString();
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
}
