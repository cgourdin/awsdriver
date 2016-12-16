/**
 * Copyright (c) 2016 Christophe Gourdin
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * Contributors:
 * - Christophe Gourdin
 */
package org.occiware.clouddriver.IAM;

/**
 * Created by christophe on 10/12/2016.
 */
public class KeyPairDO {

    private String privateKey;
    private String publicKey;

    private String fingerPrintPrivateKey;
    private String fingerPrintPublicKey;
    private String keyPairName;

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getFingerPrintPrivateKey() {
        return fingerPrintPrivateKey;
    }

    public void setFingerPrintPrivateKey(String fingerPrintPrivateKey) {
        this.fingerPrintPrivateKey = fingerPrintPrivateKey;
    }

    public String getKeyPairName() {
        return keyPairName;
    }

    public void setKeyPairName(String keyPairName) {
        this.keyPairName = keyPairName;
    }

    public String getFingerPrintPublicKey() {
        return fingerPrintPublicKey;
    }

    public void setFingerPrintPublicKey(String fingerPrintPublicKey) {
        this.fingerPrintPublicKey = fingerPrintPublicKey;
    }
}
