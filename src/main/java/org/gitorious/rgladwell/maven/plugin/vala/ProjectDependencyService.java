package org.gitorious.rgladwell.maven.plugin.vala;

import java.util.List;
import java.util.Set;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.repository.RemoteRepository;

public interface ProjectDependencyService {

	Set<Artifact> collectArtifacts() throws MojoFailureException;

	void setRepoSession(RepositorySystemSession repoSession);

	void setRemoteRepos(List<RemoteRepository> remoteRepos);

	void setProject(MavenProject project);

	void setRepoSystem(RepositorySystem repoSystem);

}
