import java.text.SimpleDateFormat;
import java.util.Date;

public class Intern extends Employee {
    private static final long kul_serialVersionUID = 1L;
    private Date kul_endDate;

    public Intern(String kul_id, String kul_name, String kul_department, double kul_baseSalary, Date kul_endDate) {
        super(kul_id, kul_name, kul_department, kul_baseSalary);
        this.kul_endDate = kul_endDate;
    }

    @Override
    public double calculateSalary() {
        return kul_baseSalary;
    }

    public Date getEndDate() { return kul_endDate; }
    public void setEndDate(Date kul_endDate) { this.kul_endDate = kul_endDate; }

    @Override
    public String toString() {
        SimpleDateFormat kul_sdf = new SimpleDateFormat("yyyy-MM-dd");
        return super.toString() + ", Type: Intern, Salary: " + calculateSalary() + ", Internship End Date: " + kul_sdf.format(kul_endDate);
    }
}