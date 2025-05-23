import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class EmployeeManagementSystem {
    private List<Employee> kul_employees;
    private String kul_dataFile;

    public EmployeeManagementSystem(String kul_dataFile) {
        this.kul_dataFile = kul_dataFile;
        this.kul_employees = new ArrayList<>();
    }

    public void loadEmployees() {
        kul_employees.clear();
        File kul_file = new File(kul_dataFile);
        if (!kul_file.exists()) {
            System.out.println("Data file not found. Generating sample data...");
            generateSampleData();
            saveEmployees();
            return;
        }

        try (BufferedReader kul_reader = new BufferedReader(new FileReader(kul_dataFile))) {
            String kul_line = kul_reader.readLine(); // Skip header
            while ((kul_line = kul_reader.readLine()) != null) {
                String[] kul_parts = kul_line.split(",");
                if (kul_parts.length < 7) continue; // Skip malformed lines

                String kul_type = kul_parts[0];
                String kul_id = kul_parts[1];
                String kul_name = kul_parts[2];
                String kul_department = kul_parts[3];
                double kul_baseSalary = Double.parseDouble(kul_parts[4]);
                int kul_performanceRating = Integer.parseInt(kul_parts[5]);
                SimpleDateFormat kul_sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date kul_hireDate = kul_sdf.parse(kul_parts[6]);
                String kul_extraField1 = kul_parts.length > 7 ? kul_parts[7] : "";
                String kul_extraField2 = kul_parts.length > 8 ? kul_parts[8] : "";

                Employee kul_employee = null;
                switch (kul_type) {
                    case "Regular":
                        double kul_overtimePay = kul_extraField1.isEmpty() ? 0 : Double.parseDouble(kul_extraField1);
                        kul_employee = new RegularEmployee(kul_id, kul_name, kul_department, kul_baseSalary, kul_overtimePay);
                        break;
                    case "Manager":
                        double kul_bonus = kul_extraField1.isEmpty() ? 0 : Double.parseDouble(kul_extraField1);
                        kul_employee = new Manager(kul_id, kul_name, kul_department, kul_baseSalary, kul_bonus);
                        break;
                    case "Intern":
                        Date kul_endDate = kul_extraField1.isEmpty() ? new Date() : kul_sdf.parse(kul_extraField1);
                        kul_employee = new Intern(kul_id, kul_name, kul_department, kul_baseSalary, kul_endDate);
                        break;
                    default:
                        continue;
                }

                kul_employee.setPerformanceRating(kul_performanceRating);
                kul_employee.setHireDate(kul_hireDate);
                if (!kul_extraField2.isEmpty()) {
                    if (kul_extraField2.startsWith("Warning:")) {
                        kul_employee.issueWarning(kul_extraField2.substring(8));
                    } else if (kul_extraField2.startsWith("Appraisal:")) {
                        kul_employee.issueAppraisal(kul_extraField2.substring(10));
                    }
                }
                kul_employees.add(kul_employee);
            }
            System.out.println("Employee data loaded successfully. Total employees: " + kul_employees.size());
        } catch (Exception kul_e) {
            System.out.println("Error loading employee data: " + kul_e.getMessage());
        }
    }

    public void saveEmployees() {
        try (BufferedWriter kul_writer = new BufferedWriter(new FileWriter(kul_dataFile))) {
            // Write header
            kul_writer.write("type,id,name,department,baseSalary,performanceRating,hireDate,extraField1,extraField2\n");
            SimpleDateFormat kul_sdf = new SimpleDateFormat("yyyy-MM-dd");

            for (Employee kul_emp : kul_employees) {
                StringBuilder kul_line = new StringBuilder();
                if (kul_emp instanceof RegularEmployee) {
                    RegularEmployee kul_re = (RegularEmployee) kul_emp;
                    kul_line.append("Regular,").append(kul_emp.getId()).append(",").append(kul_emp.getName()).append(",")
                        .append(kul_emp.getDepartment()).append(",").append(kul_emp.getBaseSalary()).append(",")
                        .append(kul_emp.getPerformanceRating()).append(",").append(kul_sdf.format(kul_emp.getHireDate())).append(",")
                        .append(kul_re.calculateSalary() - kul_re.getBaseSalary()).append(",");
                } else if (kul_emp instanceof Manager) {
                    Manager kul_m = (Manager) kul_emp;
                    kul_line.append("Manager,").append(kul_emp.getId()).append(",").append(kul_emp.getName()).append(",")
                        .append(kul_emp.getDepartment()).append(",").append(kul_emp.getBaseSalary()).append(",")
                        .append(kul_emp.getPerformanceRating()).append(",").append(kul_sdf.format(kul_emp.getHireDate())).append(",")
                        .append(kul_m.calculateSalary() - kul_m.getBaseSalary()).append(",");
                } else if (kul_emp instanceof Intern) {
                    Intern kul_i = (Intern) kul_emp;
                    kul_line.append("Intern,").append(kul_emp.getId()).append(",").append(kul_emp.getName()).append(",")
                        .append(kul_emp.getDepartment()).append(",").append(kul_emp.getBaseSalary()).append(",")
                        .append(kul_emp.getPerformanceRating()).append(",").append(kul_sdf.format(kul_emp.getHireDate())).append(",")
                        .append(kul_sdf.format(kul_i.getEndDate())).append(",");
                }

                if (kul_emp.getLastWarning() != null) {
                    kul_line.append("Warning:").append(kul_emp.getLastWarning());
                } else if (kul_emp.getLastAppraisal() != null) {
                    kul_line.append("Appraisal:").append(kul_emp.getLastAppraisal());
                }
                kul_writer.write(kul_line.toString() + "\n");
            }
            System.out.println("Employee data saved successfully.");
        } catch (IOException kul_e) {
            System.out.println("Error saving employee data: " + kul_e.getMessage());
        }
    }

    public void addEmployee(Employee kul_employee) {
        if (findEmployeeById(kul_employee.getId()) != null) {
            System.out.println("Employee with ID " + kul_employee.getId() + " already exists.");
            return;
        }
        kul_employees.add(kul_employee);
        System.out.println("Employee added successfully: " + kul_employee.getName());
    }

    public void removeEmployee(String kul_id) {
        Iterator<Employee> kul_iterator = kul_employees.iterator();
        while (kul_iterator.hasNext()) {
            Employee kul_emp = kul_iterator.next();
            if (kul_emp.getId().equals(kul_id)) {
                kul_iterator.remove();
                System.out.println("Employee removed successfully: " + kul_emp.getName());
                return;
            }
        }
        System.out.println("Employee with ID " + kul_id + " not found.");
    }

    public Employee findEmployeeById(String kul_id) {
        for (Employee kul_emp : kul_employees) {
            if (kul_emp.getId().equals(kul_id)) {
                return kul_emp;
            }
        }
        return null;
    }

    public List<Employee> findEmployeesByName(String kul_name) {
        List<Employee> kul_result = new ArrayList<>();
        for (Employee kul_emp : kul_employees) {
            if (kul_emp.getName().equalsIgnoreCase(kul_name)) {
                kul_result.add(kul_emp);
            }
        }
        return kul_result;
    }

    public List<Employee> findEmployeesByPerformance(int kul_rating) {
        List<Employee> kul_result = new ArrayList<>();
        for (Employee kul_emp : kul_employees) {
            if (kul_emp.getPerformanceRating() == kul_rating) {
                kul_result.add(kul_emp);
            }
        }
        return kul_result;
    }

    public void listAllEmployees() {
        if (kul_employees.isEmpty()) {
            System.out.println("No employees in the system.");
            return;
        }
        System.out.println("\nList of all employees:");
        for (Employee kul_emp : kul_employees) {
            System.out.println(kul_emp.toString());
        }
    }

    public void updateEmployee(String kul_id, String kul_name, String kul_department, double kul_baseSalary) {
        Employee kul_emp = findEmployeeById(kul_id);
        if (kul_emp != null) {
            kul_emp.setName(kul_name);
            kul_emp.setDepartment(kul_department);
            kul_emp.setBaseSalary(kul_baseSalary);
            System.out.println("Employee updated successfully: " + kul_emp.getName());
        } else {
            System.out.println("Employee with ID " + kul_id + " not found.");
        }
    }

    public void generateSampleData() {
        kul_employees.clear();
        try {
            SimpleDateFormat kul_sdf = new SimpleDateFormat("yyyy-MM-dd");
            addEmployee(new RegularEmployee("E001", "Basanta Bhusan Khadka", "IT", 60000, 5000));
            addEmployee(new Manager("E002", "Kul Chandra Kamal", "HR", 70000, 10000));
            addEmployee(new Intern("E003", "Roshan Gurung", "Marketing", 20000, kul_sdf.parse("2025-12-31")));
            addEmployee(new RegularEmployee("E004", "Sandesh Bhatta", "Finance", 55000, 3000));
            addEmployee(new Manager("E005", "Gyani Bohara", "Operations", 75000, 12000));
            addEmployee(new RegularEmployee("E006", "Ramu Kaka", "Sales", 60000, 4000));
            addEmployee(new Manager("E007", "Hari Bahadur", "Engineering", 80000, 15000));
            addEmployee(new Intern("E008", "Madan Krishna", "Design", 25000, kul_sdf.parse("2025-12-31")));
            addEmployee(new RegularEmployee("E009", "Bramanandam Brown", "Support", 52000, 2000));
            addEmployee(new Manager("E010", "Kevin Hart", "Product", 78000, 11000));
            System.out.println("Sample data generated with " + kul_employees.size() + " employees.");
        } catch (Exception kul_e) {
            System.out.println("Error creating sample data: " + kul_e.getMessage());
        }
    }
}