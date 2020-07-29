package seu.seniorproject.com.seubookit.Utilities;

public class DoctorVH {
    public String doctorName,doctorBranch,doctorDepartment,doctorFile;

    public DoctorVH() {
    }

    public DoctorVH(String doctorName, String doctorBranch, String doctorDepartment) {
        this.doctorName = doctorName;
        this.doctorBranch = doctorBranch;
        this.doctorDepartment = doctorDepartment;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorBranch() {
        return doctorBranch;
    }

    public void setDoctorBranch(String doctorBranch) {
        this.doctorBranch = doctorBranch;
    }

    public String getDoctorDepartment() {
        return doctorDepartment;
    }

    public void setDoctorDepartment(String doctorDepartment) {
        this.doctorDepartment = doctorDepartment;
    }

    public String getDoctorFile() {
        return doctorFile;
    }

    public void setDoctorFile(String doctorFile) {
        this.doctorFile = doctorFile;
    }
}
