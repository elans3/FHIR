/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.path.test;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;

import org.testng.annotations.Test;

import com.ibm.fhir.model.patch.exception.FHIRPatchException;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.path.util.FHIRPathUtil;

public class FHIRPathUtilTest {
    @Test
    void testAdd() throws Exception {
        HumanName name1 = HumanName.builder()
                .given(string("John"))
                .family(string("Smith"))
                .build();
        HumanName name2 = HumanName.builder()
                .given(string("Johnny"))
                .family(string("Smith"))
                .build();
        Patient builderPatient = Patient.builder()
                .id("test")
                .deceased(Boolean.TRUE)
                .name(name1, name2)
                .build();

        Patient fhirpathPatient = Patient.builder().id("test").build();
        fhirpathPatient = FHIRPathUtil.add(fhirpathPatient, "Patient", "deceased", Boolean.TRUE);
        fhirpathPatient = FHIRPathUtil.add(fhirpathPatient, "Patient", "name", name1);
        fhirpathPatient = FHIRPathUtil.add(fhirpathPatient, "Patient", "name", name2);
        
        assertEquals(fhirpathPatient, builderPatient);
    }

    @Test
    void testAddDuplicate() throws Exception {
        HumanName name1 = HumanName.builder()
                .given(string("John"))
                .family(string("Smith"))
                .build();

        HumanName name2 = HumanName.builder()
                .given(string("John"))
                .family(string("Smith"))
                .suffix(Collections.singleton(string("II")))
                .build();
        Patient builderPatient = Patient.builder()
                .id("test")
                .name(name1, name2)
                .build();

        Patient fhirpathPatient = Patient.builder()
                .id("test")
                // note that the same exact object is added twice
                .name(name1, name1)
                .build();
        fhirpathPatient = FHIRPathUtil.add(fhirpathPatient, "Patient.name[1]", "suffix", string("II"));
        assertEquals(fhirpathPatient, builderPatient);


        fhirpathPatient = Patient.builder().id("test").build();
        // note that the same exact object is added twice
        fhirpathPatient = FHIRPathUtil.add(fhirpathPatient, "Patient", "name", name1);
        fhirpathPatient = FHIRPathUtil.add(fhirpathPatient, "Patient", "name", name1);
        fhirpathPatient = FHIRPathUtil.add(fhirpathPatient, "Patient.name[1]", "suffix", string("II"));

        assertEquals(fhirpathPatient, builderPatient);
    }

    @Test(expectedExceptions = FHIRPatchException.class)
    void testAddExisting() throws Exception {
        Patient fhirpathPatient = Patient.builder().deceased(Boolean.FALSE).build();
        fhirpathPatient = FHIRPathUtil.add(fhirpathPatient, "Patient", "deceased", Boolean.TRUE);
    }

    @Test
    void testInsert() throws Exception {
        HumanName name1 = HumanName.builder()
                .given(string("John"))
                .family(string("Smith"))
                .build();
        HumanName name2 = HumanName.builder()
                .given(string("Johnny"))
                .family(string("Smith"))
                .build();
        Patient builderPatient = Patient.builder()
                .id("test")
                .name(name1, name2)
                .build();

        Patient fhirpathPatient = Patient.builder().id("test").build();
        fhirpathPatient = FHIRPathUtil.add(fhirpathPatient, "Patient", "name", name2);
        fhirpathPatient = FHIRPathUtil.insert(fhirpathPatient, "Patient.name", 0, name1);

        assertEquals(fhirpathPatient, builderPatient);
    }

    @Test
    void testDelete() throws Exception {
        HumanName name1 = HumanName.builder()
                .given(string("John"))
                .family(string("Smith"))
                .build();
        Patient patient = Patient.builder()
                .id("test")
                .deceased(Boolean.TRUE)
                .name(name1)
                .build();

        Patient builderPatient = patient.toBuilder()
                .deceased(null)
                .name(Collections.emptySet())
                .build();

        Patient fhirpathPatient = FHIRPathUtil.delete(patient, "Patient.deceased");
        fhirpathPatient = FHIRPathUtil.delete(fhirpathPatient, "Patient.name[0]");

        assertEquals(fhirpathPatient, builderPatient);
    }

    @Test
    void testReplace() throws Exception {
        HumanName name1 = HumanName.builder()
                .given(string("John"))
                .family(string("Smith"))
                .build();
        Patient patient = Patient.builder()
                .deceased(Boolean.TRUE)
                .name(name1)
                .build();
        
        Patient builderPatient = patient.toBuilder()
                .deceased(Boolean.FALSE)
                .name(Collections.singleton(patient.getName().get(0).toBuilder()
                        .given(Arrays.asList(string("Jane")))
                        .build()))
                .build();

        Patient fhirpathPatient = FHIRPathUtil.replace(patient, "Patient.deceased", Boolean.FALSE);
        fhirpathPatient = FHIRPathUtil.replace(fhirpathPatient, "Patient.name[0].given[0]", string("Jane"));

        assertEquals(fhirpathPatient, builderPatient);
    }

    @Test
    void testMove() throws Exception {
        HumanName name1 = HumanName.builder()
                .given(string("John"))
                .family(string("Smith"))
                .build();
        HumanName name2 = HumanName.builder()
                .given(string("Johnny"))
                .family(string("Smith"))
                .build();
        Patient builderPatient = Patient.builder()
                .name(name1, name2)
                .build();

        Patient fhirpathPatient = Patient.builder()
                .name(name2, name1)
                .build();
        fhirpathPatient = FHIRPathUtil.move(fhirpathPatient, "Patient.name", 1, 0);

        assertEquals(fhirpathPatient, builderPatient);
    }
}
