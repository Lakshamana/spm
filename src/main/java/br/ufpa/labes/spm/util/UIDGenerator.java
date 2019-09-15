package br.ufpa.labes.spm.util;

import java.util.UUID;

public class UIDGenerator {

	public static String generateUID() {
		UUID id = UUID.randomUUID();
		String idStr = Long.toHexString(id.getMostSignificantBits()) + Long.toHexString(id.getLeastSignificantBits());

		return idStr;
	}

}
