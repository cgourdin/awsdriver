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
 * Created by christophe on 11/12/2016.
 */
public class KeyPairOperationException extends Exception {


    public KeyPairOperationException() {
    }

    public KeyPairOperationException(String message) {
        super(message);
    }

    public KeyPairOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeyPairOperationException(Throwable cause) {
        super(cause);
    }
}
