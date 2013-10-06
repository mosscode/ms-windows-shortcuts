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

public enum NetworkProviderType {
	AVID(0x001A0000),
	DOCUSPACE(0x001B0000),
	MANGOSOFT(0x001C0000),
	SERNET(0x001D0000),
	RIVERFRONT1(0X001E0000),
	RIVERFRONT2(0x001F0000),
	DECORB(0x00200000),
	PROTSTOR(0x00210000),
	FJREDIR(0x00220000),
	DISTINCT(0x00230000),
	TWINS(0x00240000),
	RDR2SAMPLE(0x00250000),
	CSC(0x00260000),
	_3IN1(0x00270000),
	EXTENDNET(0x00290000),
	STAC(0x002A0000),
	FOXBAT(0x002B0000),
	YAHOO(0x002C0000),
	EXIFS(0x002D0000),
	DAV(0x002E0000),
	KNOWARE(0x002F0000),
	OBJECTDIRE(0x00300000),
	MASFAX(0x00310000),
	HOBNFS(0x00320000),
	SHIVA(0x00330000),
	IBMAL(0x00340000),
	LOCK(0x00350000),
	TERMSRV(0x00360000),
	SRT(0x00370000),
	QUINCY(0x00380000),
	OPENAFS(0x00390000),
	AVID1(0X003A0000),
	DFS(0x003B0000),
	KWNP(0x003C0000),
	ZENWORKS(0x003D0000),
	DRIVEONWEB(0x003E0000),
	VMWARE(0x003F0000),
	RSFX(0x00400000),
	MFILES(0x00410000),
	MSNFS(0x00420000),
	GOOGLE(0x00430000);
	
	private final int value;

	private NetworkProviderType(int value) {
		this.value = value;
	}
	
	public int value() {
		return value;
	}

}
