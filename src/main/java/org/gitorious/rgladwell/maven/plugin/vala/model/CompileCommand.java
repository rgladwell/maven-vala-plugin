package org.gitorious.rgladwell.maven.plugin.vala.model;

import java.io.File;
import java.util.HashSet;
import java.util.Set;


public class CompileCommand extends Command {

	String commandName;
	String buildName;
	Set<File> valaSources = new HashSet<File>();
	Set<String> packages = new HashSet<String>();
	File outputFolder;
	boolean library;
	boolean debug;
	Set<Library> libraries = new HashSet<Library>();
	boolean introspectionMetadata;

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String command) {
		this.commandName = command;
	}

	public String getBuildName() {
    	return buildName;
    }

	public void setBuildName(String buildName) {
    	this.buildName = buildName;
    }

	public Set<File> getValaSources() {
		return valaSources;
	}

	public void setValaSources(Set<File> valaSources) {
		this.valaSources = valaSources;
	}

	public Set<String> getPackages() {
    	return packages;
    }

	public void setPackages(Set<String> packages) {
    	this.packages = packages;
    }

	public File getOutputFolder() {
    	return outputFolder;
    }

	public void setOutputFolder(File outputFolder) {
    	this.outputFolder = outputFolder;
    }

	public boolean isLibrary() {
    	return library;
    }

	public void setLibrary(boolean library) {
    	this.library = library;
    }

	public boolean isDebug() {
    	return debug;
    }

	public void setDebug(boolean debug) {
    	this.debug = debug;
    }

	public Set<Library> getLibraries() {
    	return libraries;
    }

	public void setLibraries(Set<Library> libraries) {
    	this.libraries = libraries;
    }

	public boolean isIntrospectionMetadata() {
    	return introspectionMetadata;
    }

	public void setIntrospectionMetadata(boolean introspectionMetadata) {
    	this.introspectionMetadata = introspectionMetadata;
    }

	@Override
    public String toString() {
	    return "CompileCommand [commandName=" + commandName + ", buildName="
	            + buildName + ", valaSources=" + valaSources + ", packages="
	            + packages + ", outputFolder=" + outputFolder + ", library="
	            + library + ", debug=" + debug + ", libraries=" + libraries
	            + ", introspectionMetadata=" + introspectionMetadata + "]";
    }

	@Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result
	            + ((buildName == null) ? 0 : buildName.hashCode());
	    result = prime * result
	            + ((commandName == null) ? 0 : commandName.hashCode());
	    result = prime * result + (debug ? 1231 : 1237);
	    result = prime * result + (introspectionMetadata ? 1231 : 1237);
	    result = prime * result
	            + ((libraries == null) ? 0 : libraries.hashCode());
	    result = prime * result + (library ? 1231 : 1237);
	    result = prime * result
	            + ((outputFolder == null) ? 0 : outputFolder.hashCode());
	    result = prime * result
	            + ((packages == null) ? 0 : packages.hashCode());
	    result = prime * result
	            + ((valaSources == null) ? 0 : valaSources.hashCode());
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
	    CompileCommand other = (CompileCommand) obj;
	    if (buildName == null) {
		    if (other.buildName != null)
			    return false;
	    } else if (!buildName.equals(other.buildName))
		    return false;
	    if (commandName == null) {
		    if (other.commandName != null)
			    return false;
	    } else if (!commandName.equals(other.commandName))
		    return false;
	    if (debug != other.debug)
		    return false;
	    if (introspectionMetadata != other.introspectionMetadata)
		    return false;
	    if (libraries == null) {
		    if (other.libraries != null)
			    return false;
	    } else if (!libraries.equals(other.libraries))
		    return false;
	    if (library != other.library)
		    return false;
	    if (outputFolder == null) {
		    if (other.outputFolder != null)
			    return false;
	    } else if (!outputFolder.equals(other.outputFolder))
		    return false;
	    if (packages == null) {
		    if (other.packages != null)
			    return false;
	    } else if (!packages.equals(other.packages))
		    return false;
	    if (valaSources == null) {
		    if (other.valaSources != null)
			    return false;
	    } else if (!valaSources.equals(other.valaSources))
		    return false;
	    return true;
    }

}
