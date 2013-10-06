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
package com.moss.mswindows.shortcuts.linktargetidlist;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

import com.moss.mswindows.shortcuts.impl.ShortPart;
import com.moss.mswindows.shortcuts.util.BinUtil;

public class LinkTargetIdList {
	/**
	 * IDListSize (2 bytes): The size, in bytes, of the IDList field.
	 */
	private ShortPart idListSize = new ShortPart(null);
	
	private List<ItemId> idList = new LinkedList<ItemId>();
	
	public void print(StringBuilder text, String prefix){
		String pad = prefix + "    ";
		text.append(prefix + getClass().getSimpleName() + "{\n");
		text.append(pad + "size:" + idListSize.getValue() + "\n");
		if(idList.size()>0){
			text.append(pad + "idList:{\n" );
			for(ItemId next : idList){
				next.print(text, pad + "    ");
			}
			text.append(pad + "}\n");
		}
		text.append(prefix + "}\n");
	}
	
	public int read(byte[] data, int idx) {
//		idx = 0x4C;
		System.out.println("Reading from index " + Integer.toHexString(idx));
		int numRead = idListSize.read(data, idx);
		System.out.println("Reading LinkTargetList of " + idListSize.getValue() + " bytes");
		int listBytesRead = 0;
		
		for(int x=0;x<idListSize.getValue();x++){
			byte b = data[idx+x];
			System.out.println((x-2) + "=" + b);
		}
		for(int size = BinUtil.getShort(data, idx + numRead);size!=0;size = BinUtil.getShort(data, idx + numRead)){
			ItemId item = new ItemId();
			int itemBytesRead = item.read(data, idx+numRead);
//			itemBytesRead +=1;
			listBytesRead += itemBytesRead;
			numRead += itemBytesRead;
			idList.add(item);
		}
		
		numRead+=2;// to account for the list terminator
		
		System.out.println("Read " + numRead + " LinkTargetList bytes");
		return numRead;
	}

	public int write(OutputStream out) throws IOException {
		
		int numWritten = 0;
		
		byte[] data;
		{
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			int rSize = 0;
			for(ItemId next : idList){
				rSize += next.write(b);
			}
			
			b.write(new byte[]{0x00, 0x00});
			rSize += 2;
			
			data = b.toByteArray();
			if(data.length!=rSize){
				throw new RuntimeException("Error: inconsistency: " + rSize + "!=" + data.length);
			}
			idListSize.setValue((short)(data.length));
		}
		
		numWritten += idListSize.write(out);
		out.write(data);
		numWritten += data.length;
		

		System.out.println("Wrote " + numWritten + " LinkTargetList bytes");
		return numWritten;
	}
}
