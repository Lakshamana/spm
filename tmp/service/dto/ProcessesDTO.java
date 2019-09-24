package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.ufpa.labes.spm.service.dto.ProjectDTO;

@SuppressWarnings("serial")
public class ProcessesDTO implements Serializable {

	private List<ProcessDTO> process;

	public ProcessesDTO() {}

	public ProcessesDTO(List<ProcessDTO> process) {
		this.process = process;
	}

	public boolean addProcess(ProcessDTO processDTO) {
		return this.process.add(processDTO);
	}

	public List<String> getProcessesIdent() {
		List<String> names = new ArrayList<String>();
		for (ProcessDTO processDTO : this.process) {
			names.add(processDTO.getIdent());
		}

		return names;
	}

	public List<ProcessDTO> getProcesses() {
		return process;
	}

}
