package org.gitorious.rgladwell.maven.plugin.vala;

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.repository.RemoteRepository;

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
	 * Location of the output directory for vala compiled binaries.
	 * 
	 * @parameter expression="${project.build.directory}/vala/vapi"
	 * @required
	 */
	protected File vapiDirectory;

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

	/**
	 * The current repository/network configuration of Maven.
	 * 
	 * @parameter default-value="${repositorySystemSession}"
	 * @readonly
	 */
	protected RepositorySystemSession repoSession;

	/**
	 * The project's remote repositories to use for the resolution.
	 * 
	 * @parameter default-value="${project.remoteProjectRepositories}"
	 * @readonly
	 */
	protected List<RemoteRepository> remoteRepos;

	/**
	 * @component
	 */
	protected ProjectDependencyService projectDependencyService;

	/**
	 * @component
	 */
	protected RepositorySystem repoSystem;

}
