package com.basics.framework.core.utils.mail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
*  邮件附件
*  @since                   ：2018/8/10
*  @author                  ：zc.ding@foxmail.com
*/
public class Attachment {
	private Map<String, byte[]> mapAttachMent = null;

	public Attachment() {
		mapAttachMent = new HashMap<String, byte[]>();
	}

	public Map<String, byte[]> getMapAttachMent() {
		return mapAttachMent;
	}

	public void setMapAttachMent(Map<String, byte[]> mapAttachMent) {
		this.mapAttachMent = mapAttachMent;
	}

	public void addAttachment(File file) throws IOException {
		mapAttachMent.put(file.getName(), Files.readAllBytes(Paths.get(file.getAbsolutePath())));
	}

	public void addAttachment(String fileName, byte[] bufFile) throws IOException {
		mapAttachMent.put(fileName, bufFile);
	}
}
