package org.gitorious.rgladwell.maven.plugin.vala;

import java.io.File;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.util.DirectoryScanner;

public abstract class CompileMojo extends AbstractMojo {

    /**
     * Project object model.
     *
     * @parameter expression="${project}"
     * @required
     */
	protected MavenProject project;

    /**
     * Location of the source directory for vala source files.
     * 
     * @parameter expression="${project.build.directory}/vala"
     * @required
     */
	protected File sourceDirectory;

	/**
     * Location of the output directory for vala compiled binaries.
     * 
     * @parameter expression="${project.build.directory}/vala"
     * @required
     */
	protected File outputDirectory;

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
    
    /**
     * @component
  	 */
	protected MavenProjectHelper projectHelper;
 
	protected CompileCommand command = new CompileCommand();

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

    	for(Artifact artifact : project.getDependencyArtifacts()) {
    		command.getPackages().add(artifact.getArtifactId() + "-" + artifact.getVersion());
    	}

    	command.setOutputFolder(outputDirectory);

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
