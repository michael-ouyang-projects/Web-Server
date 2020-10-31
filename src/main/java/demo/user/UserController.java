package demo.user;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import demo.aop.ControllerAop;
import demo.aop.PostAop;
import tw.framework.michaelcore.aop.annotation.AopHere;
import tw.framework.michaelcore.ioc.annotation.Autowired;
import tw.framework.michaelcore.mvc.Model;
import tw.framework.michaelcore.mvc.annotation.Controller;
import tw.framework.michaelcore.mvc.annotation.Get;
import tw.framework.michaelcore.mvc.annotation.Post;
import tw.framework.michaelcore.mvc.annotation.RequestParam;

@Controller
@AopHere(ControllerAop.class)
public class UserController {

    @Autowired
    private UserService userService;

    @Get("/")
    public Model home() {
        return new Model("index.html");
    }

    @Get("/user/add")
    public Model addUserByGet(@RequestParam("name") String name, @RequestParam("age") int age) throws InterruptedException, ExecutionException {
        Future<String> result = userService.addUserAsync(new User(name, age));
        Model model = new Model("success.html");
        model.add("name", name);
        model.add("age", age);
        System.out.println(result.get());
        return model;
    }

    @Post("/user/add")
    @AopHere(PostAop.class)
    public Model addUserByPost(@RequestParam("name") String name, @RequestParam("age") int age) {
        userService.addUser(new User(name, age));
        Model model = new Model("success.html");
        model.add("name", name);
        model.add("age", age);
        return model;
    }

}