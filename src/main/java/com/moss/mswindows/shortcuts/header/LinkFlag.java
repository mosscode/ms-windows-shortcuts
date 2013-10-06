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

public enum LinkFlag {
	/**
	 * The shell link is saved with an item ID list (IDList). If this bit is set,
	 * a LinkTargetIDList structure (section 2.2) MUST follow the HasLinkTargetIDList
	 * ShellLinkHeader
	 */
	HAS_LINK_TARGET_ID_LIST,
	
	/**
	 * The shell link is saved with link information. If this bit is set, a 
	 * LinkInfo structure (section 2.3) MUST be present 
	 */
	HAS_LINK_INFO,
	
	/**
	 * The shell link is saved with a name string. If this bit is set, a NAME_STRING 
	 * StringData structure (section 2.4) MUST be present.
	 */
	HAS_NAME,
	
	/** 
	 * The shell link is saved with a relative path string. If this bit is set, 
	 * a RELATIVE_PATH StringData structure (section 2.4) MUST be present.
	 */
	HAS_RELATIVE_PATH,
	
	/**
	 * The shell link is saved with a working directory string. If this bit is
	 * set, a WORKING_DIR StringData structure (section 2.4) MUST be present.
	 */
	HAS_WORKING_DIR,
	
	/**
	 * The shell link is saved with command line arguments. If this bit is set, a 
	 * COMMAND_LINE_ARGUMENTS StringData structure (section 2.4) MUST be present.
	 */
	HAS_ARGUMENTS,
	
	/**
	 * The shell link is saved with an icon location string. If this bit is set, 
	 * an ICON_LOCATION StringData structure (section 2.4) MUST be present.
	 */
	HAS_ICON_LOCATION,
	
	/**
	 * The shell link contains Unicode encoded strings. This bit SHOULD be set. 
	 */
	IS_UNICODE,
	
	/**
	 * The LinkInfo structure (section 2.3) is ignored.
	 */
	FORCE_NO_LINK_INFO,
	
	/**
	 * The shell link is saved with an EnvironmentVariableDataBlock (section 2.5.4).
	 */
	HAS_EXP_STRING,
	/**
	 * The target is run in a separate virtual machine when launching a
	 * link target that is a 16-bit application.
	 */
	RUN_IN_SEPARATE_PROCESS,
	
	/**
	 * A bit that is undefined and MUST be ignored.
	 */
	UNUSED1,
	
	/**
	 * The shell link is saved with a DarwinDataBlock (section 2.5.3).
	 */
	HAS_DARWIN_ID,
	
	/**
	 * The application is run as a different user when the target of the shell link is activated.
	 */
	RunAsUser,
	
	/**
	 * The shell link is saved with an IconEnvironmentDataBlock (section 2.5.5).
	 */
	HAS_EXP_ICON,
	
	/**
	 * The file system location is represented in the shell namespace when the path 
	 * to an item is parsed into an IDList.
	 */
	NO_PIDL_ALIAS,
	
	/**
	 * A bit that is undefined and MUST be ignored.
	 */
	UNUSED2,
	
	/**
	 * The shell link is saved with a ShimDataBlock (section 2.5.8).
	 */
	RUN_WITH_SHIM_LAYER,
	
	/**
	 * The TrackerDataBlock (section 2.5.10) is ignored.
	 */
	FORCE_NO_LINK_TRACK,
	
	/**
	 * The shell link attempts to collect target properties and store them 
	 * in the PropertyStoreDataBlock (section 2.5.7) when the link target is set.
	 */
	ENABLE_TARGET_METADATA,
	
	/**
	 * The EnvironmentVariableDataBlock is ignored.
	 */
	DIABLE_LINK_PATH_TRACKING,
	
	/**
	 * The SpecialFolderDataBlock (section 2.5.9) and the KnownFolderDataBlock 
	 * (section 2.5.6) are ignored when loading the shell link. If this bit is set, 
	 * these extra data blocks SHOULD NOT be saved when saving the shell link.
	 */
	DIABLE_KNOWN_FOLDER_TRACKING,
	
	/**
	 * If the link has a KnownFolderDataBlock (section 2.5.6), the unaliased form of 
	 * the known folder IDList SHOULD be used when  translating 
	 * the target IDList at the time that the link is loaded.
	 */
	DISABLE_KNOWN_FOLDER_ALIAS,
	
	/**
	 * Creating a link that references another link is enabled. Otherwise,
	 * specifying a link as the target IDList SHOULD NOT be allowed.
	 */
	ALLOW_LINK_TO_LINK,
	
	/**
	 * When saving a link for which the target IDList is under a known folder, 
	 * either the unaliased form of that known folder or the target IDList SHOULD be used.
	 */
	UNALIAS_ON_SAVE,
	
	/**
	 * The target IDList SHOULD NOT be stored; instead, the path 
	 * specified in the EnvironmentVariableDataBlock (section 2.5.4) 
	 * SHOULD be used to refer to the target.
	 */
	PREFER_ENVIRONMENT_PATH,
	
	/**
	 * When the target is a UNC name that refers to a location on a local 
	 * machine, the local path IDList in the PropertyStoreDataBlock
	 * (section 2.5.7) SHOULD be stored, so it can be used when the link 
	 * is loaded on the local machine.
	 */
	KEEP_LOCAL_ID_LIST_FOR_UNC_TARGET
	;

	public int flagValue(){
		return 1 << ordinal();
	}
	
	public boolean isSet(int flags){
		return (flags & flagValue()) > 0;
	}
}
