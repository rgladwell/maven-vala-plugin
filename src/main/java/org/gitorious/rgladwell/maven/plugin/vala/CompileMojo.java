package org.gitorious.rgladwell.maven.plugin.vala;

/*
 * Copyright 2011 Ricardo Gladwell.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.util.DirectoryScanner;

import java.io.File;

/**
 * Goal which touches a timestamp file.
 *
 * @goal valac
 */
public class CompileMojo extends AbstractMojo {

    /**
     * Project object model.
     *
     * @parameter expression="${project}"
     * @required
     */
    private MavenProject project;

    /**
     * Location of the source directory for vala source files.
     * 
     * @parameter expression="${project.build.directory}/vala"
     * @required
     */
	private File sourceDirectory;

	/**
     * Location of the output directory for vala compiled binaries.
     * 
     * @parameter expression="${project.build.directory}/vala"
     * @required
     */
    private File outputDirectory;

    /**
     * Name of the executable output binary.
     * 
     * @parameter expression="${project.artifactId}-${project.version}"
     * @required
     */
    private String outputExecutableName;

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
  	private MavenProjectHelper projectHelper;
 
  	public void execute() throws MojoExecutionException {
    	validate();

    	CompileCommand command = new CompileCommand();
    	command.setBuildName(outputExecutableName);

    	if("vala-library".equals(project.getPackaging())) {
    		command.setLibrary(true);
    	}

    	command.setCommandName(compilerName);

    	DirectoryScanner scanner = new DirectoryScanner();
    	scanner.setBasedir(sourceDirectory);
    	scanner.setIncludes( new String[]{ "**\\*.vala" } );

    	scanner.scan();

    	for(Artifact artifact : project.getDependencyArtifacts()) {
    		command.getPackages().add(artifact.getArtifactId() + "-" + artifact.getVersion());
    	}
    	
    	for(String file : scanner.getIncludedFiles()) {
    		command.getValaSources().add(new File(sourceDirectory, file));
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

		File artifactFile = null;

    	if("vala-library".equals(project.getPackaging())) {
    		artifactFile = new File(outputDirectory, outputExecutableName+".so");
    		projectHelper.attachArtifact(project, "so", artifactFile);
    		artifactFile = new File(outputDirectory, outputExecutableName+".vapi");
    		projectHelper.attachArtifact(project, "vapi", artifactFile);
    	} else {
    		artifactFile = new File(outputDirectory, outputExecutableName);
    		projectHelper.attachArtifact(project, "exe", artifactFile);
    	}    
    }

	private void validate() throws MojoExecutionException {
		if (!sourceDirectory.exists()) {
			throw new MojoExecutionException("vala source directory=[" + sourceDirectory + "] does not exist.");
		}

		if (!sourceDirectory.isDirectory()) {
			throw new MojoExecutionException("vala source directory=[" + sourceDirectory + "] not directory.");
		}
	}

}
