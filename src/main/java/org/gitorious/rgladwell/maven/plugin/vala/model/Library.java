package org.gitorious.rgladwell.maven.plugin.vala.model;

import java.io.File;

public class Library {

	File binary;
	File vapi;

	public File getBinary() {
		return binary;
	}

	public void setBinary(File binary) {
		this.binary = binary;
	}

	public File getVapi() {
		return vapi;
	}

	public void setVapi(File vapi) {
		this.vapi = vapi;
	}

	@Override
	public String toString() {
		return "Library [binary=" + binary + ", vapi=" + vapi + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((binary == null) ? 0 : binary.hashCode());
		result = prime * result + ((vapi == null) ? 0 : vapi.hashCode());
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
		Library other = (Library) obj;
		if (binary == null) {
			if (other.binary != null)
				return false;
		} else if (!binary.equals(other.binary))
			return false;
		if (vapi == null) {
			if (other.vapi != null)
				return false;
		} else if (!vapi.equals(other.vapi))
			return false;
		return true;
	}

}
