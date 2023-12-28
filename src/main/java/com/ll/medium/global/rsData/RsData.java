package com.ll.medium.global.rsData;

import com.ll.medium.standard.base.Empty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.NonNull;

import static lombok.AccessLevel.PROTECTED;

@AllArgsConstructor(access = PROTECTED)
@Getter
public class RsData<T> {
    @NonNull
    private final String resultCode;
    @NonNull
    private final String msg;
    @NonNull
    private final T data;
    @NonNull
    private final int statusCode;

    public static <T> RsData<T> of(String resultCode, String msg, T data) {
        int statusCode = Integer.parseInt(resultCode);

        return new RsData<>(resultCode, msg, data, statusCode);
    }

    public <T> RsData<T> of(T data) {
        return RsData.of(resultCode, msg, data);
    }

    public static RsData<Empty> of(String resultCode, String msg) {
        return of(resultCode, msg, new Empty());
    }

    @NonNull
    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 400;
    }

    @NonNull
    public boolean isFail() {
        return !isSuccess();
    }
}