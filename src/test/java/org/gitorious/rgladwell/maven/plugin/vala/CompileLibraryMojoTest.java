package org.gitorious.rgladwell.maven.plugin.vala;

import static org.mockito.Mockito.verify;

import java.io.File;
import java.util.HashSet;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugin.testing.stubs.ArtifactStub;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;
import org.apache.maven.project.MavenProjectHelper;
import org.gitorious.rgladwell.maven.plugin.vala.model.CompileCommand;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CompileLibraryMojoTest extends AbstractMojoTestCase {

	CompileLibraryMojo mojo;
	@Mock CommandExecutor commandExecutor;
	@Mock MavenProjectHelper projectHelper;

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

    @Test
	@SuppressWarnings("unchecked")
	public void testExecuteForSimpleLibrary() throws Throwable {
		File pom = new File(getBasedir(), "src/test/resources/projects/simple-library/pom.xml");
		CompileLibraryMojo mojo = (CompileLibraryMojo) lookupMojo ( "valac-library", pom );
		setVariableValueToObject(mojo, "commandExecutor", commandExecutor);
		setVariableValueToObject(mojo, "compilerName", "valac");
		MavenProjectStub project = new MavenProjectStub();
		project.setPackaging("vala-library");
		ArtifactStub artifact = new ArtifactStub();
		artifact.setArtifactId("glib");
		artifact.setVersion("2.0");
		artifact.setType("package");
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
		command.setIntrospectionMetadata(true);
		verify(commandExecutor).execute(command);
		verify(projectHelper).attachArtifact(project, "so", new File(command.getOutputFolder(), "simple-library.so"));
		verify(projectHelper).attachArtifact(project, "vapi", new File(command.getOutputFolder(), "simple-library.vapi"));
		verify(projectHelper).attachArtifact(project, "h", new File(command.getOutputFolder(), "simple-library.h"));
	}

    @Test
	@SuppressWarnings("unchecked")
	public void testExecuteForSimpleLibraryWithIntrospection() throws Throwable {
		File pom = new File(getBasedir(), "src/test/resources/projects/simple-library/pom.xml");
		CompileLibraryMojo mojo = (CompileLibraryMojo) lookupMojo ( "valac-library", pom );
		setVariableValueToObject(mojo, "commandExecutor", commandExecutor);
		setVariableValueToObject(mojo, "compilerName", "valac");
		MavenProjectStub project = new MavenProjectStub();
		project.setPackaging("vala-library");
		ArtifactStub artifact = new ArtifactStub();
		artifact.setArtifactId("glib");
		artifact.setVersion("2.0");
		artifact.setType("package");
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
		command.setIntrospectionMetadata(true);
		verify(commandExecutor).execute(command);
		verify(projectHelper).attachArtifact(project, "gir", new File(command.getOutputFolder(), "simple-library.gir"));
	}

}
