package org.gitorious.rgladwell.maven.plugin.vala;

public class ValaPluginException extends Exception {

	private static final long serialVersionUID = -5317466125676760976L;

	public ValaPluginException() {
	}

	public ValaPluginException(String message) {
		super(message);
	}

	public ValaPluginException(Throwable cause) {
		super(cause);
	}

	public ValaPluginException(String message, Throwable cause) {
		super(message, cause);
	}

}
