package org.gitorious.rgladwell.maven.plugin.vala;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal which compiles a Vala shared library.
 *
 * @goal valac-library
 */
public class CompileLibraryMojo extends CompileMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		command.setLibrary(true);

		super.execute();
		
		File artifactFile = new File(outputDirectory, outputExecutableName+".so");
		projectHelper.attachArtifact(project, "so", artifactFile);
		artifactFile = new File(outputDirectory, outputExecutableName+".vapi");
		projectHelper.attachArtifact(project, "vapi", artifactFile);
		artifactFile = new File(outputDirectory, outputExecutableName+".h");
		projectHelper.attachArtifact(project, "h", artifactFile);
	}

}
