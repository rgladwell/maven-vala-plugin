package org.gitorious.rgladwell.maven.plugin.vala;

import static org.mockito.Mockito.*;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugin.testing.stubs.ArtifactStub;
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

@RunWith(MockitoJUnitRunner.class)
public class CompileExecutableMojoTest extends AbstractMojoTestCase {

	CompileExecutableMojo mojo;
	@Mock CommandExecutor commandExecutor;
	@Mock MavenProjectHelper projectHelper;
	@Mock private MavenProject project;

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
		ArtifactStub artifact = new ArtifactStub();
		artifact.setArtifactId("glib");
		artifact.setVersion("2.0");
		artifact.setType("package");
		Set<Artifact> artifacts = new HashSet<Artifact>();
		artifacts.add(artifact);
		when(project.getDependencyArtifacts()).thenReturn(artifacts);
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
		ArtifactStub artifact = new ArtifactStub();
		artifact.setArtifactId("glib");
		artifact.setVersion("2.0");
		artifact.setType("package");
		ArtifactStub artifact2 = new ArtifactStub();
		artifact2.setArtifactId("gio");
		artifact2.setVersion("2.0");
		artifact2.setType("package");
		ArtifactStub artifact3 = new ArtifactStub();
		artifact3.setGroupId("org.gitorious.rgladwell");
		artifact3.setArtifactId("vala-plugin");
		artifact3.setVersion("3.0-SNAPSHOT");
		artifact3.setType("vala-library");
		Set<Artifact> artifacts = new HashSet<Artifact>();
		artifacts.add(artifact);
		artifacts.add(artifact2);
		artifacts.add(artifact3);
		when(project.getDependencyArtifacts()).thenReturn(artifacts);
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
