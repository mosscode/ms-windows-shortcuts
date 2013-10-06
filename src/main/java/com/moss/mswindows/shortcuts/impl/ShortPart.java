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

import java.io.IOException;
import java.io.OutputStream;

import com.moss.mswindows.shortcuts.Part;
import com.moss.mswindows.shortcuts.util.BinUtil;

public class ShortPart extends AbstractPart {
	private short value;
	
	public ShortPart(Part next) {
		super(next);
	}
	
	public int write(OutputStream out) throws IOException {
		return BinUtil.writeShort(out, value);
	}
	public int read(byte[] data, int idx) {
		value = (short) (BinUtil.getShort(data, idx) & 0xFFFF);
		return 2;
	}
	public void setValue(short value) {
		this.value = value;
	}
	public short getValue() {
		return value;
	}
}