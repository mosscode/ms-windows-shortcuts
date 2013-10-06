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
package com.moss.mswindows.shortcuts.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * | pointer A |
 * |-----------|
 * | pointer B |
 * |-----------|
 * | pointer C |
 * |-----------|
 * |   data    |
 * |-----------|
 * |   data    |
 * |-----------|
 * |   data    |
 * |-----------|
 * |   data    |
 *
 */
public class IndexedFieldsSection {
	private List<IndexedFieldEntry<?>> entries = new LinkedList<IndexedFieldEntry<?>>();
	private byte[] value;
	
	public IndexedFieldsSection(IndexedFieldEntry<?> ... fields) {
		entries.addAll(Arrays.asList(fields));
	}
	
	public byte[] rawValue() {
		return value;
	}
	
	public List<IndexedFieldEntry<?>> entries() {
		return entries;
	}

	public void offsetIndexes(int pad){
		System.out.println("Padding for the header size " + pad);
		for(IndexedFieldEntry<?> next : entries){
			Integer value = next.location().getValue();
			if(next.field().hasValue()){
				int l = value + pad;
				next.location().setValue(l);
			}
		}
	}
	
	public int write(OutputStream out) throws IOException {
		int numWritten = 0;
		for(IndexedFieldEntry<?> next : entries){
			
			if(next.field().hasValue()){
				byte[] data;
				
				{
					ByteArrayOutputStream b = new ByteArrayOutputStream();
					int size = next.field().write(b);
					data = b.toByteArray();
					
					if(size!=data.length){
						throw new RuntimeException("Incorrect size for entry # " + entries.indexOf(next) + ": " + size + " returned vs " + data.length + " written (field is " + next.field() + ")");
					}
				}
				
				out.write(data);
				next.location().setValue(numWritten);
				numWritten += data.length;
				System.out.println("Wrote field of type " + next.field().getClass().getSimpleName() );
			}else{
				switch(next.emptyValueMode()){
				case NULL_OFFSET:
					next.location().setValue(null);
					break;
				case ZERO_OFFSET:
					next.location().setValue(0);
					break;
				default: throw new RuntimeException("Unknown mode:" + next.emptyValueMode());
				}
				System.out.println("WARN: Not writing field of type " + next.field().getClass().getSimpleName() + " because it was zero-length: " + next.field());
			}
		}
		
		return numWritten;
	}
	
	public void read(byte[] data, int idx) {

//		value = BinUtil.subarray(data, idx, size);
		
		for(IndexedFieldEntry<?> next : entries){
			final Integer offset = next.location().getValue();
			
			if(offset==null || offset == 0){
				System.out.println("Not reading value for zero offset.");
			}else{
				int location = idx + next.location().getValue();
				System.out.println("Reading value from " + Long.toHexString(location) + " ("+ Long.toHexString(idx) + " using offset " + next.location().getValue() + ")");
				next.field().read(data, location);
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder text = new StringBuilder();
		text.append(getClass().getSimpleName() + "{\n");
		for(IndexedFieldEntry<?> next : entries){
			text.append(" offset: " + next.location().getValue() + "\n");
			text.append("   type: " + next.field().getClass().getSimpleName() + "\n");
			text.append("  value: " + next.field() + "\n");
		}
		text.append("}");
		return text.toString();
	}
}