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
package com.moss.mswindows.shortcuts.extradata;

public enum ExtraDataBlockTypeSignature {
	// A ConsoleDataBlock structure (section 2.5.1).
	CONSOLE_PROPS(0xA0000002){
		@Override
		public ExtraDataBlock newInstance() {
			return new ConsoleDataBlock();
		}
	},
	
	// A ConsoleFEDataBlock structure (section 2.5.2).
	CONSOLE_FE_PROPS(0xA0000004){
		@Override
		public ExtraDataBlock newInstance() {
			return new ConsoleFEDataBlock();
		}
	},
	
	// A DarwinDataBlock structure (section 2.5.3).
	DARWIN_PROPS(0xA0000006){
		@Override
		public ExtraDataBlock newInstance() {
			return new DarwinDataBlock();
		}
	},
	
	// An EnvironmentVariableDataBlock structure (section 2.5.4).
	ENVIRONMENT_PROPS(0xA0000001){
		@Override
		public ExtraDataBlock newInstance() {
			return new EnvironmentVariableDataBlock();
		}
	},
	
	// An IconEnvironmentDataBlock structure (section 2.5.5).
	ICON_ENVIRONMENT_PROPS(0xA0000007){
		@Override
		public ExtraDataBlock newInstance() {
			return new IconEnvironmentDataBlock();
		}
	},
	
	// A KnownFolderDataBlock structure (section 2.5.6).
	KNOWN_FOLDER_PROPS(0xA000000B){
		@Override
		public ExtraDataBlock newInstance() {
			return new KnownFolderDataBlock();
		}
	},
	
	// A PropertyStoreDataBlock structure (section 2.5.7).
	PROPERTY_STORE_PROPS(0xA0000009){
		@Override
		public ExtraDataBlock newInstance() {
			return new PropertyStoreDataBlock();
		}
	},
	
	// A ShimDataBlock structure (section 2.5.8).
	SHIM_PROPS(0xA0000008){
		@Override
		public ExtraDataBlock newInstance() {
			return new ShimDataBlock();
		}
	},
	
	// A SpecialFolderDataBlock structure (section 2.5.9).
	SPECIAL_FOLDER_PROPS(0xA0000005){
		@Override
		public ExtraDataBlock newInstance() {
			return new SpecialFolderDataBlock();
		}
	},
	
	// A TrackerDataBlock structure (section 2.5.10).
	TRACKER_PROPS(0xA0000003){
		@Override
		public ExtraDataBlock newInstance() {
			return new TrackerDataBlock();
		}
	},
	
	// A VistaAndAboveIDListDataBlock structure (section 2.5.11).
	VISTA_AND_ABOVE_IDLIST_PROPS(0xA000000C){
		@Override
		public ExtraDataBlock newInstance() {
			return new VistaAndAboveIDListDataBlock();
		}
	}
	;

	private final int signatureValue;

	private ExtraDataBlockTypeSignature(int signatureValue) {
		this.signatureValue = signatureValue;
	}
	public int signatureValue() {
		return signatureValue;
	}
	public abstract ExtraDataBlock newInstance();

}
