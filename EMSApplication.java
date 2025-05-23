import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class EMSApplication {
    private static Scanner kul_scanner = new Scanner(System.in);
    private static EmployeeManagementSystem kul_ems;
    private static final String KUL_DATA_FILE = "employee_data.csv";

    public static void main(String[] args) {
        kul_ems = new EmployeeManagementSystem(KUL_DATA_FILE);
        File kul_file = new File(KUL_DATA_FILE);
        if (!kul_file.exists()) {
            System.out.println("No data file found. Generating sample data...");
            kul_ems.generateSampleData();
            kul_ems.saveEmployees();
        } else {
            kul_ems.loadEmployees();
        }

        boolean kul_exit = false;
        while (!kul_exit) {
            displayMainMenu();
            int kul_choice = getIntInput("Enter your choice: ");

            switch (kul_choice) {
                case 1:
                    kul_ems.loadEmployees();
                    break;
                case 2:
                    addEmployeeMenu();
                    break;
                case 3:
                    updateEmployeeMenu();
                    break;
                case 4:
                    deleteEmployeeMenu();
                    break;
                case 5:
                    queryEmployeeMenu();
                    break;
                case 6:
                    kul_ems.listAllEmployees();
                    break;
                case 7:
                    managePerformanceMenu();
                    break;
                case 8:
                    kul_ems.saveEmployees();
                    break;
                case 9:
                    kul_exit = true;
                    System.out.println("Exiting Employee Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        kul_scanner.close();
    }

    private static void displayMainMenu() {
        System.out.println("\nEmployee Management System");
        System.out.println("1. Load employee data from file");
        System.out.println("2. Add new employee");
        System.out.println("3. Update employee information");
        System.out.println("4. Delete employee");
        System.out.println("5. Query employee details");
        System.out.println("6. List all employees");
        System.out.println("7. Manage employee performance");
        System.out.println("8. Save employee data to file");
        System.out.println("9. Exit");
    }

    private static void addEmployeeMenu() {
        System.out.println("\nAdd New Employee");
        System.out.println("1. Regular Employee");
        System.out.println("2. Manager");
        System.out.println("3. Intern");
        System.out.println("4. Back to main menu");

        int kul_typeChoice = getIntInput("Enter employee type: ");
        if (kul_typeChoice == 4) return;

        String kul_id = getStringInput("Enter employee ID: ");
        String kul_name = getStringInput("Enter employee name: ");
        String kul_department = getStringInput("Enter department: ");
        double kul_baseSalary = getDoubleInput("Enter base salary: ");

        Employee kul_employee = null;
        switch (kul_typeChoice) {
            case 1:
                double kul_overtime = getDoubleInput("Enter overtime pay: ");
                kul_employee = new RegularEmployee(kul_id, kul_name, kul_department, kul_baseSalary, kul_overtime);
                break;
            case 2:
                double kul_bonus = getDoubleInput("Enter bonus: ");
                kul_employee = new Manager(kul_id, kul_name, kul_department, kul_baseSalary, kul_bonus);
                break;
            case 3:
                try {
                    String kul_endDateStr = getStringInput("Enter internship end date (yyyy-MM-dd): ");
                    SimpleDateFormat kul_sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date kul_endDate = kul_sdf.parse(kul_endDateStr);
                    kul_employee = new Intern(kul_id, kul_name, kul_department, kul_baseSalary, kul_endDate);
                } catch (Exception kul_e) {
                    System.out.println("Invalid date format. Employee not added.");
                    return;
                }
                break;
            default:
                System.out.println("Invalid choice. Employee not added.");
                return;
        }
        kul_ems.addEmployee(kul_employee);
    }

    private static void updateEmployeeMenu() {
        String kul_id = getStringInput("Enter employee ID to update: ");
        Employee kul_emp = kul_ems.findEmployeeById(kul_id);

        if (kul_emp == null) {
            System.out.println("Employee not found.");
            return;
        }

        System.out.println("Current employee details:");
        System.out.println(kul_emp);

        String kul_name = getStringInput("Enter new name (leave blank to keep current): ");
        String kul_department = getStringInput("Enter new department (leave blank to keep current): ");
        String kul_salaryStr = getStringInput("Enter new base salary (leave blank to keep current): ");

        if (!kul_name.isEmpty()) kul_emp.setName(kul_name);
        if (!kul_department.isEmpty()) kul_emp.setDepartment(kul_department);
        if (!kul_salaryStr.isEmpty()) {
            try {
                double kul_salary = Double.parseDouble(kul_salaryStr);
                if (kul_salary >= 0) kul_emp.setBaseSalary(kul_salary);
                else System.out.println("Salary cannot be negative. Salary not updated.");
            } catch (NumberFormatException kul_e) {
                System.out.println("Invalid salary format. Salary not updated.");
            }
        }
        System.out.println("Employee updated successfully.");
    }

    private static void deleteEmployeeMenu() {
        String kul_id = getStringInput("Enter employee ID to delete: ");
        kul_ems.removeEmployee(kul_id);
    }

    private static void queryEmployeeMenu() {
        System.out.println("\nQuery Employees");
        System.out.println("1. By ID");
        System.out.println("2. By Name");
        System.out.println("3. By Performance Rating");
        System.out.println("4. Back to main menu");

        int kul_choice = getIntInput("Enter your choice: ");
        if (kul_choice == 4) return;

        switch (kul_choice) {
            case 1:
                String kul_id = getStringInput("Enter employee ID: ");
                Employee kul_emp = kul_ems.findEmployeeById(kul_id);
                if (kul_emp != null) {
                    System.out.println("Employee found:");
                    System.out.println(kul_emp);
                } else {
                    System.out.println("Employee not found.");
                }
                break;
            case 2:
                String kul_name = getStringInput("Enter employee name: ");
                List<Employee> kul_byName = kul_ems.findEmployeesByName(kul_name);
                if (!kul_byName.isEmpty()) {
                    System.out.println("Employees found:");
                    for (Employee kul_e : kul_byName) {
                        System.out.println(kul_e);
                    }
                } else {
                    System.out.println("No employees found with that name.");
                }
                break;
            case 3:
                int kul_rating = getIntInput("Enter performance rating (1-5): ");
                if (kul_rating >= 1 && kul_rating <= 5) {
                    List<Employee> kul_byRating = kul_ems.findEmployeesByPerformance(kul_rating);
                    if (!kul_byRating.isEmpty()) {
                        System.out.println("Employees with rating " + kul_rating + ":");
                        for (Employee kul_e : kul_byRating) {
                            System.out.println(kul_e);
                        }
                    } else {
                        System.out.println("No employees found with that rating.");
                    }
                } else {
                    System.out.println("Invalid rating. Must be between 1 and 5.");
                }
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void managePerformanceMenu() {
        String kul_id = getStringInput("Enter employee ID: ");
        Employee kul_emp = kul_ems.findEmployeeById(kul_id);

        if (kul_emp == null) {
            System.out.println("Employee not found.");
            return;
        }

        System.out.println("\nManage Performance for: " + kul_emp.getName());
        System.out.println("1. Set performance rating");
        System.out.println("2. Issue warning");
        System.out.println("3. Issue appraisal");
        System.out.println("4. View current performance");
        System.out.println("5. Back to main menu");

        int kul_choice = getIntInput("Enter your choice: ");
        if (kul_choice == 5) return;

        switch (kul_choice) {
            case 1:
                int kul_rating = getIntInput("Enter new performance rating (1-5): ");
                kul_emp.setPerformanceRating(kul_rating);
                System.out.println("Performance rating updated.");
                break;
            case 2:
                String kul_warning = getStringInput("Enter warning message: ");
                kul_emp.issueWarning(kul_warning);
                break;
            case 3:
                String kul_appraisal = getStringInput("Enter appraisal message: ");
                kul_emp.issueAppraisal(kul_appraisal);
                break;
            case 4:
                System.out.println("Current performance rating: " + kul_emp.getPerformanceRating());
                if (kul_emp.getLastWarning() != null) {
                    System.out.println("Last warning: " + kul_emp.getLastWarning());
                }
                if (kul_emp.getLastAppraisal() != null) {
                    System.out.println("Last appraisal: " + kul_emp.getLastAppraisal());
                }
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(kul_scanner.nextLine());
            } catch (NumberFormatException kul_e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double kul_value = Double.parseDouble(kul_scanner.nextLine());
                if (kul_value >= 0) return kul_value;
                System.out.println("Value cannot be negative.");
            } catch (NumberFormatException kul_e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static String getStringInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String kul_input = kul_scanner.nextLine().trim();
            if (!kul_input.isEmpty()) return kul_input;
            System.out.println("Input cannot be empty.");
        }
    }
}