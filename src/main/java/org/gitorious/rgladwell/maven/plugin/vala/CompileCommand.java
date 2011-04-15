package org.gitorious.rgladwell.maven.plugin.vala;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class CompileCommand extends Command {

	String commandName;
	Set<File> valaSources = new HashSet<File>();
	Set<String> packages = new HashSet<String>();
	File outputFile;

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String command) {
		this.commandName = command;
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

	public File getOutputFile() {
    	return outputFile;
    }

	public void setOutputFile(File outputFile) {
    	this.outputFile = outputFile;
    }

	@Override
    public String toString() {
	    return "CompileCommand [commandName=" + commandName + ", valaSources=" + valaSources + ", packages=" + packages + ", outputFile=" + outputFile + "]";
    }

	@Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((commandName == null) ? 0 : commandName.hashCode());
	    result = prime * result + ((outputFile == null) ? 0 : outputFile.hashCode());
	    result = prime * result + ((packages == null) ? 0 : packages.hashCode());
	    result = prime * result + ((valaSources == null) ? 0 : valaSources.hashCode());
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
	    if (commandName == null) {
		    if (other.commandName != null)
			    return false;
	    } else if (!commandName.equals(other.commandName))
		    return false;
	    if (outputFile == null) {
		    if (other.outputFile != null)
			    return false;
	    } else if (!outputFile.equals(other.outputFile))
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
