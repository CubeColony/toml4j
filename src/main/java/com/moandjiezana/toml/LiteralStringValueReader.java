package com.moandjiezana.toml;

import java.util.concurrent.atomic.AtomicInteger;


class LiteralStringValueReader implements ValueReader {

    static final LiteralStringValueReader LITERAL_STRING_VALUE_READER = new LiteralStringValueReader();

    @Override
    public boolean canRead(String s) {
        return s.startsWith("'");
    }

    @Override
    public Object read(String s, AtomicInteger index, Context context) {
        final int startLine = context.line().get();
        boolean terminated = false;
        final int startIndex = index.incrementAndGet();

        for (int i = index.get(); i < s.length(); i = index.incrementAndGet()) {
            final char c = s.charAt(i);

            if (c == '\'') {
                terminated = true;
                break;
            }
        }

        if (!terminated) {
            final Results.Errors errors = new Results.Errors();
            errors.unterminated(context.identifier().getName(), s.substring(startIndex), startLine);
            return errors;
        }

        return s.substring(startIndex, index.get());
    }

    private LiteralStringValueReader() {
    }
}
