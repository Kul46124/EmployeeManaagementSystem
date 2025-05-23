public class RegularEmployee extends Employee {
    private static final long kul_serialVersionUID = 1L;
    private double kul_overtimePay;

    public RegularEmployee(String kul_id, String kul_name, String kul_department, double kul_baseSalary, double kul_overtimePay) {
        super(kul_id, kul_name, kul_department, kul_baseSalary);
        this.kul_overtimePay = kul_overtimePay;
    }

    public void setOvertimePay(double kul_overtimePay) {
        this.kul_overtimePay = kul_overtimePay;
    }

    @Override
    public double calculateSalary() {
        return kul_baseSalary + kul_overtimePay;
    }

    @Override
    public String toString() {
        return super.toString() + ", Type: Regular, Salary: " + calculateSalary();
    }
}