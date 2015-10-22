package com.mhgad.za.vitel.billing.batch;

import com.mhgad.za.vitel.billing.batch.model.Cdr;
import java.text.SimpleDateFormat;
import org.springframework.batch.item.file.transform.FieldExtractor;

/**
 *
 * @author plourand
 */
public class CdrFieldExtractor implements FieldExtractor<Cdr> {

    private static final int FIELD_COUNT = 16;
    private static final String DOUBLE_QUOTE = "\"";

    public CdrFieldExtractor() {
    }

    @Override
    public Object[] extract(Cdr item) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Object[] values = new Object[FIELD_COUNT];
        values[0] = formatter.format(item.getCallDate());
        values[1] = DOUBLE_QUOTE + item.getClid() + DOUBLE_QUOTE;
        values[2] = DOUBLE_QUOTE + item.getSrc() + DOUBLE_QUOTE;
        values[3] = DOUBLE_QUOTE + item.getDst() + DOUBLE_QUOTE;
        values[4] = DOUBLE_QUOTE + item.getDcontext() + DOUBLE_QUOTE;
        values[5] = DOUBLE_QUOTE + item.getChannel() + DOUBLE_QUOTE;
        values[6] = DOUBLE_QUOTE + item.getDstchannel() + DOUBLE_QUOTE;
        values[7] = DOUBLE_QUOTE + item.getLastapp() + DOUBLE_QUOTE;
        values[8] = DOUBLE_QUOTE + item.getLastdata() + DOUBLE_QUOTE;
        values[9] = DOUBLE_QUOTE + item.getDuration() + DOUBLE_QUOTE;
        values[10] = DOUBLE_QUOTE + item.getBillsec() + DOUBLE_QUOTE;
        values[11] = DOUBLE_QUOTE + item.getDisposition() + DOUBLE_QUOTE;
        values[12] = DOUBLE_QUOTE + item.getAmaflags() + DOUBLE_QUOTE;
        values[13] = DOUBLE_QUOTE + item.getAccountcode() + DOUBLE_QUOTE;
        values[14] = DOUBLE_QUOTE + item.getUniqueid() + DOUBLE_QUOTE;
        values[15] = DOUBLE_QUOTE + item.getUserfield() + DOUBLE_QUOTE;

        Double cost = item.getCost();
        if (cost != null) {
            values[16] = DOUBLE_QUOTE + cost + DOUBLE_QUOTE;
        } else {
            values[16] = DOUBLE_QUOTE + DOUBLE_QUOTE;
        }

        return values;
    }
}
