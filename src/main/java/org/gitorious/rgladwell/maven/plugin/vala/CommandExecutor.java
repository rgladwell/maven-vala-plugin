package org.gitorious.rgladwell.maven.plugin.vala;

import org.gitorious.rgladwell.maven.plugin.vala.model.Command;

public interface CommandExecutor {

	public void execute(Command command) throws ValaPluginException;

}
