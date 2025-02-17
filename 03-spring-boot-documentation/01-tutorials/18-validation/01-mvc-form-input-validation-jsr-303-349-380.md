# MVC form表单提交校验(使用 JSR303/349/380)

- 本案例使用 hibernate-validator 



```java
/**
 * description
 *
 * @author EricChen 2019/12/09 23:45
 */
@Controller
public class EmployeeController {
    private static List<Employee> employeeList = new ArrayList<>();

    @PostMapping("/")
    @ResponseBody
    public Object handlePostRequest(@Valid Employee employee, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            return allErrors;
        }
        employeeList.add(employee);
        return "success";
    }
}

```



```java
public class Employee {
    @NotNull
    @Size(min = 5, max = 50)
    private String name;
    @Pattern(regexp = "Admin|IT|Sales|Accounts")
    private String dept;
    @Past
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
```

![image-20191209235515709](assets/image-20191209235515709.png)