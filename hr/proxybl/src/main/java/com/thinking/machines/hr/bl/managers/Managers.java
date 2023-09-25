package com.thinking.machines.hr.bl.managers;
public class Managers
{
    private enum ManagerType{DESIGNATION,EMPLOYEE};
    public static ManagerType DESIGNATION=ManagerType.DESIGNATION;
    public static ManagerType EMPLOYEE=ManagerType.EMPLOYEE;

    public static String getManagerType(ManagerType managerType)
    {
        if(managerType==DESIGNATION) return "DesignationManager";
        if(managerType==EMPLOYEE) return "EmployeeManager";
        return "";
    }

    static public class Designation
    {
        private enum Action{ADD_DESGIGNATION,UPDATE_DESGIGNATION,REMOVE_DESGIGNATION,GET_DESGIGNATION_BY_CODE,GET_DESGIGNATION_BY_TITLE,GET_DESGIGNATION_COUNT,DESGIGNATION_CODE_EXISTS,DESIGNATION_TITLE_EXISTS,GET_DESIGNATIONS};

        public static Action ADD_DESGIGNATION=Action.ADD_DESGIGNATION;
        public static Action UPDATE_DESGIGNATION=Action.UPDATE_DESGIGNATION;
        public static Action REMOVE_DESGIGNATION=Action.REMOVE_DESGIGNATION;
        public static Action GET_DESGIGNATION_BY_CODE=Action.GET_DESGIGNATION_BY_CODE;
        public static Action GET_DESGIGNATION_BY_TITLE=Action.GET_DESGIGNATION_BY_TITLE;
        public static Action GET_DESGIGNATION_COUNT=Action.GET_DESGIGNATION_COUNT;
        public static Action DESGIGNATION_CODE_EXISTS=Action.DESGIGNATION_CODE_EXISTS;
        public static Action DESIGNATION_TITLE_EXISTS=Action.DESIGNATION_TITLE_EXISTS;
        public static Action GET_DESIGNATIONS=Action.GET_DESIGNATIONS;
    }

    public static String getAction(Designation.Action action)
    {
        if(action==Designation.ADD_DESGIGNATION) return "addDesignation";
        if(action==Designation.UPDATE_DESGIGNATION) return "updateDesignation";
        if(action==Designation.REMOVE_DESGIGNATION) return "removeDesignation";
        if(action==Designation.GET_DESGIGNATION_BY_CODE) return "getDesignationByCode";
        if(action==Designation.GET_DESGIGNATION_BY_TITLE) return "getDesignationByTitle";
        if(action==Designation.GET_DESGIGNATION_COUNT) return "getDesignationCount";
        if(action==Designation.DESGIGNATION_CODE_EXISTS) return "designationCodeExists";
        if(action==Designation.DESIGNATION_TITLE_EXISTS) return "designationTitleExists";
        if(action==Designation.GET_DESIGNATIONS) return "getDesignations";
        return "";
    }
}