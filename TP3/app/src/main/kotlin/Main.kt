import com.example.app.MyClass
import com.example.app.MyClassWrapper


fun main() {
    val myClass = MyClass()
    val wrappedMyClass = MyClassWrapper(myClass) // Use the wrapper class

    wrappedMyClass.sayHello()
    wrappedMyClass.compute()
}