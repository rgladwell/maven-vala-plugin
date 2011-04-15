using Demo;

class Demo.HelloWorld : GLib.Object {

    public static int main(string[] args) {
		Test test = new Test();
		test.run(args[1]);

        return 0;
    }
}
