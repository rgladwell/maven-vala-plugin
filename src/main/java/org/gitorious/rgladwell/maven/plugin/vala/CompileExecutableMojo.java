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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;

/**
 * Goal which compiles a Vala executable
 *
 * @goal valac-executable
 * @requiresDependencyCollection compile
 */
public class CompileExecutableMojo extends CompileMojo {

  	@Override
  	public void execute() throws MojoExecutionException, MojoFailureException {
    	super.execute();
		File artifactFile = new File(outputDirectory, outputExecutableName);
    	projectHelper.attachArtifact(project, "exe", artifactFile);  
    }

}
