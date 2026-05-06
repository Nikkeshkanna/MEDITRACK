package com.meditrack.service;

import com.meditrack.entity.Patient;
import com.meditrack.exception.ResourceNotFoundException;
import com.meditrack.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@SuppressWarnings("null")
public class EmergencyService {

    @Autowired private PatientRepository patientRepository;

    public Map<String, Object> getEmergencyInfoByUHID(String uhid) {
        Patient patient = patientRepository.findByUhid(uhid)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "UHID", uhid));
        return buildEmergencyInfo(patient);
    }

    public Map<String, Object> getEmergencyInfoByMobile(String mobile) {
        Patient patient = patientRepository.findByMobile(mobile)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "mobile", mobile));
        return buildEmergencyInfo(patient);
    }

    private Map<String, Object> buildEmergencyInfo(Patient p) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("name", p.getName());
        m.put("uhid", p.getUhid());
        m.put("bloodGroup", p.getBloodGroup());
        m.put("allergies", p.getAllergies());
        m.put("chronicDiseases", p.getChronicDiseases());
        m.put("currentMedications", p.getCurrentMedications());
        m.put("emergencyContactName", p.getEmergencyContactName());
        m.put("emergencyContactMobile", p.getEmergencyContactMobile());
        m.put("emergencyContactRelation", p.getEmergencyContactRelation());
        m.put("gender", p.getGender());
        m.put("dob", p.getDob());
        return m;
    }
}
