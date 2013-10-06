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

import com.moss.mswindows.shortcuts.Part;
import com.moss.mswindows.shortcuts.impl.UnsignedIntPart;

/**
 * <pre>
 * ShowCommand (4 bytes): A 32-bit unsigned integer that specifies the expected window state
   of an application launched by the link. This value SHOULD be one of the following.
    Value                     Meaning
    SW_SHOWNORMAL             The application is open and its window is open in a normal fashion.
    0x00000001
    SW_SHOWMAXIMIZED          The application is open, and keyboard focus is given to the application,
    0x00000003                but its window is not shown.
    SW_SHOWMINNOACTIVE        The application is open, but its window is not shown. It is not given the
    0x00000007                keyboard focus.
    
  All other values MUST be treated as SW_SHOWNORMAL.
	</pre>
 */
public class ShowCommandPart extends UnsignedIntPart {
	public enum Value {
		/**
		 * The application is open and its window is open in a normal fashion.
		 */
		NORMAL(0x00000001),
		/**
		 * The application is open, and keyboard focus is given to the application, but its window is not shown.
		 */
		MAXIMIZED(0x00000003),
		/**
		 * The application is open, but its window is not shown. It is not given the keyboard focus.
		 */
		MIN_NO_ACTIVE(0x00000007);
		
		private final int value;

		private Value(int value) {
			this.value = value;
		}
		
		public int value() {
			return value;
		}
	}

	public ShowCommandPart(Part next) {
		super(next);
	}
	
	public Value getShowValue() {
		for(Value next : Value.values()){
			if(next.value() == getValue()){
				return next;
			}
		}
		
		/*
		 * Per the docs: "All other values MUST be treated as SW_SHOWNORMAL."
		 */
		return Value.NORMAL;
	}
}
