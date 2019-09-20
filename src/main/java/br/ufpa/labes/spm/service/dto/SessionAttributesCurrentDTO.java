package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
public class SessionAttributesCurrentDTO implements Serializable {
	private String processId;
	private String titleHelpTopic;
	private String contentHelpTopic;

	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getTitleHelpTopic() {
		return titleHelpTopic;
	}
	public void setTitleHelpTopic(String titleHelpTopic) {
		this.titleHelpTopic = titleHelpTopic;
	}
	public String getContentHelpTopic() {
		return contentHelpTopic;
	}
	public void setContentHelpTopic(String contentHelpTopic) {
		this.contentHelpTopic = contentHelpTopic;
	}
}
