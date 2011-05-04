package org.gitorious.rgladwell.maven.plugin.vala;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.FileUtils;
import org.sonatype.aether.artifact.Artifact;

/**
 * Copy required resources to build folder for compilation
 *
 * @goal copy-compile-dependencies
 */
public class CopyCompileDependenciesMojo extends ValaMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
    	projectDependencyService.setProject(project);
    	projectDependencyService.setRemoteRepos(remoteRepos);
    	projectDependencyService.setRepoSession(repoSession);
    	projectDependencyService.setRepoSystem(repoSystem);

    	for(Artifact artifact : projectDependencyService.collectArtifacts()) {
			getLog().debug("found depedent artifact=["+artifact+"]");
			
			String groupDirectory = "";
			for(String label : artifact.getGroupId().split("\\.")) {
				groupDirectory += label + "/";
			}
			String basefolder = userHome+"/.m2/repository/" + groupDirectory + artifact.getArtifactId() + "/" + artifact.getVersion() + "/";
			if(artifact.getExtension().equals("vala-library")) {
				try {
					File header = new File(basefolder + artifact.getArtifactId() + "-" + artifact.getVersion() + ".h");
					getLog().info("copying " + header + " to " + outputDirectory);
	                FileUtils.copyFileToDirectory(header, outputDirectory);
					File sharedLibrary = new File(basefolder + artifact.getArtifactId() + "-" + artifact.getVersion() + ".so");
					getLog().info("copying " + sharedLibrary + " to " + outputDirectory);
	                FileUtils.copyFileToDirectory(sharedLibrary, outputDirectory);
                } catch (IOException e) {
	                throw new MojoFailureException("error copying header files to build directory", e);
                }
			} else if("vapi".equals(artifact.getExtension())) {
    			File vapiDep = new File(basefolder + artifact.getArtifactId() + "-" + artifact.getVersion() + ".vapi");
    			File destination = new File(vapiDirectory, artifact.getArtifactId() + ".vapi");
				try {
                    FileUtils.copyFile(vapiDep, destination);
                } catch (IOException e) {
                    throw new MojoFailureException("error copying vapi depenndencies", e);
                }
    		}
		}

	}

}
