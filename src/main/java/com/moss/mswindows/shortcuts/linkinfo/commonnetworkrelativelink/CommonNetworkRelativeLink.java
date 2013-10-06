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
package com.moss.mswindows.shortcuts.linkinfo.commonnetworkrelativelink;

import java.io.IOException;
import java.io.OutputStream;

import com.moss.mswindows.shortcuts.impl.IndexedField;
import com.moss.mswindows.shortcuts.impl.NullTerminatedStringPart;
import com.moss.mswindows.shortcuts.impl.UnsignedIntPart;

/**
 * CommonNetworkRelativeLink
 * The CommonNetworkRelativeLink structure specifies information about the network location where a
 * link target is stored, including the mapped drive letter and the UNC path prefix. For details on UNC
 * paths, see [MS-DFSNM] section 2.2.1.4.
 */
public class CommonNetworkRelativeLink implements IndexedField {
	/**
	 * CommonNetworkRelativeLinkSize (4 bytes): A 32-bit, unsigned integer that specifies the
	 * size, in bytes, of the CommonNetworkRelativeLink structure. This value MUST be greater than
	 * or equal to 0x00000014. All offsets specified in this structure MUST be less than this value,
	 * and all strings contained in this structure MUST fit within the extent defined by this size.
	 */
	private UnsignedIntPart commonNetworkRelativeLinkSize = new UnsignedIntPart(null);
	
	/**
	 * CommonNetworkRelativeLinkFlags (4 bytes): Flags that specify the contents of the
	 * DeviceNameOffset and NetProviderType fields.
	 */
	private CommonNetworkRelativeLinkFlagsPart commonNetworkRelativeLinkFlags = new CommonNetworkRelativeLinkFlagsPart(commonNetworkRelativeLinkSize);
	
	/**
	 * NetNameOffset (4 bytes): A 32-bit, unsigned integer that specifies the location of the
	 * NetName field. This value is an offset, in bytes, from the start of the
	 * CommonNetworkRelativeLink structure.
	 */
	private UnsignedIntPart netNameOffset = new UnsignedIntPart(commonNetworkRelativeLinkFlags);
	
	/**
	 * DeviceNameOffset (4 bytes): A 32-bit, unsigned integer that specifies the location of the
	 * DeviceName field. If the ValidDevice flag is set, this value is an offset, in bytes, from the
	 * start of the CommonNetworkRelativeLink structure; otherwise, this value MUST be zero.
	 */
	private UnsignedIntPart deviceNameOffset = new UnsignedIntPart(netNameOffset);
	
	/**
	 * NetworkProviderType (4 bytes): A 32-bit, unsigned integer that specifies the type of network
	 * provider. If the ValidNetType flag is set, this value MUST be one of the following; otherwise,
	 * this value MUST be ignored.
	 */
	
	private NetworkProviderTypePart networkProviderType = new NetworkProviderTypePart(deviceNameOffset);
	
	/**
	 * NetNameOffsetUnicode (4 bytes): An optional, 32-bit, unsigned integer that specifies the
	 * location of the NetNameUnicode field. This value is an offset, in bytes, from the start of the
	 * CommonNetworkRelativeLink structure. This field MUST be present if the value of the
	 * NetNameOffset field is greater than 0x00000014; otherwise, this field MUST NOT be
	 * present.
	 */
	private UnsignedIntPart netNameOffsetUnicode = new UnsignedIntPart(networkProviderType);
	
	/**
	 * DeviceNameOffsetUnicode (4 bytes): An optional, 32-bit, unsigned integer that specifies the
	 * location of the DeviceNameUnicode field. This value is an offset, in bytes, from the start of
	 * the CommonNetworkRelativeLink structure. This field MUST be present if the value of the
	 * NetNameOffset field is greater than 0x00000014; otherwise, this field MUST NOT be
	 * present.
	 */
	private UnsignedIntPart deviceNameOffsetUnicode = new UnsignedIntPart(netNameOffsetUnicode);
	
	/**
	 * NetName (variable): A NULL–terminated, ANSI string that specifies a server share path; for
	 * example, "\\server\share".
	 */
	private NullTerminatedStringPart netName = new NullTerminatedStringPart(deviceNameOffsetUnicode);
	
	/**
	 * DeviceName (variable): A NULL–terminated, ANSI string that specifies a device; for example,
	 * the drive letter "D:".
	 */
	private NullTerminatedStringPart deviceName = new NullTerminatedStringPart(netName);
	
	/**
	 * NetNameUnicode (variable): An optional, NULL–terminated, Unicode string that is the
	 * Unicode version of the NetName string. This field MUST be present if the value of the
	 * NetNameOffset field is greater than 0x00000014; otherwise, this field MUST NOT be
	 * present.
	 */
	private NullTerminatedStringPart netNameUnicode = new NullTerminatedStringPart(deviceName);
	
	/**
	 * DeviceNameUnicode (variable): An optional, NULL–terminated, Unicode string that is the
	 * Unicode version of the DeviceName string. This field MUST be present if the value of the
	 * NetNameOffset field is greater than 0x00000014; otherwise, this field MUST NOT be
	 * present.
	 */
	private NullTerminatedStringPart deviceNameUnicode = new NullTerminatedStringPart(netNameUnicode);
	
	boolean hasValue = false;
	
	public void read(byte[] data, int idx) {
		hasValue = true;
		throw new RuntimeException("NOT YET IMPLEMENTED");
	}
	public int write(OutputStream out) throws IOException {
		if(hasValue){
			throw new RuntimeException("NOT YET IMPLEMENTED");
		}else{
			return 0;
		}
	}
	
	
	public boolean hasValue() {
		return hasValue;
	}
	
	@Override
	public String toString() {
		return hasValue?"NOT_IMPLEMENTED":"[no-value]";
	}
}
