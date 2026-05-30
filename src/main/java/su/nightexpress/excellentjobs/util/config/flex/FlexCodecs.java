package su.nightexpress.excellentjobs.util.config.flex;

import su.nightexpress.nightcore.util.ArrayUtil;

public class FlexCodecs {

    private static final String ARRAY_DELIMITER = "~";

    private FlexCodecs() {
    }

    public static final FlexPropertyCodec<Boolean> BOOLEAN = FlexPropertyCodec.of(Boolean::parseBoolean,
        String::valueOf);
    public static final FlexPropertyCodec<Double>  DOUBLE  = FlexPropertyCodec.of(Double::parseDouble, String::valueOf);
    public static final FlexPropertyCodec<Integer> INTEGER = FlexPropertyCodec.of(Integer::parseInt, String::valueOf);
    public static final FlexPropertyCodec<Long>    LONG    = FlexPropertyCodec.of(Long::parseLong, String::valueOf);
    public static final FlexPropertyCodec<String>  STRING  = FlexPropertyCodec.of(s -> s, s -> s);

    public static final FlexPropertyCodec<double[]> DOUBLE_ARRAY = FlexPropertyCodec.of(
        str -> ArrayUtil.parseDoubleArray(str, ARRAY_DELIMITER),
        arr -> ArrayUtil.arrayToString(arr, ARRAY_DELIMITER)
    );

    public static final FlexPropertyCodec<int[]> INT_ARRAY = FlexPropertyCodec.of(
        str -> ArrayUtil.parseIntArray(str, ARRAY_DELIMITER),
        arr -> ArrayUtil.arrayToString(arr, ARRAY_DELIMITER)
    );

    public static final FlexPropertyCodec<long[]> LONG_ARRAY = FlexPropertyCodec.of(
        str -> ArrayUtil.parseLongArray(str, ARRAY_DELIMITER),
        arr -> ArrayUtil.arrayToString(arr, ARRAY_DELIMITER)
    );
}
