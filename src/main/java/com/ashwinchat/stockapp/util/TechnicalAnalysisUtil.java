package com.ashwinchat.stockapp.util;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;

import com.ashwinchat.stockapp.model.view.StockHistoryView;

public final class TechnicalAnalysisUtil {

    private static final String GETTERS_SHOULD_NOT_BE_NULL = "Getters should not be null.";
    private static final String LIST_SHOULD_NOT_BE_EMPTY = "List should not be empty.";

    private TechnicalAnalysisUtil() {
    }

    public static BigDecimal calcSma(List<StockHistoryView> stockHistoryList,
            Function<StockHistoryView, BigDecimal> getter) {
        checkIllegalArgs(stockHistoryList, getter);

        int size = stockHistoryList.size();
        BigDecimal sum = getAllPricesOfType(stockHistoryList, getter).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

        return sum.divide(new BigDecimal(size));
    }

    public static BigDecimal getMax(List<StockHistoryView> stockHistoryList,
            Function<StockHistoryView, BigDecimal> getter) {
        checkIllegalArgs(stockHistoryList, getter);

        return getAllPricesOfType(stockHistoryList, getter).max(Comparator.naturalOrder()).orElse(null);
    }

    public static BigDecimal getMin(List<StockHistoryView> stockHistoryList,
            Function<StockHistoryView, BigDecimal> getter) {
        checkIllegalArgs(stockHistoryList, getter);

        return getAllPricesOfType(stockHistoryList, getter).min(Comparator.naturalOrder()).orElse(null);
    }

    private static void checkIllegalArgs(List<StockHistoryView> stockHistoryList,
            Function<StockHistoryView, BigDecimal> getter) {
        if (Objects.isNull(getter)) {
            throw new IllegalArgumentException(GETTERS_SHOULD_NOT_BE_NULL);
        }

        if (CollectionUtils.isEmpty(stockHistoryList)) {
            throw new IllegalArgumentException(LIST_SHOULD_NOT_BE_EMPTY);
        }
    }

    private static Stream<BigDecimal> getAllPricesOfType(List<StockHistoryView> stockHistoryList,
            Function<StockHistoryView, BigDecimal> getter) {
        return stockHistoryList.stream().map(getter);
    }

}
