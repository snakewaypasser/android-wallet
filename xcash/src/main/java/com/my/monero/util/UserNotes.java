/*
 * Copyright (c) 2019-2020 The X-Cash Foundation
 * Copyright (c) 2017 m2049r
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.my.monero.util;


import com.my.monero.api.QueryOrderStatus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserNotes {

    public String txNotes = "";
    public String note = "";
    public String xmrtoKey = null;
    public String xmrtoAmount = null; // could be a double - but we are not doing any calculations
    public String xmrtoDestination = null;

    public UserNotes(final String txNotes) {
        if (txNotes == null) {
            return;
        }
        this.txNotes = txNotes;
        Pattern p = Pattern.compile("^\\{(xmrto-\\w{6}),([0-9.]*)BTC,(\\w*)\\} ?(.*)");
        Matcher m = p.matcher(txNotes);
        if (m.find()) {
            xmrtoKey = m.group(1);
            xmrtoAmount = m.group(2);
            xmrtoDestination = m.group(3);
            note = m.group(4);
        } else {
            note = txNotes;
        }
    }

    public void setNote(String newNote) {
        if (newNote != null) {
            note = newNote;
        } else {
            note = "";
        }
        txNotes = buildTxNote();
    }

    public void setXmrtoStatus(QueryOrderStatus xmrtoStatus) {
        if (xmrtoStatus != null) {
            xmrtoKey = xmrtoStatus.getUuid();
            xmrtoAmount = String.valueOf(xmrtoStatus.getBtcAmount());
            xmrtoDestination = xmrtoStatus.getBtcDestAddress();
        } else {
            xmrtoKey = null;
            xmrtoAmount = null;
            xmrtoDestination = null;
        }
        txNotes = buildTxNote();
    }

    private String buildTxNote() {
        StringBuffer stringBuffer = new StringBuffer();
        if (xmrtoKey != null) {
            if ((xmrtoAmount == null) || (xmrtoDestination == null))
                throw new IllegalArgumentException("Broken notes");
            stringBuffer.append("{");
            stringBuffer.append(xmrtoKey);
            stringBuffer.append(",");
            stringBuffer.append(xmrtoAmount);
            stringBuffer.append("BTC,");
            stringBuffer.append(xmrtoDestination);
            stringBuffer.append("}");
            if ((note != null) && (!note.isEmpty()))
                stringBuffer.append(" ");
        }
        stringBuffer.append(note);
        return stringBuffer.toString();
    }

}