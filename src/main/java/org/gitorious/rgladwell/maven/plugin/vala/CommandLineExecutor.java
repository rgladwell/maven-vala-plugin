package org.gitorious.rgladwell.maven.plugin.vala;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;
import org.codehaus.plexus.util.cli.WriterStreamConsumer;

public class CommandLineExecutor implements CommandExecutor {

	private Log log;

    public void setLog( Log log ) {
        this.log = log;
    }

    public Log getLog() {
        if ( log == null )
        {
            log = new SystemStreamLog();
        }

        return log;
    }

    public void execute(Command command) throws ValaPluginException {
		CompileCommand compileCommand = (CompileCommand) command;
		List<String> arguments = new ArrayList<String>();		
		Commandline cmd = new Commandline();
		
		cmd.setExecutable(compileCommand.getCommandName());
		
		arguments.add("-o");
		arguments.add(compileCommand.getOutputFile().getAbsolutePath());

		if(!compileCommand.getPackages().isEmpty()) {
			arguments.add("--pkg");
		}

		for(String p : compileCommand.getPackages()) {
			arguments.add(p);
		}

		for(File source : compileCommand.getValaSources()) {
			arguments.add(source.getAbsolutePath());
		}
		
		cmd.addArguments(arguments.toArray(new String[arguments.size()]));

		StringWriter outputWriter = new StringWriter();
		StringWriter errorWriter = new StringWriter();
		StreamConsumer output = new WriterStreamConsumer(outputWriter);
		StreamConsumer error = new WriterStreamConsumer(errorWriter);

		try {
			getLog().debug("executing " + cmd.toString());
			int result = CommandLineUtils.executeCommandLine(cmd, output, error);
	        Process process = cmd.execute();
	        process.waitFor();
	        process.exitValue();
	        if(result != 0) {
	        	throw new ValaPluginException("error executing " + command + " : " + errorWriter.toString());
	        }
        } catch (CommandLineException e) {
	        throw new ValaPluginException("error executing " + command, e);
        } catch (InterruptedException e) {
	        throw new ValaPluginException("error executing " + command, e);
        }
	}

}
