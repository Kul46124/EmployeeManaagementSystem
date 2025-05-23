import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Employee implements Serializable {
    private static final long kul_serialVersionUID = 1L;
    protected String kul_id;
    protected String kul_name;
    protected String kul_department;
    protected double kul_baseSalary;
    protected int kul_performanceRating;
    protected Date kul_hireDate;
    protected String kul_lastAppraisal;
    protected String kul_lastWarning;

    public Employee(String kul_id, String kul_name, String kul_department, double kul_baseSalary) {
        this.kul_id = kul_id;
        this.kul_name = kul_name;
        this.kul_department = kul_department;
        this.kul_baseSalary = kul_baseSalary;
        this.kul_performanceRating = 3; // Default rating
        this.kul_hireDate = new Date(); // Current date
    }

    public abstract double calculateSalary();

    public void setPerformanceRating(int kul_rating) {
        if (kul_rating >= 1 && kul_rating <= 5) {
            this.kul_performanceRating = kul_rating;
        } else {
            System.out.println("Invalid rating. Must be between 1 and 5.");
        }
    }

    public void issueWarning(String kul_warning) {
        this.kul_lastWarning = kul_warning;
        System.out.println("Warning issued to " + kul_name + ": " + kul_warning);
    }

    public void issueAppraisal(String kul_appraisal) {
        this.kul_lastAppraisal = kul_appraisal;
        System.out.println("Appraisal issued to " + kul_name + ": " + kul_appraisal);
    }

    // Getters
    public String getId() { return kul_id; }
    public String getName() { return kul_name; }
    public String getDepartment() { return kul_department; }
    public double getBaseSalary() { return kul_baseSalary; }
    public int getPerformanceRating() { return kul_performanceRating; }
    public Date getHireDate() { return kul_hireDate; }
    public String getLastAppraisal() { return kul_lastAppraisal; }
    public String getLastWarning() { return kul_lastWarning; }

    // Setters
    public void setName(String kul_name) { this.kul_name = kul_name; }
    public void setDepartment(String kul_department) { this.kul_department = kul_department; }
    public void setBaseSalary(double kul_baseSalary) { this.kul_baseSalary = kul_baseSalary; }
    public void setHireDate(Date kul_hireDate) { this.kul_hireDate = kul_hireDate; }

    @Override
    public String toString() {
        SimpleDateFormat kul_sdf = new SimpleDateFormat("yyyy-MM-dd");
        return String.format("ID: %s, Name: %s, Department: %s, Base Salary: %.2f, Performance: %d, Hire Date: %s",
                kul_id, kul_name, kul_department, kul_baseSalary, kul_performanceRating, kul_sdf.format(kul_hireDate));
    }
}