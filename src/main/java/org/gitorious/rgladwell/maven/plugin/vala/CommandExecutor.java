package org.gitorious.rgladwell.maven.plugin.vala;

public interface CommandExecutor {

	public void execute(Command command) throws ValaPluginException;

}
