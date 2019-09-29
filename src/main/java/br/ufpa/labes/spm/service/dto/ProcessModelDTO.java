package br.ufpa.labes.spm.service.dto;

import java.util.List;


public class ProcessModelDTO {

	private String pmState;

	private String theProcess;

	private TemplateDTO theOrigin;

	private List<String> theActivity;

	public String getPmState() {
		return pmState;
	}

	public void setPmState(String pmState) {
		this.pmState = pmState;
	}

	public String getTheProcess() {
		return theProcess;
	}

	public void setTheProcess(String theProcess) {
		this.theProcess = theProcess;
	}

	public TemplateDTO getTheOrigin() {
		return theOrigin;
	}

	public void setTheOrigin(TemplateDTO theOrigin) {
		this.theOrigin = theOrigin;
	}

	public List<String> getTheActivity() {
		return theActivity;
	}

	public void setTheActivity(List<String> theActivity) {
		this.theActivity = theActivity;
	}

}
