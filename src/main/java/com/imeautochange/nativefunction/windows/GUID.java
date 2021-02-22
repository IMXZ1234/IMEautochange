package com.imeautochange.nativefunction.windows;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

public class GUID extends Structure {
	/**
	 * Proper String should be like: "%08X-%04X-%04X-%02X%02X-%02X%02X%02X%02X%02X%02X"
	 * @param guidString
	 */
	public GUID(String guidString) {
		super();
		String[] subStrings = guidString.split("-");
		if(subStrings.length != 5||
			  !(subStrings[0].matches("[a-fA-F0-9]{8}")&&
				subStrings[1].matches("[a-fA-F0-9]{4}")&&
				subStrings[2].matches("[a-fA-F0-9]{4}")&&
				subStrings[3].matches("[a-fA-F0-9]{4}")&&
				subStrings[4].matches("[a-fA-F0-9]{12}"))) {
			throw new IllegalArgumentException("Not valid Guid String");
		}
		Data1 = Integer.parseUnsignedInt(subStrings[0], 16);
		Data2 = (short)Integer.parseUnsignedInt(subStrings[1], 16);
		Data3 = (short)Integer.parseUnsignedInt(subStrings[2], 16);
		Data4[0] = (byte)Integer.parseUnsignedInt(subStrings[3].substring(0, 2), 16);
		Data4[1] = (byte)Integer.parseUnsignedInt(subStrings[3].substring(2, 4), 16);
		Data4[2] = (byte)Integer.parseUnsignedInt(subStrings[4].substring(0, 2), 16);
		Data4[3] = (byte)Integer.parseUnsignedInt(subStrings[4].substring(2, 4), 16);
		Data4[4] = (byte)Integer.parseUnsignedInt(subStrings[4].substring(4, 6), 16);
		Data4[5] = (byte)Integer.parseUnsignedInt(subStrings[4].substring(6, 8), 16);
		Data4[6] = (byte)Integer.parseUnsignedInt(subStrings[4].substring(8, 10), 16);
		Data4[7] = (byte)Integer.parseUnsignedInt(subStrings[4].substring(10, 12), 16);
	}
	public GUID(byte[] data) {
		super();
        if (data.length != 16) {
            throw new IllegalArgumentException("Invalid data length: " + data.length);
        }

        long data1Temp = data[0] & 0xff;
        data1Temp <<= 8;
        data1Temp |= data[1] & 0xff;
        data1Temp <<= 8;
        data1Temp |= data[2] & 0xff;
        data1Temp <<= 8;
        data1Temp |= data[3] & 0xff;
        Data1 = (int) data1Temp;

        int data2Temp = data[4] & 0xff;
        data2Temp <<= 8;
        data2Temp |= data[5] & 0xff;
        Data2 = (short) data2Temp;

        int data3Temp = data[6] & 0xff;
        data3Temp <<= 8;
        data3Temp |= data[7] & 0xff;
        Data3 = (short) data3Temp;

        Data4[0] = data[8];
        Data4[1] = data[9];
        Data4[2] = data[10];
        Data4[3] = data[11];
        Data4[4] = data[12];
        Data4[5] = data[13];
        Data4[6] = data[14];
        Data4[7] = data[15];
    }
	public GUID() {
		super();
	}
	public static class ByReference extends GUID implements Structure.ByReference {
		public ByReference(String guidString) {
			super(guidString);
		}
		public ByReference() {
			super();
		}
		public ByReference(byte[] data) {
			super(data);
		}
	}

	public int Data1;
	public short Data2;
	public short Data3;
	public byte[] Data4 = new byte[8];
	@Override
	protected List<String> getFieldOrder() {
		return Arrays.asList(
				"Data1",
				"Data2",
				"Data3",
				"Data4");
	}
	public static String getStringRepresentation(GUID guid) {
		return String.format("%08X-%04X-%04X-%02X%02X-%02X%02X%02X%02X%02X%02X",
				guid.Data1,
				guid.Data2,
				guid.Data3,
				guid.Data4[0],
				guid.Data4[1],
				guid.Data4[2],
				guid.Data4[3],
				guid.Data4[4],
				guid.Data4[5],
				guid.Data4[6],
				guid.Data4[7]);
	}
}
