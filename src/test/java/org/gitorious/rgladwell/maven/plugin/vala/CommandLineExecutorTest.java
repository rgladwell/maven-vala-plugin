package org.gitorious.rgladwell.maven.plugin.vala;

import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;
import org.codehaus.plexus.util.cli.WriterStreamConsumer;
import org.gitorious.rgladwell.maven.plugin.vala.model.CompileCommand;
import org.gitorious.rgladwell.maven.plugin.vala.model.Library;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CommandLineExecutorTest extends AbstractMojoTestCase {

	CommandLineExecutor executor;
	private File outputDirectory;

	@Before
	public void setUp() {
		executor = new CommandLineExecutor();

		outputDirectory = new File(getBasedir(), "target/vala-maven-plugin/unit-tests/tmp");
   		outputDirectory.mkdirs();
	}

    @After
    public void tearDown() throws Exception {
    	outputDirectory.delete();
    }

	@Test
	public void testExecuteCompileForSimpleExecutableProject() throws Exception {
		CompileCommand command = new CompileCommand();
		command.setCommandName("valac");
		command.getValaSources().add(new File(getBasedir(), "src/test/resources/projects/simple-executable/main.vala"));
		command.getPackages().add("glib-2.0");
		File simpleExecutable = new File(outputDirectory, "simple-executable");
		command.setOutputFolder(outputDirectory);
		command.setBuildName("simple-executable");

		executor.execute(command);

		assertTrue("simple executable not built correctly", simpleExecutable.exists());
		Commandline cmd = new Commandline();
		cmd.setExecutable(simpleExecutable.getAbsolutePath());
		StringWriter outputWriter = new StringWriter();
		StringWriter errorWriter = new StringWriter();
		StreamConsumer output = new WriterStreamConsumer(outputWriter);
		StreamConsumer error = new WriterStreamConsumer(errorWriter);
		assertEquals("simple executable not built correctly", 0, CommandLineUtils.executeCommandLine(cmd, output, error));
		assertTrue(outputWriter.toString().contains("TEST-simple-executable"));
	}

	@Test
	public void testExecuteCompileForComplexExecutableProject() throws Exception {
		CompileCommand command = new CompileCommand();
		command.setCommandName("valac");
		command.getValaSources().add(new File(getBasedir(), "src/test/resources/projects/complex-executable/main.vala"));
		command.getValaSources().add(new File(getBasedir(), "src/test/resources/projects/complex-executable/src/Test.vala"));
		command.getPackages().add("gio-2.0");
		File complexExecutable = new File(outputDirectory, "complex-executable");
		command.setOutputFolder(outputDirectory);
		command.setBuildName("complex-executable");

		executor.execute(command);

		assertTrue("complex executable not built correctly", complexExecutable.exists());
		File testOutput = new File(outputDirectory, "test.txt");
		Commandline cmd = new Commandline();
		cmd.setExecutable(complexExecutable.getAbsolutePath());
		cmd.addArguments(new String[] { testOutput.getAbsolutePath() });
		assertEquals("complex executable not built correctly", 0, CommandLineUtils.executeCommandLine(cmd, null, null));
		assertTrue("complex executable did not execute correctly", testOutput.exists());
		assertTrue("complex executable did not execute correctly", IOUtil.toString(new FileReader(testOutput)).contains("TEST-complex-executable"));
	}

	@Test
	public void testExecuteCompileForComplexExecutableProjectWithMultiplePackageDependencies() throws Exception {
		CompileCommand command = new CompileCommand();
		command.setCommandName("valac");
		command.getValaSources().add(new File(getBasedir(), "src/test/resources/projects/complex-executable/main.vala"));
		command.getValaSources().add(new File(getBasedir(), "src/test/resources/projects/complex-executable/src/Test.vala"));
		command.getPackages().add("glib-2.0");
		command.getPackages().add("gio-2.0");
		File complexExecutable = new File(outputDirectory, "complex-executable");
		command.setOutputFolder(outputDirectory);
		command.setBuildName("complex-executable");

		executor.execute(command);

		assertTrue("complex executable not built correctly", complexExecutable.exists());
	}

	@Test
	public void testExecuteCompileForSimpleLibraryProject() throws Exception {
		CompileCommand command = new CompileCommand();
		command.setCommandName("valac");
		command.getValaSources().add(new File(getBasedir(), "src/test/resources/projects/simple-library/main.vala"));
		command.getPackages().add("glib-2.0");
		command.setLibrary(true);
		File simpleLibrary = new File(outputDirectory, "simple-library.so");
		command.setOutputFolder(outputDirectory);
		command.setBuildName("simple-library");

		executor.execute(command);

		assertTrue("simple executable not built correctly", simpleLibrary.exists());
		File header = new File(outputDirectory, "simple-library.h");
		assertTrue("simple executable header not built correctly", header.exists());
		File vapi = new File(outputDirectory, "simple-library.vapi");
		assertTrue("simple executable vapi not built correctly", vapi.exists());
	}

	@Test
	public void testExecuteCompileForDebugMode() throws Exception {
		CompileCommand command = new CompileCommand();
		command.setCommandName("valac");
		command.getValaSources().add(new File(getBasedir(), "src/test/resources/projects/simple-executable/main.vala"));
		command.getPackages().add("glib-2.0");
		File simpleExecutable = new File(outputDirectory, "simple-executable");
		command.setOutputFolder(outputDirectory);
		command.setBuildName("simple-executable");
		command.setDebug(true);

		executor.execute(command);

		assertTrue("simple executable not built correctly", simpleExecutable.exists());
		Commandline cmd = new Commandline();
		cmd.setExecutable(simpleExecutable.getAbsolutePath());
		StringWriter outputWriter = new StringWriter();
		StringWriter errorWriter = new StringWriter();
		StreamConsumer output = new WriterStreamConsumer(outputWriter);
		StreamConsumer error = new WriterStreamConsumer(errorWriter);
		assertEquals("simple executable not built correctly", 0, CommandLineUtils.executeCommandLine(cmd, output, error));
		assertTrue(outputWriter.toString().contains("TEST-simple-executable"));
	}

	@Test
	public void testExecuteCompileWithLibraryDependency() throws Exception {
		CompileCommand command = new CompileCommand();
		command.setCommandName("valac");
		command.getValaSources().add(new File(getBasedir(), "src/test/resources/projects/simple-library/main.vala"));
		command.getPackages().add("glib-2.0");
		command.setLibrary(true);
		File simpleLibrary = new File(outputDirectory, "simple-library.so");
		command.setOutputFolder(outputDirectory);
		command.setBuildName("simple-library");

		executor.execute(command);
		
		command = new CompileCommand();
		command.setCommandName("valac");
		command.getValaSources().add(new File(getBasedir(), "src/test/resources/projects/executable-with-library/main.vala"));
		command.getPackages().add("glib-2.0");
		File simpleExecutable = new File(outputDirectory, "executable-with-library");
		command.setOutputFolder(outputDirectory);
		command.setBuildName("executable-with-library");
		Library library = new Library();
		library.setBinary(simpleLibrary);
		library.setVapi(new File(outputDirectory, "simple-library.vapi"));
		command.getLibraries().add(library);
		command.setDebug(true);

		executor.execute(command);

		assertTrue("simple executable not built correctly", simpleExecutable.exists());
		Commandline cmd = new Commandline();
		cmd.setExecutable(simpleExecutable.getAbsolutePath());
		StringWriter outputWriter = new StringWriter();
		StringWriter errorWriter = new StringWriter();
		StreamConsumer output = new WriterStreamConsumer(outputWriter);
		StreamConsumer error = new WriterStreamConsumer(errorWriter);
		assertEquals("simple executable not built correctly", 0, CommandLineUtils.executeCommandLine(cmd, output, error));
		assertTrue(outputWriter.toString().contains("TEST-simple-library"));
	}

	@Test
	public void testExecuteCompileForLibraryWithIntrospection() throws Exception {
		CompileCommand command = new CompileCommand();
		command.setCommandName("valac");
		command.getValaSources().add(new File(getBasedir(), "src/test/resources/projects/simple-library/main.vala"));
		command.getPackages().add("glib-2.0");
		command.setLibrary(true);
		command.setIntrospectionMetadata(true);
		command.setOutputFolder(outputDirectory);
		command.setBuildName("simple-library-1.0-SNAPSHOT");

		executor.execute(command);

		File gir = new File(outputDirectory, "simple-library-1.0-SNAPSHOT.gir");
		assertTrue("simple executable vapi not built correctly", gir.exists());
	}

}
