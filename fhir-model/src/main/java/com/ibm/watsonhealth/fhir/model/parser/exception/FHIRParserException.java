/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.parser.exception;

public class FHIRParserException extends Exception {
    private static final long serialVersionUID = 1L;
    protected final String path;

    public FHIRParserException(String message, String path, Throwable cause) {
        super(message, cause);
        this.path = path;
    }
    
    public String getPath() {
        return path;
    }
}