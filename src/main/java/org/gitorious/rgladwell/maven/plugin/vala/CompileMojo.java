package org.gitorious.rgladwell.maven.plugin.vala;

import java.io.File;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.DirectoryScanner;
import org.gitorious.rgladwell.maven.plugin.vala.model.CompileCommand;
import org.gitorious.rgladwell.maven.plugin.vala.model.Library;

public abstract class CompileMojo extends ValaMojo {

    /**
     * Name of the executable output binary.
     * 
     * @parameter expression="${project.artifactId}-${project.version}"
     * @required
     */
	protected String outputExecutableName;

	/**
     * Name of the vala compilation executable.
     * 
     * @parameter default-value="valac"
     */
	private String compilerName;

	/**
     * @required
     * @component role="org.gitorious.rgladwell.maven.plugin.vala.CommandExecutor"
     */
	private CommandExecutor commandExecutor;

	protected CompileCommand command = new CompileCommand();

	/**
	 * @parameter default-value="false";
	 */
	private boolean debug = false;

	public void execute() throws MojoExecutionException, MojoFailureException {
		validate();

    	command.setBuildName(outputExecutableName);

    	command.setCommandName(compilerName);

    	DirectoryScanner scanner = new DirectoryScanner();
    	scanner.setBasedir(sourceDirectory);
    	scanner.setIncludes( new String[]{ "**\\*.vala" } );

    	scanner.scan();

    	for(String file : scanner.getIncludedFiles()) {
    		command.getValaSources().add(new File(sourceDirectory, file));
    	}
    	
    	if(project.getDependencyArtifacts() != null) {    		
	    	for(Artifact artifact : project.getDependencyArtifacts()) {
	    		if("package".equals(artifact.getType())) {
	    			command.getPackages().add(artifact.getArtifactId() + "-" + artifact.getVersion());
	    		} else if("vala-library".equals(artifact.getType())) {
	    			String groupDirectory = "";
	    			for(String label : artifact.getGroupId().split("\\.")) {
	    				groupDirectory += label + "/";
	    			}
	    			Library library = new Library();
	    			String basefolder = userHome+"/.m2/repository/" + groupDirectory + artifact.getArtifactId() + "/" + artifact.getVersion() + "/";
	    			library.setBinary(new File(basefolder + artifact.getArtifactId() + "-" + artifact.getVersion() + ".so"));
	    			library.setVapi(new File(basefolder + artifact.getArtifactId() + "-" + artifact.getVersion() + ".vapi"));
	    			command.getLibraries().add(library);
	    		} else if("vapi".equals(artifact.getType())) {
	    			command.setVapiDirectory(vapiDirectory);
	    		}
	    	}

    	}

    	command.setOutputFolder(outputDirectory);
    	
    	if(debug) {
    		getLog().info("compiling with debug info");
    	}

    	command.setDebug(debug);

    	if(!outputDirectory.exists()) {
    		outputDirectory.mkdirs();
    	}

    	try {
			commandExecutor.execute(command);
		} catch (ValaPluginException e) {
			throw new MojoExecutionException("error during vala compilation", e);
		}
	}

	protected void validate() throws MojoExecutionException {
		if (!sourceDirectory.exists()) {
			throw new MojoExecutionException("vala source directory=[" + sourceDirectory + "] does not exist.");
		}

		if (!sourceDirectory.isDirectory()) {
			throw new MojoExecutionException("vala source directory=[" + sourceDirectory + "] not directory.");
		}
	}

}
