/************************************************************************
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER
 * 
 * Copyright 2008, 2010 Oracle and/or its affiliates. All rights reserved.
 * 
 * Use is subject to license terms.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0. You can also
 * obtain a copy of the License at http://odftoolkit.org/docs/license.txt
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ************************************************************************/
package uk.org.taverna.scufl2.ucfpackage.impl.odfdom.pkg.manifest;

public class EncryptionData {

    private String _checksumType;
    private String _checksum;
    Algorithm _algorithm;
    KeyDerivation _keyDerivation;

    public EncryptionData() {
    }

    public EncryptionData(String checksumType, String checksum,
                          Algorithm algorithm, KeyDerivation keyDerivation) {

        _checksumType=checksumType;
        _checksum=checksum;
        _algorithm=algorithm;
        _keyDerivation=keyDerivation;
    }

    public void setChecksumType(String checksumType) {
        _checksumType=checksumType;
    }

    public String getChecksumType() {
        return _checksumType;
    }

    public void setChecksum(String checksum) {
        _checksum=checksum;
    }

    public String getChecksum() {
        return _checksum;
    }

    public void setAlgorithm(Algorithm algorithm) {
        _algorithm=algorithm;
    }

    public Algorithm getAlgorithm() {
        return _algorithm;
    }

    public void setKeyDerivation(KeyDerivation keyDerivation) {
        _keyDerivation=keyDerivation;
    }

    public KeyDerivation getKeyDerivation() {
        return _keyDerivation;
    }

}
