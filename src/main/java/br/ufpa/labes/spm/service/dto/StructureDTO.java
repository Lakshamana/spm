package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;

import org.qrconsult.spm.converter.annotations.IgnoreMapping;

@SuppressWarnings("serial")
public class StructureDTO implements Serializable{
	private Long oid;

	@IgnoreMapping
    private RepositoryDTO theRepository;

	@IgnoreMapping
    private NodeDTO rootElement;

    public StructureDTO() {
        this.theRepository = null;
        this.rootElement = null;
    }

	public Integer getId() {
		return oid;
	}

	public void setId(Long oid) {
		this.oid = oid;
	}

	public RepositoryDTO getTheRepository() {
		return theRepository;
	}

	public void setTheRepository(RepositoryDTO theRepository) {
		this.theRepository = theRepository;
	}

	public NodeDTO getRootElement() {
		return rootElement;
	}

	public void setRootElement(NodeDTO rootElement) {
		this.rootElement = rootElement;
	}

	@Override
	public String toString() {
		return "StructureDTO [oid=" + oid + ", rootElement=" + rootElement
				+ "]";
	}
}
