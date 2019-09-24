package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.ufpa.labes.spm.annotations.IgnoreMapping;

@SuppressWarnings("serial")
public class CompanyDTO implements Serializable{
	private Long id;
	private String ident;
	private String cnpj;
	private String fantasyName;
	private String socialReason;
	private String acronym;
	private String address;
	private String phone;
	private String description;
	private String url;
	private Boolean automaticInstantiation;

	@IgnoreMapping
	private Collection<String> policyEnabled;
	@IgnoreMapping
	private Collection<String> theSystem;
	@IgnoreMapping
	private Collection<String> organizationMetric;
	@IgnoreMapping
	private Collection<String> organizationEstimation;
	@IgnoreMapping
	private List<CompanyUnitDTO> theCompanyUnits;
	public CompanyDTO(){
		ident = "";
		cnpj = "";
		fantasyName = "";
		socialReason = "";
		acronym = "";
		address = "";
		phone = "";
		description = "";
		automaticInstantiation = new Boolean(false);

		policyEnabled = new ArrayList<String>();
		theSystem = new ArrayList<String>();
		organizationMetric = new ArrayList<String>();
		organizationEstimation = new ArrayList<String>();
		setTheCompanyUnits(new ArrayList<CompanyUnitDTO>());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdent() {
		return ident;
	}

	public void setIdent(String ident) {
		this.ident = ident;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getFantasyName() {
		return fantasyName;
	}

	public void setFantasyName(String fantasyName) {
		this.fantasyName = fantasyName;
	}

	public String getSocialReason() {
		return socialReason;
	}

	public void setSocialReason(String socialReason) {
		this.socialReason = socialReason;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/*public Blob getImage() {
		return image;
	}

	public void setImage(Blob image) {
		this.image = image;
	}*/

	public Boolean isAutomaticInstantiation() {
		return automaticInstantiation;
	}

	public void setAutomaticInstantiation(Boolean automaticInstantiation) {
		this.automaticInstantiation = automaticInstantiation;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "CompanyDTO [id=" + id + ", ident=" + ident + ", cnpj=" + cnpj
				+ ", fantasyName=" + fantasyName + ", socialReason="
				+ socialReason + ", acronym=" + acronym + ", address="
				+ address + ", phone=" + phone + ", description=" + description
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ident == null) ? 0 : ident.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompanyDTO other = (CompanyDTO) obj;
		if (ident == null) {
			if (other.ident != null)
				return false;
		} else if (!ident.equals(other.ident))
			return false;
		return true;
	}

	public List<CompanyUnitDTO> getTheCompanyUnits() {
		return theCompanyUnits;
	}

	public void setTheCompanyUnits(List<CompanyUnitDTO> theCompanyUnits) {
		this.theCompanyUnits = theCompanyUnits;
	}
}
