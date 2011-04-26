class Demo.HelloWorld : GLib.Object {

    public static int main(string[] args) {

		MyLib test = new MyLib();
        test.test();

        return 0;
    }
}
