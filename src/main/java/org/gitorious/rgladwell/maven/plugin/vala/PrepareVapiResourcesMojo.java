package org.gitorious.rgladwell.maven.plugin.vala;

import java.io.File;
import java.io.IOException;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.DefaultArtifactHandler;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.FileUtils;

/**
 * Copy required resources to build folder for compilation
 *
 * @goal prepare-vapi-resources
 */
public class PrepareVapiResourcesMojo extends ValaMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		if(!outputDirectory.exists()) {
			outputDirectory.mkdirs();
		}
		for(String vapi : FileUtils.getFilesFromExtension(sourceDirectory.getAbsolutePath(), new String[] { "vapi" })) {
			try {
				File file = new File(vapi);
				FileUtils.copyFileToDirectory(vapi, outputDirectory.getAbsolutePath());
				String artifactId = file.getName().substring(0, file.getName().length() - 5);
	            String mavenVapi = artifactId + project.getVersion() + "vapi";
	            File vapiArtifact = new File(outputDirectory, mavenVapi);
	            FileUtils.copyFile(new File(outputDirectory, file.getName()), vapiArtifact);
	            Artifact artifact = new DefaultArtifact(project.getGroupId(), artifactId, project.getVersion(), "compile", "vapi", null, new DefaultArtifactHandler("vapi"));
	            artifact.setFile(vapiArtifact);
				project.getAttachedArtifacts().add(artifact);
            } catch (IOException e) {
	            throw new MojoExecutionException("error copying vapi files", e);
            }
		}

	}

}
