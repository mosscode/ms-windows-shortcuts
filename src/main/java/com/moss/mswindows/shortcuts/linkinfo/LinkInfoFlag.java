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
package com.moss.mswindows.shortcuts.linkinfo;

public enum LinkInfoFlag {
	/** If set, the VolumeID and LocalBasePath fields are
    present, and their locations are specified by the

    values of the VolumeIDOffset and
    LocalBasePathOffset fields, respectively. If the
    value of the LinkInfoHeaderSize field is greater
    than or equal to 0x00000024, the
    LocalBasePathUnicode field is present, and its
    location is specified by the value of the
    LocalBasePathOffsetUnicode field.
    If not set, the VolumeID, LocalBasePath, and
    LocalBasePathUnicode fields are not present, and
    the values of the VolumeIDOffset and
    LocalBasePathOffset fields are zero. If the value of
    the LinkInfoHeaderSize field is greater than or
    equal to 0x00000024, the value of the
    LocalBasePathOffsetUnicode field is zero.
	 */
	VolumeIDAndLocalBasePath,
	/**
	 *  If set, the CommonNetworkRelativeLink field is
    present, and its location is specified by the value of
    the CommonNetworkRelativeLinkOffset field.
    If not set, the CommonNetworkRelativeLink field
    is not present, and the value of the
    CommonNetworkRelativeLinkOffset field is zero.

	 */
	CommonNetworkRelativeLinkAndPathSuffix;


	public int flagValue(){
		return 1 << ordinal();
	}

	public boolean isSet(int flags){
		return (flags & flagValue()) > 0;
	}
}
