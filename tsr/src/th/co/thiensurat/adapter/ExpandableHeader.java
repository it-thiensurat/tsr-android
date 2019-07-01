package th.co.thiensurat.adapter;

import th.co.thiensurat.data.info.EmployeeDetailInfo;

public class ExpandableHeader {
    private String text;
    private boolean notCollapse;
    private boolean isEmpLayout;
    private EmployeeDetailInfo emp;
    private int resource;

    public ExpandableHeader(String text) {
        this.text = text;
    }

    public ExpandableHeader(String text, boolean notCollapse, boolean isEmpLayout, EmployeeDetailInfo emp, int resource) {
        this.text = text;
        this.notCollapse = notCollapse;
        this.isEmpLayout = isEmpLayout;
        this.emp = emp;
        this.resource = resource;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isNotCollapse() {
        return notCollapse;
    }

    public void setNotCollapse(boolean notCollapse) {
        this.notCollapse = notCollapse;
    }

    public boolean isEmpLayout() {
        return isEmpLayout;
    }

    public void setIsEmpLayout(boolean isEmpLayout) {
        this.isEmpLayout = isEmpLayout;
    }

    public EmployeeDetailInfo getEmp() {
        return emp;
    }

    public void setEmp(EmployeeDetailInfo emp) {
        this.emp = emp;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }
}
