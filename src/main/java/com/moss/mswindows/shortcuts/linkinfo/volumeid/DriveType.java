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
/**
 * DriveType (4 bytes): A 32-bit, unsigned integer that specifies the type of drive the link target
 * is stored on. This value MUST be one of the following:
 */
public enum DriveType {
	
	/**
	 * The drive type cannot be determined.
	 */
	DRIVE_UNKNOWN(0x00000000),
	
	/**
	 * The root path is invalid; for example, there is no volume mounted at the
	 * path.
	 */
	DRIVE_NO_ROOT_DIR(0x00000001),

	/**
	 * The drive has removable media, such as a floppy drive, thumb drive, or
	 * flash card reader.
	 */
	DRIVE_REMOVABLE(0x00000002),

	/**
	 * The drive has fixed media, such as a hard drive or flash drive.
	 */
	DRIVE_FIXED(0x00000003),

	/**
	 * The drive is a remote (network) drive.
	 */
	DRIVE_REMOTE(0x00000004),

	/**
	 * The drive is a CD-ROM drive.
	 */
	DRIVE_CDROM(0x00000005),

	/**
	 * The drive is a RAM disk.
	 */
	DRIVE_RAMDISK(0x00000006);

	private final int value;

	private DriveType(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}

}
