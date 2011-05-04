package org.gitorious.rgladwell.maven.plugin.vala;

import static org.mockito.Mockito.*;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.gitorious.rgladwell.maven.plugin.vala.model.CompileCommand;
import org.gitorious.rgladwell.maven.plugin.vala.model.Library;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.util.artifact.DefaultArtifact;

@RunWith(MockitoJUnitRunner.class)
public class CompileExecutableMojoTest extends AbstractMojoTestCase {

	CompileExecutableMojo mojo;
	@Mock CommandExecutor commandExecutor;
	@Mock MavenProjectHelper projectHelper;
	@Mock private MavenProject project;
	@Mock ProjectDependencyService projectDependencyService;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		mojo = new CompileExecutableMojo();
	}

    @Test
	public void testExecuteForSimpleExecutable() throws Throwable {
		File pom = new File(getBasedir(), "src/test/resources/projects/simple-executable/pom.xml");
		CompileExecutableMojo mojo = (CompileExecutableMojo) lookupMojo ( "valac-executable", pom );
		setVariableValueToObject(mojo, "commandExecutor", commandExecutor);
		setVariableValueToObject(mojo, "compilerName", "valac");
		setVariableValueToObject(mojo, "projectDependencyService", projectDependencyService);
		Artifact artifact = new DefaultArtifact("org.gnome:glib:package:2.0");
		Set<Artifact> artifacts = new HashSet<Artifact>();
		artifacts.add(artifact);
		when(projectDependencyService.collectArtifacts()).thenReturn(artifacts);
		setVariableValueToObject(mojo, "project", project);
		setVariableValueToObject(mojo, "outputExecutableName", "simple-executable");
		setVariableValueToObject(mojo, "projectHelper", projectHelper);

		mojo.execute();

		CompileCommand command = new CompileCommand();
		command.setCommandName("valac");
		command.setBuildName("simple-executable");
		command.getValaSources().add(new File(getBasedir(), "src/test/resources/projects/simple-executable/main.vala"));
		command.getPackages().add("glib-2.0");
		command.setOutputFolder(new File(getBasedir(), "src/test/resources/projects/simple-executable/target"));
		verify(commandExecutor).execute(command);
	}

    @Test
	public void testExecuteForComplexExecutable() throws Throwable {
		File pom = new File(getBasedir(), "src/test/resources/projects/complex-executable/pom.xml");
		CompileExecutableMojo mojo = (CompileExecutableMojo) lookupMojo ( "valac-executable", pom );
		setVariableValueToObject(mojo, "commandExecutor", commandExecutor);
		setVariableValueToObject(mojo, "compilerName", "valac");
		setVariableValueToObject(mojo, "userHome", "/home/test");
		setVariableValueToObject(mojo, "projectDependencyService", projectDependencyService);
		Set<Artifact> artifacts = new HashSet<Artifact>();
		setVariableValueToObject(mojo, "projectDependencyService", projectDependencyService);
		Artifact artifact = new DefaultArtifact("org.gnome:glib:package:2.0");
		Artifact artifact2 = new DefaultArtifact("org.gnome:gio:package:2.0");
		Artifact artifact3 = new DefaultArtifact("org.gitorious.rgladwell:vala-plugin:vala-library:3.0-SNAPSHOT");
		artifacts.add(artifact);
		artifacts.add(artifact2);
		artifacts.add(artifact3);
		when(projectDependencyService.collectArtifacts()).thenReturn(artifacts);
		setVariableValueToObject(mojo, "project", project);
		setVariableValueToObject(mojo, "outputExecutableName", "complex-executable");
		setVariableValueToObject(mojo, "projectHelper", projectHelper);

		mojo.execute();

		CompileCommand command = new CompileCommand();
		command.setCommandName("valac");
		command.setBuildName("complex-executable");
		command.getValaSources().add(new File(getBasedir(), "src/test/resources/projects/complex-executable/src/Test.vala"));
		command.getValaSources().add(new File(getBasedir(), "src/test/resources/projects/complex-executable/main.vala"));
		command.getPackages().add("glib-2.0");
		command.getPackages().add("gio-2.0");
		Library library = new Library();
		library.setBinary(new File("/home/test/.m2/repository/org/gitorious/rgladwell/vala-plugin/3.0-SNAPSHOT/vala-plugin-3.0-SNAPSHOT.so"));
		library.setVapi(new File("/home/test/.m2/repository/org/gitorious/rgladwell/vala-plugin/3.0-SNAPSHOT/vala-plugin-3.0-SNAPSHOT.vapi"));
		command.getLibraries().add(library);
		command.setOutputFolder(new File(getBasedir(), "src/test/resources/projects/complex-executable/target"));
		verify(commandExecutor).execute(command);
		verify(projectHelper).attachArtifact(project, "exe", new File(command.getOutputFolder(), "complex-executable"));
	}

    @Test
	public void testExecuteForSimpleExecutableWithDebug() throws Throwable {
		File pom = new File(getBasedir(), "src/test/resources/projects/simple-executable/pom.xml");
		CompileExecutableMojo mojo = (CompileExecutableMojo) lookupMojo ( "valac-executable", pom );
		setVariableValueToObject(mojo, "commandExecutor", commandExecutor);
		setVariableValueToObject(mojo, "compilerName", "valac");
		MavenProjectStub project = new MavenProjectStub();
		setVariableValueToObject(mojo, "project", project);
		setVariableValueToObject(mojo, "outputExecutableName", "simple-executable");
		setVariableValueToObject(mojo, "projectHelper", projectHelper);
		setVariableValueToObject(mojo, "debug", true);
		setVariableValueToObject(mojo, "projectDependencyService", projectDependencyService);

		mojo.execute();

		CompileCommand command = new CompileCommand();
		command.setCommandName("valac");
		command.setBuildName("simple-executable");
		command.getValaSources().add(new File(getBasedir(), "src/test/resources/projects/simple-executable/main.vala"));
		command.setOutputFolder(new File(getBasedir(), "src/test/resources/projects/simple-executable/target"));
		command.setDebug(true);
		verify(commandExecutor).execute(command);
	}
}
