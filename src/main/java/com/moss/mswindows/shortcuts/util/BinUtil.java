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
package com.moss.mswindows.shortcuts.util;

import java.io.IOException;
import java.io.OutputStream;

public class BinUtil {
	public static long getLong(byte[] in, int idx) {
		long out = 0;
		for (int i = 0; i < 8; i++)
			out |= ((long) in[idx + i] & 0xff) << i * 8;
		return out;
	}

	public static int setLong(byte[] out, int idx, long val) {
		for (int i = 0; i < 8; i++)
			out[idx + i] = (byte) ((val >> i * 8) & 0xff);
		return 8;
	}
	
	public static int writeLong(OutputStream dest, long val) throws IOException {
		byte[] out = new byte[8];
		
		for (int i = 0; i < 8; i++){
			out[i] = (byte) ((val >> i * 8) & 0xff);
		}
		
		dest.write(out);
		return 8;
	}

	public static byte[] subarray(byte[] in, int idx, int len) {
		byte[] out = new byte[len];
		for (int i = 0; i < len; i++)
			out[i] = in[idx + i];
		return out;
	}

	public static String hexDump(byte[] in) {
		String out = new String("");
		for (int i = 0; i < in.length; i++)
			if ((i % 8) == 0 && i != 0)
				out += String.format("\n0x%02X, ", in[i] & 0xff);
			else
				out += String.format("0x%02X, ", in[i] & 0xff);
		return out;
	}

	public static String asciiDump(byte[] in) {
		String out = new String("");
		for (int i = 0; i < in.length; i++)
			out += ((in[i] & 0xFF) == 0) ? '_' : (char) (in[i] & 0xFF);
		// out += (char);
		return out;
	}

	public static int copyUString(byte[] out, byte[] in, int idx) {
		setShort(out, idx, (short) (in.length / 2));
		copyArray(out, in, idx + 2);
		return in.length + 2;
	}

	public static int copyArray(byte[] out, byte[] in, int idx) {
		for (int i = in.length - 1; i >= 0; i--)
			out[idx + i] = in[i];
		return in.length;
	}

	public static short getShort(byte[] in, int idx) {
		short out = 0;
		for (int i = 0; i < 2; i++)
			out |= ((short) in[idx + i] & 0xff) << i * 8;
		return out;
	}

	public static int setShort(byte[] out, int idx, short val) {
		for (int i = 0; i < 2; i++)
			out[idx + i] = (byte) ((val >> i * 8) & 0xff);
		return 2;
	}
	public static int writeShort(OutputStream dest, short val) throws IOException {
		byte[] out = new byte[2];
		for (int i = 0; i < 2; i++){
			out[i] = (byte) ((val >> i * 8) & 0xff);
		}
		dest.write(out);
		return 2;
	}
	public static int getInt(byte[] in, int idx) {
		int out = 0;
		for (int i = 0; i < 4; i++)
			out |= ((int) in[idx + i] & 0xff) << i * 8;
		return out;
	}

	public static int setInt(byte[] out, int idx, int val) {
		for (int i = 0; i < 4; i++)
			out[idx + i] = (byte) ((val >> i * 8) & 0xff);
		return 4;
	}
	public static int writeInt(OutputStream dest, int val) throws IOException {
		byte[] out = new byte[4];
		for (int i = 0; i < 4; i++){
			out[i] = (byte) ((val >> i * 8) & 0xff);
		}
		
		dest.write(out);
		
		return 4;
	}
}
