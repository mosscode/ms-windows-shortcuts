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

public enum FileAttribute {
	/**
	 * The file or directory is read-only. For a file, if this bit
	 * is set, applications can read the file but cannot write 
	 * to it or delete it. For a directory, if this bit is set,
	 * applications cannot delete the directory.
	 */
	
	READONLY,
    
	/**
	 * The file or directory is hidden. If this bit is set, the
	 * file or folder is not included in an ordinary directory
	 * listing.
	 */
    
    HIDDEN,
    
    /**
     * The file or directory is part of the operating system
     * or is used exclusively by the operating system.
     */
    SYSTEM,
    
    /**
     *  A bit that MUST be zero.
     */
    Reserved1,
    
    /**
     * The link target is a directory instead of a file.
     */
     DIRECTORY,
     
     /**
      * The file or directory is an archive file. Applications
      * use this flag to mark files for backup or removal.
      *   
      */
     ARCHIVE,
     
     /**
      * A bit that MUST be zero.
      */
     Reserved2,
     
     /**
      * The file or directory has no other flags set. If this bit
      * is 1, all other bits in this structure MUST be clear.
      */
     NORMAL,
     
     /**
      * The file is being used for temporary storage.
      */
     TEMPORARY,
     
     /**
      * The file is a sparse file.
      */
     SPARSE_FILE,

     /**
      * The file or directory has an associated reparse point.
      */
     REPARSE_POINT,
     
     /**
      * The file or directory is compressed. For a file, this
      * means that all data in the file is compressed. For a 
      * directory, this means that compression is the default
      * for newly created files and subdirectories.
      */
     COMPRESSED,
     
     /**
      * The data of the file is not immediately available.
      */
     OFFLINE,
     
     /**
      * The contents of the file need to be indexed.
      */
     NOT_CONTENT_INDEXED,
     
     /**
      * The file or directory is encrypted. For a file, this
      * means that all data in the file is encrypted. For a
      * directory, this means that encryption is the default
      * for newly created files and subdirectories.
      */
     ENCRYPTED;
	
	public int flagValue(){
		return 1 << ordinal();
	}
	
	public boolean isSet(int flags){
		return (flags & flagValue()) > 0;
	}

}
