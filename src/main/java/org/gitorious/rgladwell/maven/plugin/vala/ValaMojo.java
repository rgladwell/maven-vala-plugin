package org.gitorious.rgladwell.maven.plugin.vala;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

public abstract class ValaMojo extends AbstractMojo {

    /**
     * Project object model.
     *
     * @parameter expression="${project}"
     * @required
     */
	protected MavenProject project;

	/**
     * Location of the output directory for vala compiled binaries.
     * 
     * @parameter expression="${project.build.directory}/vala"
     * @required
     */
	protected File outputDirectory;
	
	/**
	 * @parameter expression="${user.home}"
	 */
	protected String userHome;

    /**
     * Location of the source directory for vala source files.
     * 
     * @parameter expression="${project.build.directory}/vala"
     * @required
     */
	protected File sourceDirectory;
    
    /**
     * @component
  	 */
	protected MavenProjectHelper projectHelper;
 
}
