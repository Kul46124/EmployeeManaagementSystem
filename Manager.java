public class Manager extends Employee {
    private static final long kul_serialVersionUID = 1L;
    private double kul_bonus;

    public Manager(String kul_id, String kul_name, String kul_department, double kul_baseSalary, double kul_bonus) {
        super(kul_id, kul_name, kul_department, kul_baseSalary);
        this.kul_bonus = kul_bonus;
    }

    public void setBonus(double kul_bonus) {
        this.kul_bonus = kul_bonus;
    }

    @Override
    public double calculateSalary() {
        return kul_baseSalary + kul_bonus;
    }

    @Override
    public String toString() {
        return super.toString() + ", Type: Manager, Salary: " + calculateSalary();
    }
}