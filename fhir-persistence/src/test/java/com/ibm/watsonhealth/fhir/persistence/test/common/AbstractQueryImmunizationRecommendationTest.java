/**
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.test.common;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.ImmunizationRecommendation;
import com.ibm.watsonhealth.fhir.model.ImmunizationRecommendationRecommendation;
import com.ibm.watsonhealth.fhir.model.Resource;

/**
 *  This class contains a collection of tests that will be run against
 *  each of the various persistence layer implementations.
 *  There will be a subclass in each persistence project.
 *  
 *  ImmunizationRecommendation chosen because it is one of the only resources with a search parameter of type number.
 */
public abstract class AbstractQueryImmunizationRecommendationTest extends AbstractPersistenceTest {
	
    /**
     * Creates an ImmunicationRecommendation
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" })
    public void testCreateImmunizationRecommendation() throws Exception {
        ImmunizationRecommendationRecommendation imm_recrec = f.createImmunizationRecommendationRecommendation()
                .withDate(f.createDateTime().withValue("2017-01-01T10:10:10Z"))
                .withVaccineCode(f.createCodeableConcept().withText(f.createString().withValue("a vaccine")))
                .withDoseNumber(f.createPositiveInt().withValue(BigInteger.valueOf(10)))
                .withForecastStatus(f.createCodeableConcept().withText(f.createString().withValue("cloudy")));
        
        ImmunizationRecommendation imm_rec = f.createImmunizationRecommendation()
               .withPatient(f.createReference().withDisplay(f.createString().withValue("Pat Ient")))
               .withRecommendation(imm_recrec);
        persistence.create(getDefaultPersistenceContext(), imm_rec);
        assertNotNull(imm_rec);
        assertNotNull(imm_rec.getId());
        assertNotNull(imm_rec.getId().getValue());
        assertNotNull(imm_rec.getMeta());
        assertNotNull(imm_rec.getMeta().getVersionId().getValue());
        assertEquals("1", imm_rec.getMeta().getVersionId().getValue());
    }
    
    /**
     * Query for ImmunizationRecommendation with dose-number = 10
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateImmunizationRecommendation" })
    public void testCompositionQuery_Number_postive_integer() throws Exception {
        List<Resource> resources = runQueryTest(ImmunizationRecommendation.class, persistence, "dose-number", "10");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Query for ImmunizationRecommendation with dose-number = 10
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateImmunizationRecommendation" })
    public void testCompositionQuery_Number_postive_double() throws Exception {
        List<Resource> resources = runQueryTest(ImmunizationRecommendation.class, persistence, "dose-number", "10.0");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Query for ImmunizationRecommendation with dose-number < 11
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc-normalized" }, dependsOnMethods = { "testCreateImmunizationRecommendation" })
    public void testCompositionQuery_Number_positive_comparison_lt() throws Exception {
        List<Resource> resources = runQueryTest(ImmunizationRecommendation.class, persistence, "dose-number", "lt11");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Query for ImmunizationRecommendation with dose-number <= 11
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc-normalized" }, dependsOnMethods = { "testCreateImmunizationRecommendation" })
    public void testCompositionQuery_Number_positive_comparison_le() throws Exception {
        List<Resource> resources = runQueryTest(ImmunizationRecommendation.class, persistence, "dose-number", "le11");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Query for ImmunizationRecommendation with dose-number <= 10
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc-normalized" }, dependsOnMethods = { "testCreateImmunizationRecommendation" })
    public void testCompositionQuery_Number_positive_comparison_leNumber2() throws Exception {
        List<Resource> resources = runQueryTest(ImmunizationRecommendation.class, persistence, "dose-number", "le10");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Query for ImmunizationRecommendation with dose-number > 9
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc-normalized" }, dependsOnMethods = { "testCreateImmunizationRecommendation" })
    public void testCompositionQuery_Number_positive_comparison_gt() throws Exception {
        List<Resource> resources = runQueryTest(ImmunizationRecommendation.class, persistence, "dose-number", "gt9");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Query for ImmunizationRecommendation with dose-number >= 9
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc-normalized" }, dependsOnMethods = { "testCreateImmunizationRecommendation" })
    public void testCompositionQuery_Number_positive_comparison_ge() throws Exception {
        List<Resource> resources = runQueryTest(ImmunizationRecommendation.class, persistence, "dose-number", "ge9");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Query for ImmunizationRecommendation with dose-number >= 10
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc-normalized" }, dependsOnMethods = { "testCreateImmunizationRecommendation" })
    public void testCompositionQuery_Number_positive_comparison_geNumber2() throws Exception {
        List<Resource> resources = runQueryTest(ImmunizationRecommendation.class, persistence, "dose-number", "ge10");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Query for ImmunizationRecommendation with dose-number != 9
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc-normalized" }, dependsOnMethods = { "testCreateImmunizationRecommendation" })
    public void testCompositionQuery_Number_positive_comparison_ne() throws Exception {
        List<Resource> resources = runQueryTest(ImmunizationRecommendation.class, persistence, "dose-number", "ne99");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Query for ImmunizationRecommendation on date with year 2017
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateImmunizationRecommendation" })
    public void testCompositionQuery_Date_partialDate_postive() throws Exception {
        List<Resource> resources = runQueryTest(ImmunizationRecommendation.class, persistence, "date", "2017");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Query for ImmunizationRecommendation on date 2017-01-01T10:10:10Z
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateImmunizationRecommendation" })
    public void testCompositionQuery_Date_fullDate_postive() throws Exception {
        List<Resource> resources = runQueryTest(ImmunizationRecommendation.class, persistence, "date", "2017-01-01T10:10:10Z");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Query for ImmunizationRecommendation on date eq2017-01-01T10:10:10Z
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateImmunizationRecommendation" })
    public void testCompositionQuery_Date_fullDate_postive_eq() throws Exception {
        List<Resource> resources = runQueryTest(ImmunizationRecommendation.class, persistence, "date", "eq2017-01-01T10:10:10Z");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Query for ImmunizationRecommendation on date ne9999-01-01T10:10:10Z
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateImmunizationRecommendation" })
    public void testCompositionQuery_Date_fullDate_postive_ne() throws Exception {
        List<Resource> resources = runQueryTest(ImmunizationRecommendation.class, persistence, "date", "ne9999-01-01T10:10:10Z");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Query for ImmunizationRecommendation on date le2017-01-02T10:10:10Z
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateImmunizationRecommendation" })
    public void testCompositionQuery_Date_fullDate_postive_le() throws Exception {
        List<Resource> resources = runQueryTest(ImmunizationRecommendation.class, persistence, "date", "le2017-01-02T10:10:10Z");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Query for ImmunizationRecommendation on date lt2017-01-02T10:10:10Z
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateImmunizationRecommendation" })
    public void testCompositionQuery_Date_fullDate_postive_lt() throws Exception {
        List<Resource> resources = runQueryTest(ImmunizationRecommendation.class, persistence, "date", "lt2017-01-02T10:10:10Z");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Query for ImmunizationRecommendation on date gt2017-01-01T00:00:00Z
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateImmunizationRecommendation" })
    public void testCompositionQuery_Date_fullDate_postive_gt() throws Exception {
        List<Resource> resources = runQueryTest(ImmunizationRecommendation.class, persistence, "date", "gt2017-01-01T00:00:00Z");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Query for ImmunizationRecommendation on date ge2017-01-01
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateImmunizationRecommendation" })
    public void testCompositionQuery_Date_fullDate_postive_ge() throws Exception {
        List<Resource> resources = runQueryTest(ImmunizationRecommendation.class, persistence, "date", "ge2017-01-01");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Query for ImmunizationRecommendation on dates 2016-12-01 - 2017-01-11
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateImmunizationRecommendation" })
    public void testCompositionQuery_Date_fullDate_postive_range() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<>();  
        queryParms.put("date", Arrays.asList("ge2016-12-01","le2017-01-11"));
        List<Resource> resources = runQueryTest(ImmunizationRecommendation.class, persistence, queryParms);
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Query for ImmunizationRecommendation on date ge2017-01-01
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateImmunizationRecommendation" })
    public void testCompositionQuery_Date_fullDate_negative_ge() throws Exception {
        List<Resource> resources = runQueryTest(ImmunizationRecommendation.class, persistence, "date", "ge9999-01-01");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
        
    }
    
    /**
     * Query for ImmunizationRecommendation with dose-number = 525600 which should retrieve no Observations
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateImmunizationRecommendation" })
    public void testCompositionQuery_Number_negative() throws Exception {
        List<Resource> resources = runQueryTest(ImmunizationRecommendation.class, persistence, "dose-number", "525600");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
        
    }
    
    /**
     * Query for ImmunizationRecommendation with dose-number = 525600 which should retrieve no Observations
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateImmunizationRecommendation" })
    public void testCompositionQuery_Date_negative() throws Exception {
        List<Resource> resources = runQueryTest(ImmunizationRecommendation.class, persistence, "date", "2018");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
        
    }
    
    /**
     * Query for ImmunizationRecommendation on dates 2099-12-01 - 3000-01-11
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateImmunizationRecommendation" })
    public void testCompositionQuery_Date_fullDate_negative_range() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<>();  
        queryParms.put("date", Arrays.asList("ge2099-12-01","le3000-01-11"));
        List<Resource> resources = runQueryTest(ImmunizationRecommendation.class, persistence, queryParms);
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
        
    }
}
