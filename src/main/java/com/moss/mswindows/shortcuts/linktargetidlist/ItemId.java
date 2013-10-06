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

import java.io.IOException;
import java.io.OutputStream;

import com.moss.mswindows.shortcuts.impl.ShortPart;
import com.moss.mswindows.shortcuts.util.BinUtil;

/**
 * An ItemID is an element in an IDList structure (section 2.2.1). The data stored in a given ItemID is
	defined by the source that corresponds to the location in the target namespace of the preceding

    ItemIDSize (2 bytes): A 16-bit, unsigned integer that specifies the size, in bytes, of the
       ItemID structure, including the ItemIDSize field.

    Data (variable): The shell data source-defined data that specifies an item.

 *
 */
public class ItemId {
	/**
	 * ItemIDSize (2 bytes): A 16-bit, unsigned integer that specifies the size, in bytes, of the
       ItemID structure, including the ItemIDSize field.
	 */
	private ShortPart size = new ShortPart(null);
	
	private byte[] value;

	public void print(StringBuilder text, String prefix){
		String pad = prefix + "    ";
		text.append(prefix + getClass().getSimpleName() + "{\n");
		text.append(pad + "size:" + size.getValue() + "\n");
		text.append(pad + "value:" + BinUtil.hexDump(value) + "\n");
		text.append(prefix + "}\n");
	}
	
	public int read(byte[] data, int idx) {
		int numRead = size.read(data, idx);
		
		System.out.println("Reading " + size.getValue() + " bytes for itemid from index " + (idx-2));
		
		value = new byte[size.getValue()-2];
		
		for(int x=0;x<value.length;x++){
			byte b = data[idx + numRead + x];
//			System.out.println((idx + x) + "=" + b);
			value[x] = b;
		}
		numRead += value.length;
		System.out.println("read " + numRead);
		return numRead;
	}

	public int write(OutputStream out) throws IOException {
		int numWritten = 0;
		
		size.setValue((short)(value.length + 2));
		numWritten = size.write(out);
		
		out.write(value);
		numWritten += value.length;
		
		System.out.println("Wrote item id of " + numWritten + " bytes");
		return numWritten;
	}
}
