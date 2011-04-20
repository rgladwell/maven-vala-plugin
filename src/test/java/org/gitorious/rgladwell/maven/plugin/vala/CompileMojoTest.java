package org.gitorious.rgladwell.maven.plugin.vala;

import static org.mockito.Mockito.*;

import java.io.File;
import java.util.HashSet;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugin.testing.stubs.ArtifactStub;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;
import org.apache.maven.project.MavenProjectHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CompileMojoTest extends AbstractMojoTestCase {

	CompileMojo mojo;
	@Mock CommandExecutor commandExecutor;
	@Mock MavenProjectHelper projectHelper;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		mojo = new CompileMojo();
	}

    @Test
	@SuppressWarnings("unchecked")
	public void testExecuteForSimpleExecutable() throws Throwable {
		File pom = new File(getBasedir(), "src/test/resources/projects/simple-executable/pom.xml");
		CompileMojo mojo = (CompileMojo) lookupMojo ( "valac", pom );
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
	@SuppressWarnings("unchecked")
	public void testExecuteForComplexExecutable() throws Throwable {
		File pom = new File(getBasedir(), "src/test/resources/projects/complex-executable/pom.xml");
		CompileMojo mojo = (CompileMojo) lookupMojo ( "valac", pom );
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
		setVariableValueToObject(mojo, "projectHelper", projectHelper);

		mojo.execute();

		CompileCommand command = new CompileCommand();
		command.setCommandName("valac");
		command.setBuildName("complex-executable");
		command.getValaSources().add(new File(getBasedir(), "src/test/resources/projects/complex-executable/src/Test.vala"));
		command.getValaSources().add(new File(getBasedir(), "src/test/resources/projects/complex-executable/main.vala"));
		command.getPackages().add("glib-2.0");
		command.getPackages().add("gio-2.0");
		command.setOutputFolder(new File(getBasedir(), "src/test/resources/projects/complex-executable/target"));
		verify(commandExecutor).execute(command);
		verify(projectHelper).attachArtifact(project, "exe", new File(command.getOutputFolder(), "complex-executable"));
	}

    @Test
	@SuppressWarnings("unchecked")
	public void testExecuteForSimpleLibrary() throws Throwable {
		File pom = new File(getBasedir(), "src/test/resources/projects/simple-library/pom.xml");
		CompileMojo mojo = (CompileMojo) lookupMojo ( "valac", pom );
		setVariableValueToObject(mojo, "commandExecutor", commandExecutor);
		setVariableValueToObject(mojo, "compilerName", "valac");
		MavenProjectStub project = new MavenProjectStub();
		project.setPackaging("vala-library");
		Artifact artifact = new ArtifactStub();
		artifact.setArtifactId("glib");
		artifact.setVersion("2.0");
		project.setDependencyArtifacts(new HashSet<Artifact>());
		project.getDependencyArtifacts().add(artifact);
		setVariableValueToObject(mojo, "project", project);
		setVariableValueToObject(mojo, "outputExecutableName", "simple-library");
		setVariableValueToObject(mojo, "projectHelper", projectHelper);

		mojo.execute();

		CompileCommand command = new CompileCommand();
		command.setCommandName("valac");
		command.setBuildName("simple-library");
		command.getValaSources().add(new File(getBasedir(), "src/test/resources/projects/simple-library/main.vala"));
		command.getPackages().add("glib-2.0");
		command.setOutputFolder(new File(getBasedir(), "src/test/resources/projects/simple-library/target"));
		command.setLibrary(true);
		verify(commandExecutor).execute(command);
		verify(projectHelper).attachArtifact(project, "so", new File(command.getOutputFolder(), "simple-library.so"));
		verify(projectHelper).attachArtifact(project, "vapi", new File(command.getOutputFolder(), "simple-library.vapi"));
	}
}
