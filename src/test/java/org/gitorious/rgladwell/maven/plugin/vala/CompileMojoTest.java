package org.gitorious.rgladwell.maven.plugin.vala;

import static org.mockito.Mockito.*;

import java.io.File;
import java.util.HashSet;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugin.testing.stubs.ArtifactStub;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CompileMojoTest extends AbstractMojoTestCase {

	CompileMojo mojo;
	@Mock CommandExecutor commandExecutor;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		mojo = new CompileMojo();
	}

    @Test
	@SuppressWarnings("unchecked")
	public void testExecuteForSimpleExecutable() throws Throwable {
		File pom = new File(getBasedir(), "src/test/resources/projects/simple-executable/pom.xml");
		CompileMojo mojo = (CompileMojo) lookupMojo ( "vala-compile", pom );
		setVariableValueToObject(mojo, "commandExecutor", commandExecutor);
		setVariableValueToObject(mojo, "compilerName", "valac");
		MavenProjectStub project = new MavenProjectStub();
		Artifact artifact = new ArtifactStub();
		artifact.setArtifactId("glib");
		artifact.setVersion("2.0");
		project.setDependencyArtifacts(new HashSet<Artifact>());
		project.getDependencyArtifacts().add(artifact);
		setVariableValueToObject(mojo, "project", project);
		setVariableValueToObject(mojo, "outputExecutableName", "simple-executable");

		mojo.execute();

		CompileCommand command = new CompileCommand();
		command.setCommandName("valac");
		command.getValaSources().add(new File(getBasedir(), "src/test/resources/projects/simple-executable/main.vala"));
		command.getPackages().add("glib-2.0");
		command.setOutputFile(new File(getBasedir(), "src/test/resources/projects/simple-executable/target/simple-executable"));
		verify(commandExecutor).execute(command);
	}

    @Test
	@SuppressWarnings("unchecked")
	public void testExecuteForComplexExecutable() throws Throwable {
		File pom = new File(getBasedir(), "src/test/resources/projects/complex-executable/pom.xml");
		CompileMojo mojo = (CompileMojo) lookupMojo ( "vala-compile", pom );
		setVariableValueToObject(mojo, "commandExecutor", commandExecutor);
		setVariableValueToObject(mojo, "compilerName", "valac");
		MavenProjectStub project = new MavenProjectStub();
		Artifact artifact = new ArtifactStub();
		artifact.setArtifactId("glib");
		artifact.setVersion("2.0");
		Artifact artifact2 = new ArtifactStub();
		artifact2.setArtifactId("gio");
		artifact2.setVersion("2.0");
		project.setDependencyArtifacts(new HashSet<Artifact>());
		project.getDependencyArtifacts().add(artifact);
		project.getDependencyArtifacts().add(artifact2);
		setVariableValueToObject(mojo, "project", project);
		setVariableValueToObject(mojo, "outputExecutableName", "complex-executable");

		mojo.execute();

		CompileCommand command = new CompileCommand();
		command.setCommandName("valac");
		command.getValaSources().add(new File(getBasedir(), "src/test/resources/projects/complex-executable/src/Test.vala"));
		command.getValaSources().add(new File(getBasedir(), "src/test/resources/projects/complex-executable/main.vala"));
		command.getPackages().add("glib-2.0");
		command.getPackages().add("gio-2.0");
		command.setOutputFile(new File(getBasedir(), "src/test/resources/projects/complex-executable/target/complex-executable"));
		verify(commandExecutor).execute(command);
	}

}
