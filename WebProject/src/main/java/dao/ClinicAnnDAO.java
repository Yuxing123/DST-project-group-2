package dao;

import DBmtd.DBmethods;
import bean.ClinicAnnBean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClinicAnnDAO {

    public List<ClinicAnnBean> findAll(HashMap<String, ArrayList<String>> target){
        List<ClinicAnnBean> clinicAnnBeans = new ArrayList<>();
        ArrayList<String> filter_drug = target.get("drug");
        ArrayList<String> filter_disease = target.get("disease");
        if (filter_disease.isEmpty() & filter_drug.isEmpty()){
            DBmethods.execSQL(connection -> {
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, location, gene, evidencelevel, clinical_annotation_types, annotation_text, related_chemicals, related_diseases, biogeographical_groups, chromosome FROM clinic_meta;");
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        String id = resultSet.getString("id");
                        String location = resultSet.getString("location");
                        String gene = resultSet.getString("gene");
                        String evidencelevel = resultSet.getString("evidencelevel");
                        String types = resultSet.getString("clinical_annotation_types");
                        String annotation_text = resultSet.getString("annotation_text");
                        String related_chemicals = resultSet.getString("related_chemicals").toLowerCase();
                        String related_diseases = resultSet.getString("related_diseases").toLowerCase();
                        String biogeographical_groups = resultSet.getString("biogeographical_groups");
                        String chromosome = resultSet.getString("chromosome");
                        ClinicAnnBean clinicAnnBean = new ClinicAnnBean(id,location,gene,evidencelevel,types,annotation_text,related_chemicals,related_diseases,biogeographical_groups,chromosome);
                        clinicAnnBeans.add(clinicAnnBean);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } else {
            DBmethods.execSQL(connection -> {
                try {
                    boolean unmet_disease = true;
                    boolean unmet_drug = true;
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, location, gene, evidencelevel, clinical_annotation_types, annotation_text, related_chemicals, related_diseases, biogeographical_groups, chromosome FROM clinic_meta;");
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        String related_chemicals = resultSet.getString("related_chemicals").toLowerCase();
                        String related_diseases = resultSet.getString("related_diseases").toLowerCase();
                        for (String drug:filter_drug) {
                            if (related_chemicals.contains(drug)) {
                                unmet_drug = false;
                                break;
                            }
                        }
                        for (String disease:filter_disease){
                            if (related_diseases.contains(disease)) {
                                unmet_disease = false;
                                break;
                            }
                        }
                        if (unmet_disease | unmet_drug){
                            unmet_disease = unmet_drug = true;
                            continue;
                        }
                        String id = resultSet.getString("id");
                        String location = resultSet.getString("location");
                        String gene = resultSet.getString("gene");
                        String evidencelevel = resultSet.getString("evidencelevel");
                        String types = resultSet.getString("clinical_annotation_types");
                        String annotation_text = resultSet.getString("annotation_text");
                        String biogeographical_groups = resultSet.getString("biogeographical_groups");
                        String chromosome = resultSet.getString("chromosome");
                        ClinicAnnBean clinicAnnBean = new ClinicAnnBean(id, location, gene, evidencelevel, types, annotation_text, related_chemicals, related_diseases, biogeographical_groups, chromosome);
                        clinicAnnBeans.add(clinicAnnBean);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
        return clinicAnnBeans;
    }

}
