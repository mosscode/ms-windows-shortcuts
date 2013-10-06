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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import com.moss.mswindows.shortcuts.linktargetidlist.ItemId;
import com.moss.mswindows.shortcuts.util.BinUtil;

public class ExtraData {
	private List<ExtraDataBlock> blocks = new LinkedList<ExtraDataBlock>();
	
	public void add(ExtraDataBlock block){
		this.blocks.add(block);
	}
	
	public void read(byte[] data, int idx) {
		int numRead = 0;
		boolean foundBlock;
		
		do{
			// READ LENGTH FIELD
			System.out.println("Reading from " + (idx+numRead) + "(" + Integer.toHexString(idx+numRead) + ")");
			int length = BinUtil.getInt(data, idx+numRead);
//			numRead += 4;
			System.out.println("Length is " + length);
			if(length<0x00000004){
				foundBlock = false;
			}else{
				foundBlock = true;
				
				// READ SIGNATURE FIELD
				int signatureValue = BinUtil.getInt(data, idx+numRead + 4);
//				numRead += 4;
				System.out.println("Signature value is " + signatureValue);
				ExtraDataBlockTypeSignature signature = null;
				for(ExtraDataBlockTypeSignature next : ExtraDataBlockTypeSignature.values()){
					if(next.signatureValue()==signatureValue){
						signature = next;
					}
				}
				if(signature==null){
					throw new IllegalStateException("Unknown signature " + signatureValue);
				}
				System.out.println("Signature is " + signature);
				
				ExtraDataBlock block = signature.newInstance();
				block.read(data, idx + numRead + 8, length-8);
				numRead += length;
				blocks.add(block);
			}
		}while(foundBlock);
		
	}
	
	public int write(OutputStream out) throws IOException {
		int numWritten = 0;
		for(ExtraDataBlock next : blocks){
			
			byte[] data;
			
			{
				ByteArrayOutputStream b = new ByteArrayOutputStream();
				next.write(b);
				data = b.toByteArray();
			}
			
			
			
			// SIZE
			numWritten += BinUtil.writeInt(out, data.length + 8);// 8 extra bytes to account for the size and signature fields
			// SIGNATURE
			numWritten += BinUtil.writeInt(out, next.signature().signatureValue());
			
			out.write(data);
			numWritten += data.length;
		}
		
		byte[] terminator = new byte[]{0x00, 0x00, 0x00, 0x00};
		out.write(terminator);
		numWritten += terminator.length;
		
		return numWritten;
	}
	
	public List<ExtraDataBlock> blocks() {
		return blocks;
	}
}