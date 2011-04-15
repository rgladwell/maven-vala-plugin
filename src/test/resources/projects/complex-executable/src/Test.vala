using GLib;

namespace Demo {

	class Test : GLib.Object {
	
		public void run(string file) throws IOError, Error {
			File f = File.new_for_path(file);
			FileOutputStream output = f.create(FileCreateFlags.REPLACE_DESTINATION, null);
			string test = "TEST-complex-executable\n";
	        output.write(test.data, test.size());
		}
	 
	}

}
