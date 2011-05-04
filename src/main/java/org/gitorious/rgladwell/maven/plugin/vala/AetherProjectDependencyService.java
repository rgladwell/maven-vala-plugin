package org.gitorious.rgladwell.maven.plugin.vala;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.collection.CollectRequest;
import org.sonatype.aether.collection.CollectResult;
import org.sonatype.aether.collection.DependencyCollectionException;
import org.sonatype.aether.graph.Dependency;
import org.sonatype.aether.graph.DependencyNode;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.util.artifact.DefaultArtifact;

public class AetherProjectDependencyService implements ProjectDependencyService {

	private RepositorySystem repoSystem;

	private RepositorySystemSession repoSession;

	private List<RemoteRepository> remoteRepos;

	private MavenProject project;

	public void setRepoSystem(RepositorySystem repoSystem) {
    	this.repoSystem = repoSystem;
    }

	public void setRepoSession(RepositorySystemSession repoSession) {
    	this.repoSession = repoSession;
    }

	public void setRemoteRepos(List<RemoteRepository> remoteRepos) {
    	this.remoteRepos = remoteRepos;
    }

	public void setProject(MavenProject project) {
    	this.project = project;
    }

	public Set<Artifact> collectArtifacts() throws MojoFailureException {
		Set<Artifact> results = new HashSet<Artifact>();

        try {
	        CollectRequest request = new CollectRequest();
	        Artifact projectArtifact = new DefaultArtifact(project.getGroupId(), project.getArtifactId(), project.getArtifact().getArtifactHandler().getExtension(), project.getVersion());
	        Dependency dependency = new Dependency(projectArtifact, "compile");
	        request.setRoot(dependency);
	        request.setRepositories( remoteRepos );
			CollectResult result = repoSystem.collectDependencies(repoSession, request );
			for(DependencyNode node : result.getRoot().getChildren()) {
				Artifact artifact = node.getDependency().getArtifact();
				results.add(artifact);
	
	    	}
        } catch (DependencyCollectionException e) {
	        throw new MojoFailureException("", e);
        }
		
		return results;
	}

}
